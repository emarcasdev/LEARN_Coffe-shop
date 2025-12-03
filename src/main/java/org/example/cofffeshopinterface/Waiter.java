package org.example.cofffeshopinterface;

import java.util.*;

public class Waiter extends Thread {
    // Nombre del camarero
    volatile String name;
    // Nombre del cliente asignado
    volatile Client assignament = null;
    // Declaramos la referecia al buffer
    private Buffer buffer;
    // Saber si el camarero tiene trabajo
    boolean running = true;
    // Tiempo que llevo la preparacion del cafe
    long preparationTime;
    // Camarero empiza a atender
    long timeStart;

    public Waiter (String name, Buffer buffer) {
        this.name = name;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (running || assignament != null) {
            // Si no tenemos cliente asignado esperamos y volvemos a comprobar
            if (assignament == null) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                continue;
            }

            // Obtenemos el cliente que estamos antendiendo
            Client client = assignament;
            // Verificamos si justamente se fue de la cafetería antes de empezar a hacerle su cafe
            if (client.left) {
                assignament = null;
                continue;
            }

            try {
                System.out.println("[Camarero: " + name + "] Atendiendo al cliente " + client.name + ".");
                // Empieza a hacer el cafe
                timeStart = System.currentTimeMillis();
                // Esperamos hasta tener los cafés
                buffer.takeCoffe();
                // El tiempo que le llevo hacer el cafe
                preparationTime = System.currentTimeMillis() - timeStart;

                // Si el cliente no nos hizo bomba de humo, le entregamos su cafe
                if (!client.left) {
                    client.preparationTime = preparationTime;
                    client.served = true;
                } else {
                    // Si el cliente se marcho durante la preparacion de su cafe, lo notificamos
                    System.out.println("[Camarero: " + name + "] El cliente " + client.name + " se fue de la cola sin su café.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // Liberamos al camerero para que pueda atender a otro cliente
            assignament = null;
        }
    }

    // Actualizar el buffer
    public void setBuffer(Buffer buffer) {
        this.buffer = buffer;
    }
}