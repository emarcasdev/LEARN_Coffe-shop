package org.example.cofffeshopinterface;

import java.util.*;

public class Buffer {
    // Lista de los cafés preparados
    private Queue<String> coffesList = new LinkedList<>();
    // Capaciadad maxima 1 cafe por camarero
    private int capacity;

    public Buffer(int capacity) {
        this.capacity = capacity;
    }

    // Actualizar la cantidad que puede preparar
    public synchronized void setCapacity(int newCapacity) {
        this.capacity = newCapacity;
        notifyAll(); // Avisar al barista que cree mas cafés
    }

    // Barista hace los cafés
    public synchronized void addCoffe(String coffe) throws InterruptedException {
        while (coffesList.size() == capacity) {
            wait(); // Esperar hasta tener los cafes preparados
        }
        coffesList.add(coffe);
        System.out.println("Café listo: " + coffe);
        notifyAll(); // Avisar a camareros
    }

    // Los camareros cogen los cafés para darselos a los clientes
    public synchronized String takeCoffe() throws InterruptedException {
        while (coffesList.isEmpty()) {
            wait(); // Esperar mientras no estén los cafes
        }
        String coffe = coffesList.remove();
        notifyAll();
        return coffe;
    }

    // Obtener la lista de cafés
    public synchronized List<String> getCoffesList() {
        return new ArrayList<>(coffesList);
    }
}
