package se.dns;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by diego on 9/9/17.
 */
public class CacheInvalidator implements CacheInvalidationInterface {

    private static CacheInvalidator instance = new CacheInvalidator();
    Set<CacheInvalidationInterface> caches = new HashSet<>();

    public synchronized void register(CacheInvalidationInterface cache) {
        caches.add(cache);
    }

    @Override
    public synchronized void invalidateCache() {
        caches.forEach(CacheInvalidationInterface::invalidateCache);
        //System.out.println("Thread[" + Thread.currentThread().getName() + "] Invalidating caches for [" + caches.size() + "] nodes");
    }

    public void unRegister(Node node) {
        caches.remove(node);
    }

    public static CacheInvalidator getInstance() {
        return instance;
    }
}
