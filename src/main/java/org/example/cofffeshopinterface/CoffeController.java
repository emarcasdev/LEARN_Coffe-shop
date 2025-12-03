package org.example.cofffeshopinterface;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.*;
import java.util.concurrent.*;

public class CoffeController {
    // Referencias de la interfaz
    @FXML private Button btnStart;
    @FXML private ListView<String> listClients;
    @FXML private ListView<String> listWaiters;
    @FXML private ListView<String> listCoffes;

    private Buffer buffer;
    private Barista barista;

    // Creamos las listas para clientes y camareros para la simulación
    private final List<Client> clients = new ArrayList<>();
    private final List<Waiter> waiters = new ArrayList<>();

    // Planificador para poder refrescar la interfaz cada x tiempo
    private ScheduledExecutorService uiRefresh;

    @FXML
    private void initialize() {
        // Creamos el refrescador periódico para las listas
        uiRefresh = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread uiRefreshThread = new Thread(runnable, "ui-refresh");
            return uiRefreshThread;
        });
        // Refrescar la interfaz cada 250 ms
        uiRefresh.scheduleAtFixedRate(this::refreshUI, 0, 250, TimeUnit.MILLISECONDS);
    }

    @FXML
    private void onSimulate() {
        btnStart.setDisable(true);

        // Limpiamos las listas de la interfaz
        listClients.getItems().clear();
        listWaiters.getItems().clear();
        clients.clear();
        waiters.clear();

        // Creamos el buffer con su capacidad
        buffer = new Buffer(2);

        // Instanciamos a los camareros
        waiters.add(new Waiter("Roberto", buffer));
        waiters.add(new Waiter("Nuria", buffer));

        // Asignar el buffer a cada camarero
        for (Waiter waiter : waiters) {
            waiter.setBuffer(buffer);
        }

        // Creamos al barista
        barista = new Barista(buffer);
        barista.start();

        // Instanciamos los clientes con sus tiempos como en el Main
        clients.add(new Client("David",  45_000));
        clients.add(new Client("Diego",  75_000));
        clients.add(new Client("Agustín",120_000));
        clients.add(new Client("Adriano",90_000));
        clients.add(new Client("John",   60_000));
        clients.add(new Client("Iván",  105_000));

        // Creamos un hilp que ejecutará el metodo que con la lógica principal
        Thread mainThread = new Thread(this::runSimulation, "main-thread");
        mainThread.start();
    }

    // Funcion para iniciar la simulacion como el main
    private void runSimulation() {
        try {
            // Iniciamos con el servicio arrancando los hilos de clientes y camereros
            for (Waiter waiter : waiters) {
                waiter.start();
            }

            // Lanzamos los clientes de forma que como máximo haya 2 activos
            for (Client client : clients) {
                client.start();
            }

            // Esperar a que todos los clientes se hayan sido atendidos o se hayan ido
            for (Client client : clients) {
                client.join();
            }

            // Esperar a que los camareros hayan terminado con su trabajo
            for (Waiter waiter : waiters) {
                waiter.running = false;
            }
            for (Waiter waiter : waiters) {
                waiter.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            Platform.runLater(() -> btnStart.setDisable(false));
        }
    }

    // Función para refresacar y mostrar el contenido actual de los clientes y camareros
    private void refreshUI() {
        // Aun no hay buffer para mostrar
        if (buffer == null) {
            Platform.runLater(() -> {
                listClients.getItems().clear();
                listWaiters.getItems().clear();
                listCoffes.getItems().clear();
            });
            return;
        }

        // Asignar cliente a un camarero para que ninguno qude parado
        for (Waiter waiter : waiters) {
            if (waiter.assignament == null) {
                for (Client client : clients) {
                    if (client.inFile && !client.assigned && !client.left && !client.served) {
                        client.assigned = true;
                        waiter.assignament = client;
                        break;
                    }
                }
            }
        }

        // Lista pera guardar los estodos de los clientes
        List<String> clientsStates = new ArrayList<>();
        for (Client client : clients) {
            // Mostrar un texto u otro para ver en la interfaz el estado de cada cliente
            String state;
            if (client.served) {
                state = "Recibio su café en (" + client.preparationTime + " ms)";
            } else if (client.left) {
                state = "Se marco una bomba de humo";
            } else if (client.assigned) {
                state = "Está atendido";
            } else if (client.inFile) {
                state = "Está esperando en la cola";
            } else {
                state = "Pendiente";
            }
            // Texto informativo que mostraremos en la interfaz
            clientsStates.add(String.format(client.name + " — " + state + " [espera maxima: " + client.waitingTime + " ms]"));
        }

        // Lista pera guardar los estodos de los clientes
        List<String> waitersStates = new ArrayList<>();
        for (Waiter waiter : waiters) {
            // Mostrar un texto u otro para ver en la interfaz el estado de cada camarero
            String state;
            if (waiter.assignament == null) {
                state = "Está libre";
            } else {
                state = "Preparando el café a " + waiter.assignament.name;
            }
            // Texto informativo que mostraremos en la interfaz
            waitersStates.add(String.format(waiter.name + " — " + state));
        }

        // Mostrar los cafes actualmente en el buffer
        List<String> coffesStates = new ArrayList<>();
        var coffesInBuffer = buffer.getCoffesList(); // ya es synchronized en Buffer
        coffesStates.add("Cafés en espera (" + coffesInBuffer.size() + "/" + waiters.size() + "):");

        // Actualizamos las listas de la interfaz
        Platform.runLater(() -> {
            listClients.getItems().setAll(clientsStates);
            listWaiters.getItems().setAll(waitersStates);
            listCoffes.getItems().setAll(coffesStates);
        });
    }

    @FXML
    private void addWaiter() {
        if (buffer == null) return;
        // Creamos al camarero
        Waiter newWaiter = new Waiter("Camarero " + (waiters.size() + 1), buffer);
        waiters.add(newWaiter);
        newWaiter.start();

        // Actualizamos la capacidad del buffer
        buffer.setCapacity(waiters.size());
    }
}
