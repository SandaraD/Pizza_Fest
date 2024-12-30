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


    public Customer(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.loyaltyProgram = new LoyaltyProgram();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void addFavoritePizza(Pizza pizza) {
        favoritePizzas.add(pizza);
    }

    //list of favorite pizzas
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