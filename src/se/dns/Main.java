package se.dns;

import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Node root = Node.createRootNode();

        root.addChild(new Node(2));
        root.addChild(new Node(3));
        root.addChild(new Node(4));

        System.out.println("Thread[" + Thread.currentThread().getName() + "] Root new value = " + root.getValue());

        root.getChildren().stream().forEach(child -> child.addChild(new Node(100)));

        System.out.println("Thread[" + Thread.currentThread().getName() + "] Root new value = " + root.getValue());

        root.addChild(new Node(5));

        System.out.println("Thread[" + Thread.currentThread().getName() + "] Root new value = " + root.getValue());

        root.getChildren().toArray(new Node[root.getChildren().size()])[0].addChild(new Node(300));

        System.out.println("Thread[" + Thread.currentThread().getName() + "] Root new value = " + root.getValue());

        Runnable r = () -> {
            while (true) {
                try {
                    root.getChildren().forEach(child -> child.addChild(new Node((int) System.currentTimeMillis())));
                    Thread.sleep(1000);
                    System.out.println("Thread[" + Thread.currentThread().getName() + "] Root new value = " + root.getValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.interrupted();
                }
            }
        };

        Thread t1 = new Thread(r, "Thread1");
        Thread t2 = new Thread(r, "Thread2");
        Thread t3 = new Thread(() -> {

            while(true) {
                root.removeAllChildren();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread3");

        t1.start();
        t2.start();
        //t3.start();

        t1.join();
        t2.join();
        //t3.join();
    }
}
