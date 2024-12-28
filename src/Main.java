package Main;

import ChainOfResponsibility.CheeseHandler;
import ChainOfResponsibility.CustomizationHandler;
import ChainOfResponsibility.ToppingHandler;
import Command.FeedbackCommand;
import Command.PlaceOrderCommand;
import Context.InPreparationState;
import Context.OrderContext;
import Context.OutForDeliveryState;
import Decorator.ExtraToppingDecorator;
import Model.Customer;
import Model.Order;
import Model.Pizza;
import Payment.CreditCardPayment;
import Payment.DigitalWalletPayment;
import Payment.PaymentMethod;
import Payment.PaymentProcessor;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // User registration
        System.out.println("Welcome to the Pizza Fest!");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Create a password: ");
        String password = scanner.nextLine();

        // Create a new customer
        Customer customer = new Customer(name, email, password);
        System.out.println("User registered successfully!");

        // Create an order and associate it with the customer
        Order order1 = new Order();
        order1.addObserver(customer);

        // Collect pizza details
        System.out.println("Choose your pizza details!");

        System.out.print("Enter a custom pizza name: ");
        String pizzaName = scanner.nextLine();

        System.out.print("Choose your size (e.g., Small, Medium, Large): ");
        String size = scanner.nextLine();

        System.out.print("Choose your crust (e.g., Thin, Thick): ");
        String crust = scanner.nextLine();

        System.out.print("Choose your sauce (e.g., Tomato, Alfredo): ");
        String sauce = scanner.nextLine();

        // Build the pizza using PizzaBuilder
        Pizza.PizzaBuilder pizzaBuilder = new Pizza.PizzaBuilder()
                .setSize(size)
                .setCrust(crust)
                .setSauce(sauce)
                .setName(pizzaName);

        while (true) {
            System.out.print("Add a topping (e.g., Pepperoni, Mushrooms) or type 'done': ");
            String topping = scanner.nextLine();
            if (topping.equalsIgnoreCase("done")) break;
            pizzaBuilder.addTopping(topping);
        }

        System.out.print("Choose your cheese (e.g., Mozzarella, Cheddar): ");
        String cheese = scanner.nextLine();
        pizzaBuilder.setCheese(cheese);

        Pizza pizza = pizzaBuilder.build();
        System.out.println("Your pizza: " + pizza.getDescription());

        // Add to favorites or not
        System.out.print("Would you like to add this pizza to your favorites? (yes/no): ");
        String addToFavorites = scanner.nextLine();
        if (addToFavorites.equalsIgnoreCase("yes")) {
            // Implement adding to favorites (assuming some logic here)
            System.out.println("Pizza added to your favorites!");
        }

        // Extra cheese and special packaging
        System.out.print("Would you like extra cheese? (yes/no): ");
        String extraCheese = scanner.nextLine();

        System.out.print("Would you like special packaging? (yes/no): ");
        String specialPackaging = scanner.nextLine();

        // Order context and state transitions
        OrderContext orderContext = new OrderContext();
        orderContext.setState(new InPreparationState());
        orderContext.request();

        // Choose payment method
        System.out.print("Choose payment method (1 for Credit Card, 2 for Digital Wallet): ");
        int paymentChoice = scanner.nextInt();
        PaymentMethod paymentMethod = paymentChoice == 1 ? new CreditCardPayment() : new DigitalWalletPayment();
        PaymentProcessor paymentProcessor = new PaymentProcessor(paymentMethod);
        paymentProcessor.process(20.00); // Example price

        // Chain of Responsibility: Handle customizations
        CustomizationHandler toppingHandler = new ToppingHandler();
        CustomizationHandler cheeseHandler = new CheeseHandler();
        toppingHandler.setNextHandler(cheeseHandler);

        scanner.nextLine(); // Consume leftover newline
        System.out.print("Add extra customization (e.g., 'extra topping' or 'extra cheese'): ");
        String customization = scanner.nextLine();
        toppingHandler.handleRequest(customization);

        // Pick up or delivery
        System.out.print("Pick up or Delivery? (Enter 'pickup' or 'delivery'): ");
        String deliveryChoice = scanner.nextLine();

        if (deliveryChoice.equalsIgnoreCase("delivery")) {
            System.out.print("Enter delivery address: ");
            String address = scanner.nextLine();
            System.out.println("Your pizza will be delivered to: " + address);
        } else {
            System.out.println("You will pick up your pizza at the shop.");
        }

        // Update order context state
        orderContext.setState(new OutForDeliveryState());
        orderContext.request();

        // Execute commands
        PlaceOrderCommand placeOrder = new PlaceOrderCommand(order1);
        placeOrder.execute();

        // Decorator: Enhance the pizza
        Pizza enhancedPizza = new ExtraToppingDecorator(pizza);
        System.out.println("Enhanced pizza: " + enhancedPizza.getDescription());

        // Feedback command
        System.out.print("Provide feedback: ");
        scanner.nextLine(); // Consume leftover newline
        String feedback = scanner.nextLine();
        FeedbackCommand feedbackCommand = new FeedbackCommand(feedback);
        feedbackCommand.execute();

        // Update order status
        order1.setStatus("Your pizza is being prepared!");
        order1.setStatus("Out for delivery!");
        order1.setStatus("Delivered!");

        scanner.close();
    }
}



//package Main;
//
//import ChainOfResponsibility.CheeseHandler;
//import ChainOfResponsibility.CustomizationHandler;
//import ChainOfResponsibility.ToppingHandler;
//import Command.FeedbackCommand;
//import Command.PlaceOrderCommand;
//import Context.InPreparationState;
//import Context.OrderContext;
//import Context.OutForDeliveryState;
//import Decorator.ExtraToppingDecorator;
//import Model.Customer;
//import Model.Order;
//import Model.Pizza;
//import Payment.CreditCardPayment;
//import Payment.DigitalWalletPayment;
//import Payment.PaymentMethod;
//import Payment.PaymentProcessor;
//
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//
//        // Create customers and order
//        Customer customer1 = new Customer("Alice");
//        Customer customer2 = new Customer("Bob");
//        Order order1 = new Order();
//        order1.addObserver(customer1);
//        order1.addObserver(customer2);
//
//        // Welcome and collect pizza details
//        System.out.println("Welcome to the Pizza Shop!");
//        System.out.print("Choose your size (e.g., Small, Medium, Large): ");
//        String size = scanner.nextLine();
//        System.out.print("Choose your crust (e.g., Thin, Thick): ");
//        String crust = scanner.nextLine();
//
//        // Build the pizza using PizzaBuilder
//        Pizza.PizzaBuilder pizzaBuilder = new Pizza.PizzaBuilder()
//                .setSize(size)
//                .setCrust(crust);
//
//        while (true) {
//            System.out.print("Add a topping (e.g., Pepperoni, Mushrooms) or type 'done': ");
//            String topping = scanner.nextLine();
//            if (topping.equalsIgnoreCase("done")) break;
//            pizzaBuilder.addTopping(topping);
//        }
//
//        Pizza pizza = pizzaBuilder.build();
//        System.out.println("Your pizza: " + pizza.getDescription());
//
//        // Order context and state transitions
//        OrderContext orderContext = new OrderContext();
//        orderContext.setState(new InPreparationState());
//        orderContext.request();
//
//        // Choose payment method
//        System.out.print("Choose payment method (1 for Credit Card, 2 for Digital Wallet): ");
//        int paymentChoice = scanner.nextInt();
//        PaymentMethod paymentMethod = paymentChoice == 1 ? new CreditCardPayment() : new DigitalWalletPayment();
//        PaymentProcessor paymentProcessor = new PaymentProcessor(paymentMethod);
//        paymentProcessor.process(19.99); // Example price
//
//        // Chain of Responsibility: Handle customizations
//        CustomizationHandler toppingHandler = new ToppingHandler();
//        CustomizationHandler cheeseHandler = new CheeseHandler();
//        toppingHandler.setNextHandler(cheeseHandler);
//
//        scanner.nextLine(); // Consume leftover newline
//        System.out.print("Add extra customization (e.g., 'extra topping' or 'extra cheese'): ");
//        String customization = scanner.nextLine();
//        toppingHandler.handleRequest(customization);
//
//        // Update order context state
//        orderContext.setState(new OutForDeliveryState());
//        orderContext.request();
//
//        // Execute commands
//        PlaceOrderCommand placeOrder = new PlaceOrderCommand(order1);
//        placeOrder.execute();
//
//        // Decorator: Enhance the pizza
//        Pizza enhancedPizza = new ExtraToppingDecorator(pizza);
//        System.out.println("Enhanced pizza: " + enhancedPizza.getDescription());
//
//        // Feedback command
//        System.out.print("Provide feedback: ");
//        String feedback = scanner.nextLine();
//        FeedbackCommand feedbackCommand = new FeedbackCommand(feedback);
//        feedbackCommand.execute();
//
//        // Update order status
//        order1.setStatus("Your pizza is being prepared!");
//        order1.setStatus("Out for delivery!");
//        order1.setStatus("Delivered!");
//
//        scanner.close();
//    }
//}
