package Model;

import Observer.Observable;

public class Order extends Observable {
    private String status;

    public void setStatus(String status) {
        this.status = status;
        notifyObservers(status);
    }
}
