package Model;

import Observer.Observable;
import Payment_Strategy.PaymentProcessor;
import Payment_Strategy.PaymentMethod;

public class Order extends Observable {
    private double totalAmount;
    private LoyaltyProgram loyaltyProgram;
    private Customer customer;
    private String status;

    public Order(double totalAmount, LoyaltyProgram loyaltyProgram, Customer customer) {
        this.totalAmount = totalAmount;
        this.loyaltyProgram = loyaltyProgram;
        this.customer = customer;
        this.status = "Pending";
    }

    //  process payment and update loyalty points
    public void applyPayment(PaymentMethod paymentMethod) {
        PaymentProcessor paymentProcessor = new PaymentProcessor(paymentMethod);
        paymentProcessor.process(totalAmount);

        // add loyalty points based on the total amount
        loyaltyProgram.addPoints(totalAmount);

        // order status updated to "Paid" and notify observers
        setStatus("Paid");
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getStatus() {
        return status;
    }

    //  update the status of the order
    public void setStatus(String status) {
        this.status = status;

        // Notify all observers about the status changes
        notifyObservers("Order status updated to: " + status);
    }

}
