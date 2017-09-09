package se.dns;

/**
 * Created by diego on 9/9/17.
 */
public interface TransactionTreeListener {

    void nodeAdded(Node node);
    void nodeRemoved(Node node);
}
