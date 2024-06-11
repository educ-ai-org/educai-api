package api.educai.utils;

import lombok.Getter;

@Getter
public class FilaObj<T> {

    private int size;
    private T[] fila;

    public FilaObj(int capacidade) {
        fila = (T[]) new Object[capacidade];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == fila.length;
    }

    public void insert(T info) {
        if (isFull()) {
            throw new IllegalStateException("Fila cheia!");
        }
        else {
            fila[size++] = info;
        }

    }

    public T peek() {
        return fila[0];
    }

    public T poll() {
        T first = fila[0];

        if (!isEmpty()) {
            for (int i = 0; i < size -1; i++) {
                fila[i] = fila[i+1];
            }
            fila[--size] = null;
        }

        return first;
    }

    public void show() {
        if (isEmpty()) {
            System.out.println("Fila vazia!");
        }
        else {
            System.out.println("Conteúdo da fila:");
            for (int i = 0; i < size; i++) {
                System.out.println(fila[i]);
            }
        }

    }

}
