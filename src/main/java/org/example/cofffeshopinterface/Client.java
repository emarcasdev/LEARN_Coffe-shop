package org.example.cofffeshopinterface;

public class Client extends Thread {
    // Nombre del cliente
    String name;
    // Tiempo que el cliente esta dispuesto a esperar
    int waitingTime;

    // Estado para cuando el cliente esta en la cola
    volatile boolean inFile = false;
    // Estado para cuando el cliente esta atendido por un camarero
    volatile boolean assigned = false;
    // Estado para cuando el cliente ya tiene su cafe
    volatile boolean served = false;
    // Estado para cuando el cliente se marcho
    volatile boolean left = false;
    // Tiempo en el que el cliente llega
    long arriveAt = Long.MAX_VALUE;

    // Tiempo en el que el camarero tarda en hacerle su cafe
    long preparationTime = 0;

    // El cliente llega al local
    long timeStart;

    public Client(String name, int waitingTime) {
        this.name = name;
        this.waitingTime = waitingTime;
    }

    @Override
    public void run() {
        // Cuando el cliente entra en el local marcamos su llegada
        timeStart = System.currentTimeMillis();
        arriveAt = timeStart;
        inFile = true;
        System.out.println("--> Entra el cliente " + name + ".");

        // Si el cliente todavia no fue servido
        while (!served) {
            // Esperar a recibir el cafe o irse si tarda mas de lo esperado
            long waiting = System.currentTimeMillis() - timeStart;
            if (waiting >= waitingTime) {
                left = true;
                System.out.println("[El cliente: " + name + "] Se marcho porque tuvo que esperar " + waiting + " ms.");
                return;
            }
        }

        // Si el cliente recibio el cafe mostramos su nombre y cuanto tiempo tardaron en preparar su cafe el camerero
        System.out.println("[El cliente: " + name + "] Recibio su café en " + preparationTime + " ms");
    }
}