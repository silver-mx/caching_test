package se.dns;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by diego on 9/9/17.
 */
public class Node implements CacheInvalidationInterface {
    Node parent;
    private Set<Node> children = new HashSet<>();
    private Integer value;
    private Map<Object, Integer> cache = new HashMap<>();

    public Node(int value) {
        this.value = value;
    }

    public static Node createRootNode() {
        Node root = new Node(0);
        SynchronizedCalculator.getInstance().nodeAdded(root);
        return root;
    }

    public boolean isRootNode() {
        return parent == null;
    }

    public void addChild(Node child) {
        child.parent = this;
        SynchronizedCalculator.getInstance().nodeAdded(child);
    }

    void addChildSync(Node child) {
        children.add(child);
    }

    public void removeAllChildren() {
        Iterator<Node> it = children.iterator();

        while (it.hasNext()) {
            Node child = it.next();
            SynchronizedCalculator.getInstance().nodeRemoved(child);
            child.removeAllChildren();
        }

    }

    void removeChildSync(Node child) {
        children.remove(child);      //MUST BE DONE VIA ITERATOR
    }

    public int getValue() {
        return SynchronizedCalculator.getInstance().calculateValue(this);
    }

    int getOrComputeValue() {

        Integer value = cache.get(this);
        if (value == null) {
            value = computeValue();
            cache.put(this, value);

            //System.out.println("Thread[" + Thread.currentThread().getName() + "] Adding to cache key[" + this + "] value[" + value + "]");
        }

        return value;
    }

    public void invalidateCache() {
        cache.clear();
        //System.out.println("Thread[" + Thread.currentThread().getName() + "] Invalidating cache");
    }

    private int computeValue() {
        return value + getChildren().stream().collect(Collectors.summingInt(Node::getOrComputeValue));
    }

    public Set<Node> getChildren() {
        return children;
    }

}
