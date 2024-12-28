package Model;

import Observer.Observer;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Observer {
    private String name;
    private String email;
    private String password;
    private List<Pizza> favoritePizzas = new ArrayList<>();
    private LoyaltyProgram loyaltyProgram;

    // Constructor for name-only (used in some cases)
    public Customer(String name) {
        this.name = name;
    }

    // Constructor for name, email, and password
    public Customer(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.loyaltyProgram = new LoyaltyProgram();
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Add a pizza to the favorite list
    public void addFavoritePizza(Pizza pizza) {
        favoritePizzas.add(pizza);
    }

    // Retrieve the list of favorite pizzas
    public List<Pizza> getFavoritePizzas() {
        return favoritePizzas;
    }

    public LoyaltyProgram getLoyaltyProgram() {
        return loyaltyProgram;
    }

    // Observer pattern: Receive status updates
    @Override
    public void update(String status) {
        System.out.println(name + " received status update: " + status);
    }
}


//package Model;
//
//import Observer.Observer;
//
//public class Customer implements Observer {
//    private String name;
//
//    public Customer(String name) {
//        this.name = name;
//    }
//
//    public Customer(String name, String email, String password) {
//    }
//
//    @Override
//    public void update(String status) {
//        System.out.println(name + " received status update: " + status);
//    }
//}
