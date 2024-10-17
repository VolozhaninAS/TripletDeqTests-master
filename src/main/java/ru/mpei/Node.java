package ru.mpei;

//отвечает за хранение данных и ссылок на соседние контейнеры.

public class Node <E>  { //массив для хранения элементов
    E[] data;
    Node<E> next; //ссылка на следующий контейнер
    Node<E> prev; //ссылка на предыдущий контейнер.

    @SuppressWarnings("unchecked")
    public Node(int capacity) { //инициализирует массив заданного размера и устанавливает ссылки на соседние контейнеры в null
        data = (E[]) new Object[capacity];
        next = null;
        prev = null;
    }
}
