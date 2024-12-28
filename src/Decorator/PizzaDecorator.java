package Decorator;

import Model.Pizza;

public abstract class PizzaDecorator extends Pizza {
    protected Pizza pizza; // Hold a reference to the original pizza

    public PizzaDecorator(Pizza pizza) {
        this.pizza = pizza;
    }

    // The method that adds additional description or behavior
    @Override
    public String getDescription() {
        return pizza.getDescription(); // Delegate to the original pizza's description
    }

    @Override
    public double getCost() {
        return pizza.getCost(); // Delegate to the original pizza's cost
    }
}
