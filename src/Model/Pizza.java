
package Model;

import java.util.ArrayList;
import java.util.List;

public class Pizza {
    private String name;
    private String size;
    private String crust;
    private String sauce;
    private List<String> toppings = new ArrayList<>();
    private String cheese;
    private String cost;

    // PizzaBuilder class inside Pizza
    public static class PizzaBuilder {
        private String name;
        private String size;
        private String crust;
        private String sauce;
        private List<String> toppings = new ArrayList<>();
        private String cheese;
        private String cost;

        public PizzaBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public PizzaBuilder setSize(String size) {
            this.size = size;
            return this;
        }

        public PizzaBuilder setCrust(String crust) {
            this.crust = crust;
            return this;
        }

        public PizzaBuilder setSauce(String sauce) {
            this.sauce = sauce;
            return this;
        }

        public PizzaBuilder addTopping(String topping) {
            this.toppings.add(topping);
            return this;
        }

        public PizzaBuilder setCheese(String cheese) {
            this.cheese = cheese;
            return this;
        }

        public PizzaBuilder setPrice(String cost) {
            this.cost = cost;
            return this;
        }

        public Pizza build() {
            Pizza pizza = new Pizza();
            pizza.name = this.name;
            pizza.size = this.size;
            pizza.crust = this.crust;
            pizza.sauce = this.sauce;
            pizza.toppings = this.toppings;
            pizza.cheese = this.cheese;
            pizza.cost = this.cost;
            return pizza;
        }
    }

    // Method to add a topping (for extra toppings)
    public void addTopping(String topping) {
        if (this.toppings == null) {
            this.toppings = new ArrayList<>();
        }
        this.toppings.add(topping);
    }

    public String getDescription() {
        return name + " " + size + " " + crust + " " + sauce + " pizza with " + String.join(", ", toppings) + " and " + cheese + " cheese.";
    }

    public double getCost() {
        // Example cost calculation: base cost + topping cost
        double cost = 300; // Base cost
        if (size.equals("Large")) {
            cost += 100; // Additional cost for large size
        } else if (size.equals("Medium")) {
            cost += 50;  // Additional cost for medium size
        }
        cost += toppings.size() * 150; // Example: each topping adds 150
        return cost;
    }

        public double applyPromotion(double discountPercentage) {
        double discountAmount = this.getCost() * (discountPercentage / 100);
        return this.getCost() - discountAmount;
    }
}

