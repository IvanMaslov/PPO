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
    private final Node lastNode = new Node(null, null);
    private Node firstNode = lastNode;
    public static final int MAX_SIZE = 10;

    private void insertNode(@NonNull Node node) {
        assert storage.size() <= MAX_SIZE
                : "Can not insert node according container size";
        linkNodes(firstNode, node);
        firstNode = node;
    }

    private void removeNode(@NonNull Node node) {
        final Node lhs = node.prev;
        final Node rhs = node.next;
        assert lhs != null && node != lastNode : "Can not remove initial node";
        if (firstNode == node) {
            firstNode = firstNode.prev;
            return;
        }
        linkNodes(lhs, rhs);
    }

    public void addValue(@NotNull K key, @NotNull V value) {
        if (storage.size() >= MAX_SIZE) {
            assert lastNode.next != null : "Node list can not be empty";
            assert lastNode.next.key != null : "Node can not have null key";
            storage.remove(lastNode.next.key);
            removeNode(lastNode.next);
        }
        Node node = new Node(key, value);
        storage.put(key, node);
        insertNode(node);
        assert node.prev != null : "Node had not been inserted";
        assert node == firstNode : "Node had not been updated after insert";
    }

    public Optional<V> getValue(@NotNull K key) {
        if (storage.containsKey(key)) {
            Node node = storage.get(key);
            assert node != null : "Storage can not contain nullable value";
            assert node != lastNode : "Storage can not contain initial node";
            removeNode(node);
            insertNode(node);
            assert node == firstNode : "Node had not been updated after read";
            return Optional.of(node.value);
        }
        return Optional.empty();
    }

}
