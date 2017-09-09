package se.dns;

/**
 * Created by diego on 9/9/17.
 */
public class SynchronizedCalculator implements TransactionTreeListener {

    private static SynchronizedCalculator instance = new SynchronizedCalculator();
    private static CacheInvalidator cacheInvalidator = new CacheInvalidator();

    public synchronized int calculateValue(Node node) {
        int value = node.getOrComputeValue();
        System.out.println("Thread[" + Thread.currentThread().getName() + "] Calculate value[" + value + "]");
        return value;

    }

    public static SynchronizedCalculator getInstance() {
        return instance;
    }

    @Override
    public synchronized void nodeAdded(Node node) {
        //System.out.println("Thread[" + Thread.currentThread().getName() + "] Adding new child[" + node + "]");
        if (!node.isRootNode()) {
            node.parent.addChildSync(node);
        }
        cacheInvalidator.register(node);
        cacheInvalidator.invalidateCache();
    }

    @Override
    public synchronized void nodeRemoved(Node node) {
        //System.out.println("Thread[" + Thread.currentThread().getName() + "] Removing child[" + node + "]");
        node.parent.removeChildSync(node);
        cacheInvalidator.unRegister(node);
        cacheInvalidator.invalidateCache();
    }
}
