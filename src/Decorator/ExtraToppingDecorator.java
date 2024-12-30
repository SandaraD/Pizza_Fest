package Decorator;

import Model.Pizza;

public class ExtraToppingDecorator extends PizzaDecorator {

    public ExtraToppingDecorator(Pizza pizza) {

        super(pizza);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Extra Topping";
    }

    @Override
    public double getCost() {
        return super.getCost() + 200; //+200 for extra topping
    }
}
