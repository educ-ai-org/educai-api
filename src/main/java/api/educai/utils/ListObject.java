package api.educai.utils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

@JsonSerialize(using = ListObjectSerializer.class)
@JsonDeserialize(using = ListObjectDeserializer.class)
public class ListObject<T> {
    @Getter
    private T[] array;
    private int numElements;

    public ListObject(int size) {
        array = (T[]) new Object[size];
        numElements = 0;
    }

    public void add(T element) {
        if(numElements == array.length) {
            throw new IllegalStateException("List full");
        }

        array[numElements++] = element;
    }

    public void addInIndex(T element, int index) {
        if(index < numElements && index > -1) {
            array[index] = element;
        }
    }

    public int search(T searchedElement) {
        int index = -1;

        for (int i = 0; i < array.length; i++) {
            if(array[i] != null && array[i].equals(searchedElement) && i < numElements) {
                index = i;
                break;
            }
        }

        return index;
    }

    public boolean removeByIndex(int index) {
        boolean wasRemoved = false;

        if(index < numElements && index > -1) {
            for (int i = index; i < numElements - 1; i++) {
                array[i] = array[i + 1];
            }

            array[numElements - 1] = null;
            wasRemoved = true;
            numElements--;
        }

        return wasRemoved;
    }

    public boolean removeElement(T elementToRemove) {
        return removeByIndex(search(elementToRemove));
    }

    public int getSize() {
        return numElements;
    }

    public int getLength() {
        return array.length;
    }

    public T getElement(int index) {
        return index < numElements && index > -1 ? array[index] : null;
    }

    public void clear() {
        array = (T[]) new Object[array.length];
    }

    public void display() {
        for (Object item: array) {
            if(item != null) {
                System.out.printf(item.toString());
            }
        }
    }
}