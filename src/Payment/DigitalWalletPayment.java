package Payment;

public class DigitalWalletPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using Digital Wallet.");
    }
}
