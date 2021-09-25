import org.junit.Test;
import static org.junit.Assert.*;

public class LRUCacheTest {
    @Test
    public void simple_test() {
        LRUCache<Integer, String> cache = new LRUCache<>();
        cache.addValue(1, "1");
        assertTrue(cache.getValue(1).isPresent());
        assertTrue(cache.getValue(2).isEmpty());
        assertEquals("1", cache.getValue(1).get());
        cache.addValue(2, "2");
        assertEquals("2", cache.getValue(2).get());
        cache.addValue(1, "3");
        assertEquals("3", cache.getValue(1).get());
    }

    @Test
    public void one_return_test() {
        LRUCache<Integer, String> cache = new LRUCache<>();
        cache.addValue(1, "1");
        for (int i = 0; i < LRUCache.MAX_SIZE * 2; ++i) {
            assertTrue(cache.getValue(1).isPresent());
            assertEquals("1", cache.getValue(1).get());
            cache.addValue(i + 2, "2");
        }
    }

    @Test
    public void large_test() {
        LRUCache<Integer, String> cache = new LRUCache<>();
        for (int i = 0; i < LRUCache.MAX_SIZE; ++i) {
            cache.addValue(i, String.valueOf(i));
        }
        for (int i = LRUCache.MAX_SIZE; i < LRUCache.MAX_SIZE * 2; ++i) {
            cache.addValue(i, String.valueOf(i));
            for(int j = i - LRUCache.MAX_SIZE + 1; j <= i; ++j) {
                assertTrue(cache.getValue(j).isPresent());
                assertEquals(cache.getValue(j).get(), String.valueOf(j));
            }
            for(int j = 0; j < i - LRUCache.MAX_SIZE - 1; ++j) {
                assertTrue(cache.getValue(j).isEmpty());
            }
        }
    }
}