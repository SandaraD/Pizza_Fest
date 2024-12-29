package Decorator;

import Model.Pizza;

public class ExtraToppingDecorator extends PizzaDecorator {

    public ExtraToppingDecorator(Pizza pizza) {

        super(pizza); // Pass the pizza object to the parent class
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Extra Topping";
    }

    @Override
    public double getCost() {
        return super.getCost() + 200; //Extra topping cost
    }
}
