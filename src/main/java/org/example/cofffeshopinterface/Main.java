package org.example.cofffeshopinterface;

import java.util.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Creamos los clientes
        List<Client> clients = new ArrayList<>();
        clients.add(new Client("David",  45_000));
        clients.add(new Client("Diego",  75_000));
        clients.add(new Client("Agustín",120_000));
        clients.add(new Client("Adriano",90_000));
        clients.add(new Client("John",   60_000));
        clients.add(new Client("Iván",  105_000));

        // Creamos los camareros
        List<Waiter> waiters = new ArrayList<>();
        waiters.add(new Waiter("Roberto"));
        waiters.add(new Waiter("Nuria"));

        // Iniciamos con el servicio arrancando los hilos de clientes y camereros
        for (Waiter waiter : waiters) {
            waiter.start();
        }

        // Lanzamos los clientes de forma que como máximo haya 2 activos
        for (Client client : clients) {
            // Esperamos a que un camarero este libre para que pase el siguiente cliente
            Waiter waiterFree = null;
            while (waiterFree == null) {
                for (Waiter waiter : waiters) {
                    // Si el camarero está libre
                    if (waiter.assignament == null) {
                        waiterFree = waiter;
                        break;
                    }
                }
            }

            // Asignar el camarero libre a nuestro cliente
            client.assigned = true;
            waiterFree.assignament = client;

            // El cliente entra
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

        System.out.println("FIN DEL SERVICIO: Ya no queda nadie en la cola");
    }
}