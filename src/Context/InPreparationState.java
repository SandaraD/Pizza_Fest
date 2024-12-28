package Context;

public class InPreparationState implements OrderState {
    @Override
    public void handle() {
        System.out.println("Order is in preparation.");
    }
}
