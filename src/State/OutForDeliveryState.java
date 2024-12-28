package State;

public class OutForDeliveryState implements OrderState {
    @Override
    public void handle() {
        System.out.println("Order is out for delivery.");
    }
}
