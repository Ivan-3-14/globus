package module1_2;

import java.util.Objects;

/*
 * Своя реализация хэш-таблицы с разрешением коллизий через списки (chaining).
 *
 * Внутри: массив бакетов (Node[] table). Каждый бакет - односвязный список узлов.
 * При превышении load factor (0.75) происходит рехэширование (удвоение capacity).
 *
 * Big O:
 *   put   : O(1) среднее, O(n) худшее
 *   get   : O(1) среднее, O(n) худшее
 *   remove: O(1) среднее, O(n) худшее
 *   size  : O(1)
 */
public class MyHashMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node<K, V>[] table;
    private int size;
    // порог для рехэширования = capacity * LOAD_FACTOR
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    //Добавление/обновление. Возвращает предыдущее значение или null.
    public V put(K key, V value) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        // Ищем существующий ключ в цепочке бакета
        for (Node<K, V> element = table[index]; element != null; element = element.next) {
            if (element.hash == hash && Objects.equals(element.key, key)) {
                V old = element.value;
                element.value = value;
                return old;
            }
        }

        // Если ключа нет — вставляем в голову списка (O(1))
        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        size++;

        // Проверяем размер: требуется ли ребалансировка?
        if (size > threshold) {
            resize();
        }
        return null;
    }

    // Получение значения по ключу.
    public V get(Object key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for (Node<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && Objects.equals(e.key, key)) {
                return e.value;
            }
        }
        return null;
    }

    // Проверка наличия ключа.
    public boolean containsKey(Object key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for (Node<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && Objects.equals(e.key, key)) {
                return true;
            }
        }
        return false;
    }

    // Удаление по ключу. Возвращает удалённое значение или null.
    public V remove(Object key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        Node<K, V> previous = null;
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash && Objects.equals(current.key, key)) {
                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return current.value;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    // Рехэширование: удваиваем capacity, перераспределяем все узлы.
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length << 1;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];

        for (Node<K, V> head : table) {
            Node<K, V> e = head;
            while (e != null) {
                Node<K, V> next = e.next;
                int newIndex = indexFor(e.hash, newCapacity);
                e.next = newTable[newIndex];
                newTable[newIndex] = e;
                e = next;
            }
        }

        table = newTable;
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }

    // Для тестов: текущая ёмкость таблицы
    int capacity() {
        return table.length;
    }

    /* Хэш-функция с беззнаковым сдвигом вправо на 16 позиций (перенос старших битов на места младших), чтобы уменьшить
    коллизии.
    */
    private static int hash(Object key) {
        int h = (key == null) ? 0 : key.hashCode();
        return h ^ (h >>> 16);
    }

    // Индекс бакета: hash & (capacity - 1). Работает, т.к. capacity — степень двойки.
    private static int indexFor(int hash, int capacity) {
        return hash & (capacity - 1);
    }
}

 /*       put/get/remove — O(1) в среднем:
           - hash(key) и indexFor(hash, capacity) — константные битовые операции (XOR, сдвиг, &).
           - Работа идёт только внутри одного бакета: обход цепочки длиной L.
           - Благодаря load factor <= 0.75 средняя длина цепочки L = size / capacity
             не превышает 0.75, то есть является константой. Отсюда O(1) в среднем.
           - Вставка нового узла выполняется в голову списка — O(1), без обхода цепочки.
           - Удаление и обновление — переподвеска одной ссылки previous.next = current.next,
             тоже O(1) после того, как узел найден.

         put/get/remove — O(n) в худшем случае:
           - Если hashCode() всех ключей даёт одинаковый индекс бакета (плохая хэш-функция
        или специально подобранные ключи), все n элементов попадают в одну цепочку.
           - Тогда поиск/удаление вырождаются в линейный обход списка длины n -> O(n).
           - Именно для недопущения этого применяются: перемешивание битов h ^ (h >>> 16)
           и поддержание load factor <= 0.75 через resize().

          size() — O(1):
           - Размер хранится в поле size и обновляется при put/remove, не пересчитывается.

         Память — O(n + m), где m = capacity:
           - массив бакетов длины m + по одному узлу Node на каждую пару ключ-значение.
 */
