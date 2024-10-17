package ru.mpei;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

//реализует логику самой двусторонней очереди.

public class TripletDeque<E> implements Deque<E>, Containerable {
    private Node<E> head; //ссылка на первый контейнер
    private Node<E> tail; //ссылка на последний контейнер
    private int capacity; //максимальное количество элементов в одном контейнере
    private int size; //общее количество элементов в очереди
    private int lastFilledIndex; // для tail (индекс последнего заполненного элемента в последнем контейнере)
    private int firstFilledIndex; // для head (индекс первого заполненного элемента в первом контейнере)

    public TripletDeque() {
        this(5); //Конструктор по умолчанию, устанавливающий размер контейнера на 5
    }

    public TripletDeque(int capacity) { //Устанавливает размер контейнера, проверяя, чтобы он был положительным
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0.");
        }
        this.capacity = capacity;
        head = new Node<>(capacity);
        tail = head;
        lastFilledIndex = 0;
        firstFilledIndex = -1;
        size = 0;
    }

    //Добавляет элемент в начало очереди. Если первый контейнер переполнен, создает новый контейнер и ставит его впереди
    public void addFirst(E e) {
        if (e == null) throw new NullPointerException("Element must not be null.");
        if (firstFilledIndex == -1) {
            firstFilledIndex = capacity - 1;
        } else {
            if (firstFilledIndex == 0) {
                Node<E> newHead = new Node<>(capacity);
                newHead.next = head;
                head.prev = newHead;
                head = newHead;
                firstFilledIndex = capacity - 1;
            } else {
                firstFilledIndex--;
            }
        }
        head.data[firstFilledIndex] = e;
        size++;
    }

    //Добавляет элемент в конец очереди. Если последний контейнер переполнен, создает новый контейнер и ставит его в конец
    public void addLast(E e) {
        if (e == null) throw new NullPointerException("Element must not be null.");
        if (lastFilledIndex == capacity) {
            Node<E> newTail = new Node<>(capacity);
            tail.next = newTail;
            newTail.prev = tail;
            tail = newTail;
            lastFilledIndex = 0;
        }
        tail.data[lastFilledIndex++] = e;
        size++;
    }

    //добавляют элемент, возвращая true по успеху, false если произошла ошибка.
    public boolean offerFirst(E e) {
        try {
            addFirst(e);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    //добавляют элемент, возвращая true по успеху, false если произошла ошибка.
    public boolean offerLast(E e) {
        try {
            addLast(e);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    //Удаляет элемент с начала или конца очереди, соответствующим образом обновляя индексы и размер
    public E removeFirst() {
        if (size == 0) throw new NoSuchElementException();
        E e = head.data[firstFilledIndex];
        head.data[firstFilledIndex] = null; // очистить
        size--;

        if (firstFilledIndex == capacity - 1) {
            head = head.next;
            if (head != null) {
                firstFilledIndex = 0;
            }
        } else {
            firstFilledIndex++;
        }
        return e;
    }

    //Удаляет элемент с начала или конца очереди, соответствующим образом обновляя индексы и размер
    public E removeLast() {
        if (size == 0) throw new NoSuchElementException();
        lastFilledIndex--;
        E e = tail.data[lastFilledIndex];
        tail.data[lastFilledIndex] = null; // очистить
        size--;

        if (lastFilledIndex == 0 && tail.prev != null) {
            tail = tail.prev;
            lastFilledIndex = capacity;
        }
        return e;
    }

    //Удаляют первый или последний элемент, возвращая null, если очередь пуста.
    public E pollFirst() {
        return size > 0 ? removeFirst() : null;
    }

    //Удаляют первый или последний элемент, возвращая null, если очередь пуста.
    public E pollLast() {
        return size > 0 ? removeLast() : null;
    }

    //Возвращают первый и последний элементы без их удаления
    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return head.data[firstFilledIndex];
    }

    //Возвращают первый и последний элементы без их удаления
    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return tail.data[lastFilledIndex - 1];
    }

    //Аналогичны getFirst(), но возвращают null, если очередь пуста
    public E peekFirst() {
        return size > 0 ? getFirst() : null;
    }

    //Аналогичны getLast(), но возвращают null, если очередь пуста
    public E peekLast() {
        return size > 0 ? getLast() : null;
    }

    //Методы для удаления определенного элемента
    public boolean removeFirstOccurrence(Object o) {
        if (o == null) return false;
        for (Node<E> n = head; n != null; n = n.next) {
            for (int i = 0; i < (n == tail ? lastFilledIndex : capacity); i++) {
                if (o.equals(n.data[i])) {
                    n.data[i] = null; // удалить элемент
                    size--;
                    return true;
                }
            }
        }
        return false;
    }

    //Методы для удаления определенного элемента
    public boolean removeLastOccurrence(Object o) {
        if (o == null) return false;
        for (Node<E> n = tail; n != null; n = n.prev) {
            for (int i = (n == head ? firstFilledIndex : capacity - 1); i >= 0; i--) {
                if (o.equals(n.data[i])) {
                    n.data[i] = null; // удалить элемент
                    size--;
                    return true;
                }
            }
        }
        return false;
    }

    //Методы для проверки добавления элементов
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    //Методы для проверки добавления элементов
    public boolean offer(E e) {
        return offerLast(e);
    }

    public E remove() {
        return removeFirst();
    }

    public E poll() {
        return pollFirst();
    }

    public E element() {
        return getFirst();
    }

    public E peek() {
        return peekFirst();
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            modified |= add(e);
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    public void push(E e) {
        addFirst(e);
    }

    public E pop() {
        return removeFirst();
    }

    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    public boolean contains(Object o) {
        for (Node<E> n = head; n != null; n = n.next) {
            for (E e : n.data) {
                if (o.equals(e)) return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    //Возвращает итератор для обхода элементов очереди
    public Iterator<E> iterator() {
        return new TripletDequeIterator<>(head, firstFilledIndex, capacity);
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    //Бросает исключение, так как не реализован
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException("Descending iterator is not supported.");
    }

    public Object[] getContainerByIndex(int cIndex) {
        Node<E> temp = head;
        int count = 0;
        while (count < cIndex) {
            temp = temp.next;
            count++;
        }
        if (temp == null) {
            return null;
        } else {
            return temp.data;
        }
    }
}