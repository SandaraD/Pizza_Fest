package Context;

public class DeliveredState implements OrderState {
    @Override
    public void handle() {
        System.out.println("Order has been delivered.");
    }
}
