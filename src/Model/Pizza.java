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

    // PizzaBuilder class inside Pizza
    public static class PizzaBuilder {
        private String name;
        private String size;
        private String crust;
        private String sauce;
        private List<String> toppings = new ArrayList<>();
        private String cheese;

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

        public Pizza build() {
            Pizza pizza = new Pizza();
            pizza.name = this.name;
            pizza.size = this.size;
            pizza.crust = this.crust;
            pizza.sauce = this.sauce;
            pizza.toppings = this.toppings;
            pizza.cheese = this.cheese;
            return pizza;
        }
    }

    public String getDescription() {
        return name+ size + " " + crust + sauce + " pizza with " + String.join(", ", toppings + cheese);
    }

    public double getCost() {
        // Example cost calculation: base cost + topping cost
        double cost = 10.00; // Base cost
        cost += toppings.size() * 1.50; // Example: each topping adds 1.50
        return cost;
    }
}
