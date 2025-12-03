package org.example.cofffeshopinterface;

import java.util.Random;

public class Barista extends Thread {
    // Declaramos la referecia al buffer
    private Buffer buffer;
    // Saber si el barista tiene que hacer cafes
    private boolean running = true;
    // Creamos un generador aleatorio
    private Random random = new Random();

    public Barista (Buffer buffer) {
        this.buffer = buffer;
    }

    // Simulamos lo que lleva hacer un cafe
    private void prepareCoffe() {
        try {
            Thread.sleep(3_000 + random.nextLong(1_501));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        int count = 1;

        while (running) {
            try {
                prepareCoffe();
                String coffe = "Café nº" + count++;
                buffer.addCoffe(coffe);
            } catch (InterruptedException e) {
                if (!running) break;
            }
        }
        System.out.println("El Barista ya termino de hacer los cafés");
    }
}
