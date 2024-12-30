package Payment_Strategy;

public class DigitalWalletPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println("Paid Rs." + amount + " using Digital Wallet.");
    }
}
