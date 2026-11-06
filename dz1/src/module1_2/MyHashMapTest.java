package module1_2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyHashMapTest {

    @Test
    void emptyMap() {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertNull(map.get("nope"));
        assertNull(map.remove("nope"));
        assertFalse(map.containsKey("nope"));
    }

    @Test
    void singleElement() {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        assertNull(map.put("a", 1));
        assertEquals(1, map.size());
        assertEquals(1, map.get("a"));
        assertTrue(map.containsKey("a"));

        // обновление не увеличивает размер
        assertEquals(1, map.put("a", 2));
        assertEquals(1, map.size());
        assertEquals(2, map.get("a"));

        // удаление
        assertEquals(2, map.remove("a"));
        assertEquals(0, map.size());
        assertNull(map.get("a"));
    }

    @Test
    void nullKeyAndValue() {
        MyHashMap<String, String> map = new MyHashMap<>();
        map.put(null, "nullVal");

        assertEquals("nullVal", map.get(null));
        assertEquals("nullVal", map.remove(null));
        assertNull(map.get(null));

        map.put("k", null);
        assertTrue(map.containsKey("k"));
        assertNull(map.get("k"));
    }

    @Test
    void collisionsResolvedByChaining() {
        // Все ключи с одинаковым hashCode -> один бакет
        class BadKey {
            final int id;
            BadKey(int id) { this.id = id; }
            @Override public int hashCode() { return 42; }
            @Override public boolean equals(Object o) {
                return o instanceof BadKey && ((BadKey) o).id == id;
            }
        }
        MyHashMap<BadKey, Integer> map = new MyHashMap<>();
        for (int i = 0; i < 100; i++) {
            map.put(new BadKey(i), i);
        }

        assertEquals(100, map.size());

        for (int i = 0; i < 100; i++) {
            assertEquals(i, map.get(new BadKey(i)));
        }
    }

    @Test
    void resizeOnLargeInput() {
        MyHashMap<Integer, Integer> map = new MyHashMap<>();
        int initialCapacity = map.capacity();
        int n = 10_000;
        for (int i = 0; i < n; i++) {
            map.put(i, i * 2);
        }

        assertEquals(n, map.size());
        assertTrue(map.capacity() > initialCapacity, "должен был произойти ресайз");

        for (int i = 0; i < n; i++) {
            assertEquals(i * 2, map.get(i));
        }
    }

    @Test
    void removeAllAfterResize() {
        MyHashMap<Integer, String> map = new MyHashMap<>();
        for (int i = 0; i < 1000; i++) {
            map.put(i, "v" + i);
        }
        for (int i = 0; i < 1000; i += 2) {
            assertEquals("v" + i, map.remove(i));
        }

        assertEquals(500, map.size());

        for (int i = 0; i < 1000; i++) {
            if (i % 2 == 0) {
                assertNull(map.get(i));
            }
            else assertEquals("v" + i, map.get(i));
        }
    }

    @Test
    void putGetManyKeys() {
        MyHashMap<Integer, Integer> map = new MyHashMap<>();
        for (int i = 0; i < 100_000; i++) {
            map.put(i, i);
        }
        assertEquals(100_000, map.size());

        for (int i = 0; i < 100_000; i++) {
            assertEquals(i, map.get(i));
        }
        for (int i = 0; i < 100_000; i++) {
            assertEquals(i, map.remove(i));
        }
        assertTrue(map.isEmpty());
    }
}
