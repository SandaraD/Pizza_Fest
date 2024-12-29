package Model;

import Observer.Observable;
import Payment.PaymentProcessor;
import Payment.PaymentMethod;

public class Order extends Observable {
    private double totalAmount;
    private LoyaltyProgram loyaltyProgram;
    private Customer customer; // New field to store customer information
    private String status; // Tracks the current status of the order

    // Updated constructor to include Customer
    public Order(double totalAmount, LoyaltyProgram loyaltyProgram, Customer customer) {
        this.totalAmount = totalAmount;
        this.loyaltyProgram = loyaltyProgram;
        this.customer = customer;
        this.status = "Pending"; // Default status when the order is created
    }

    // Method to process payment and update loyalty points
    public void applyPayment(PaymentMethod paymentMethod) {
        PaymentProcessor paymentProcessor = new PaymentProcessor(paymentMethod);
        paymentProcessor.process(totalAmount);

        // Add loyalty points based on total amount
        loyaltyProgram.addPoints(totalAmount);

        // Update order status to "Paid" and notify observers
        setStatus("Paid");
    }

    // Getter for total amount
    public double getTotalAmount() {
        return totalAmount;
    }

    // Getter for customer
    public Customer getCustomer() {
        return customer;
    }

    // Getter for order status
    public String getStatus() {
        return status;
    }

    // Method to update the status of the order
    public void setStatus(String status) {
        this.status = status;

        // Notify all observers (e.g., customers) about the status change
        notifyObservers("Order status updated to: " + status);
    }

    // Method to notify a specific observer (e.g., customer) about the status
    public void notifyCustomer() {
        notifyObservers("Dear " + customer.getName() + ", your order status is now: " + status);
    }
}
