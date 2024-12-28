package Decorator;

import Model.Pizza;

public class ExtraToppingDecorator extends PizzaDecorator {

    public ExtraToppingDecorator(Pizza pizza) {

        super(pizza); // Pass the pizza object to the parent class
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Extra Topping"; // Add extra topping description
    }

    @Override
    public double getCost() {
        return super.getCost() + 200; // Add the cost for extra topping (example)
    }
}
