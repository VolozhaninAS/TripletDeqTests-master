package ru.mpei;

import java.util.Iterator;
import java.util.NoSuchElementException;

//реализует итератор для обхода элементов в порядке добавления.

public class TripletDequeIterator <E> implements Iterator<E> {
    private Node<E> currentNode; //текущий контейнер при обходе
    private int currentIndex; //текущий индекс при обходе внутри контейнера
    private final int capacity; //размер контейнера

    public TripletDequeIterator(Node<E> head, int firstFilledIndex, int capacity) {
        this.currentNode = head;
        this.currentIndex = firstFilledIndex < capacity ? firstFilledIndex : 0;
        this.capacity = capacity;
    }

    //Возвращает true, если есть следующий элемент для итерации
    @Override
    public boolean hasNext() {
        return currentNode != null && (currentIndex < (currentNode == currentNode.next ? currentIndex : capacity));
    }

    //Возвращает следующий элемент, если он доступен, и переходит к следующему элементу; иначе выбрасывает NoSuchElementException
    @Override
    public E next() {
        if (!hasNext()) throw new NoSuchElementException();
        E e = currentNode.data[currentIndex++];
        if (currentIndex >= capacity) {
            currentNode = currentNode.next;
            currentIndex = 0;
        }
        return e;
    }
}