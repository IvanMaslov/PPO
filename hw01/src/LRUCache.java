import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Optional;

public class LRUCache<K, V> {

    private class Node {
        final K key;
        final V value;
        Node next;
        Node prev;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private void linkNodes(@NonNull Node lhs, @NonNull Node rhs) {
        lhs.next = rhs;
        rhs.prev = lhs;
    }

    private final HashMap<K, Node> storage = new HashMap<>();
    private final Node fakeNode = new Node(null, null);
    private Node firstNode = fakeNode;
    private Node lastNode = fakeNode;
    public static final int MAX_SIZE = 10;

    private void insertNode(@NonNull Node node) {
        assert (storage.size() <= MAX_SIZE);
        linkNodes(firstNode, node);
        firstNode = node;
    }

    private void removeNode(@NonNull Node node) {
        final Node lhs = node.prev;
        final Node rhs = node.next;
        assert (lhs != null);
        if (firstNode == node) {
            firstNode = firstNode.prev;
            return;
        }
        linkNodes(lhs, rhs);
    }

    public void addValue(@NotNull K key, @NotNull V value) {
        if (storage.size() >= MAX_SIZE) {
            storage.remove(lastNode.next.key);
            removeNode(lastNode.next);
        }
        Node node = new Node(key, value);
        storage.put(key, node);
        insertNode(node);
    }

    public Optional<V> getValue(@NotNull K key) {
        if (storage.containsKey(key)) {
            Node node = storage.get(key);
            removeNode(node);
            insertNode(node);
            return Optional.of(node.value);
        }
        return Optional.empty();
    }

}
