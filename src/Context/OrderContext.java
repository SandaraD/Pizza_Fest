package Context;

public class OrderContext {
    private OrderState state;

    public void setState(OrderState state) {
        this.state = state;
    }

    public void request() {
        if (state != null) {
            state.handle();
        }
    }
}
