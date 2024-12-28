package Model;

import Observer.Observer;

public class Customer implements Observer {
    private String name;

    public Customer(String name) {
        this.name = name;
    }

    public Customer(String name, String email, String password) {
    }

    @Override
    public void update(String status) {
        System.out.println(name + " received status update: " + status);
    }
}
