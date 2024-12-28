package Main;

import ChainOfResponsibility.CheeseHandler;
import ChainOfResponsibility.CustomizationHandler;
import ChainOfResponsibility.ToppingHandler;
import Command.PlaceOrderCommand;
import State.InPreparationState;
import State.OrderContext;
import Decorator.ExtraToppingDecorator;
import Model.Customer;
import Model.LoyaltyProgram;
import Model.Order;
import Model.Pizza;
import Payment.CreditCardPayment;
import Payment.DigitalWalletPayment;
import Payment.PaymentMethod;
import Payment.PaymentProcessor;
import Promotion.Promotion;
import Promotion.PromotionManager;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Customer> customers = new ArrayList<>();
        List<Pizza> pizzas = new ArrayList<>();
        LoyaltyProgram loyaltyProgram = new LoyaltyProgram();

        PromotionManager promotionManager = new PromotionManager();

        // Create promotions
        Promotion christmasPromotion = new Promotion(Month.DECEMBER, Month.DECEMBER, 10.0, "Christmas Discount 10% Off");
        Promotion buyTwoGetDiscount = new Promotion(Promotion.PromotionType.BUY_TWO_GET_DISCOUNT, 25.0, "Buy 2 Get 25% Off");

        // Add promotions to the manager
        promotionManager.addPromotion(christmasPromotion);
        promotionManager.addPromotion(buyTwoGetDiscount);

        while (true) {
            System.out.println("\n=== Pizza Ordering System ===");
            System.out.println("1. Register New User");
            System.out.println("2. View All Registered Users");
            System.out.println("3. Create Pizza");
            System.out.println("4. Order Pizza");
            System.out.println("5. View All Created Pizzas");
            System.out.println("6. View Favorite Pizza List");
            System.out.println("7. View Available Promotions");
            System.out.println("8. View Loyalty Program");
            System.out.println("9. Exit");
            System.out.print("Choose an option (1-8): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> registerUser(scanner, customers);
                case 2 -> viewAllUsers(customers);
                case 3 -> createPizza(scanner, customers, pizzas);
                case 4 -> orderPizza(scanner, customers, pizzas, promotionManager, loyaltyProgram);
                case 5 -> viewAllPizzas(pizzas);
                case 6 -> viewFavoritePizzas(customers);
                case 7 -> viewAvailablePromotions(promotionManager);
                case 8 -> viewLoyaltyPoints(scanner, customers, loyaltyProgram);
                case 9 -> {
                    System.out.println("Thank you for using Pizza Ordering System. Goodbye!");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Please choose again.");
            }
        }
    }

    private static void viewLoyaltyPoints(Scanner scanner, List<Customer> customers, LoyaltyProgram loyaltyProgram) {
        System.out.println("\n=== View Loyalty Points ===");
        System.out.println("1. View points for all customers");
        System.out.println("2. View points for a specific customer");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            System.out.println("Loyalty Points for All Customers:");
            for (Customer customer : customers) {
                System.out.println("Customer: " + customer.getName() + " | Points: " + loyaltyProgram.getPoints());
            }
        } else if (choice == 2) {
            System.out.print("Enter the customer name: ");
            String customerName = scanner.nextLine();
            Customer selectedCustomer = customers.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(customerName))
                    .findFirst()
                    .orElse(null);

            if (selectedCustomer != null) {
                System.out.println("Customer: " + selectedCustomer.getName() + " | Points: " + loyaltyProgram.getPoints());
            } else {
                System.out.println("Customer not found!");
            }
        } else {
            System.out.println("Invalid choice!");
        }
    }


//    private static void viewLoyaltyPoints(LoyaltyProgram loyaltyProgram) {
//        System.out.println("\n=== Your Loyalty Points ===");
//        System.out.println("You have " + loyaltyProgram.getPoints() + " loyalty points.");
//        System.out.println("1 Point = Rs. 0.1 discount.");
//    }


    private static void registerUser(Scanner scanner, List<Customer> customers) {
        System.out.println("\n=== Register New User ===");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Create a password: ");
        String password = scanner.nextLine();

        customers.add(new Customer(name, email, password));
        System.out.println("User registered successfully!");
    }

    private static void viewAllUsers(List<Customer> customers) {
        System.out.println("\n=== Registered Users ===");
        if (customers.isEmpty()) {
            System.out.println("No users found.");
        } else {
            customers.forEach(customer ->
                    System.out.println("Name: " + customer.getName() + ", Email: " + customer.getEmail()));
        }
    }

    private static Customer selectCustomerForOrder(Scanner scanner, List<Customer> customers) {
        System.out.println("\n=== Select User for Order ===");
        System.out.println("Please select a customer to place the order:");
        for (int i = 0; i < customers.size(); i++) {
            System.out.println((i + 1) + ". " + customers.get(i).getName());
        }
        System.out.print("Enter the number corresponding to the customer: ");
        int customerChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (customerChoice < 1 || customerChoice > customers.size()) {
            System.out.println("Invalid choice, please try again.");
            return selectCustomerForOrder(scanner, customers); // Recursive call if invalid input
        }

        return customers.get(customerChoice - 1); // Return the selected customer
    }


    private static void createPizza(Scanner scanner, List<Customer> customers, List<Pizza> pizzas) {
        System.out.println("\n=== Create a Pizza ===");

        System.out.print("Enter a custom pizza name: ");
        String pizzaName = scanner.nextLine();

        System.out.print("Choose your size (Small, Medium, Large): ");
        String size = scanner.nextLine();

        System.out.print("Choose your crust (Thin, Thick): ");
        String crust = scanner.nextLine();

        System.out.print("Choose your sauce (Tomato, Alfredo): ");
        String sauce = scanner.nextLine();

        Pizza.PizzaBuilder pizzaBuilder = new Pizza.PizzaBuilder()
                .setSize(size)
                .setCrust(crust)
                .setSauce(sauce)
                .setName(pizzaName);

        while (true) {
            System.out.print("Add a topping (or type 'done'): ");
            String topping = scanner.nextLine();
            if ("done".equalsIgnoreCase(topping)) break;
            pizzaBuilder.addTopping(topping);
        }

        System.out.print("Choose your cheese (Mozzarella, Cheddar): ");
        String cheese = scanner.nextLine();
        pizzaBuilder.setCheese(cheese);

        Pizza pizza = pizzaBuilder.build();
        pizzas.add(pizza);
        System.out.println("Your pizza: " + pizza.getDescription());
        // Display pizza cost
        System.out.println("Total cost: Rs." + pizza.getCost() + " /=");

        System.out.print("Add this pizza to your favorites? (yes/no): ");
        String addToFavorites = scanner.nextLine();
        if ("yes".equalsIgnoreCase(addToFavorites)) {
            if (customers.isEmpty()) {
                System.out.println("No customers available. Register first!");
                return;
            }

            System.out.println("Select a customer to add the pizza to favorites:");
            for (int i = 0; i < customers.size(); i++) {
                System.out.println((i + 1) + ". " + customers.get(i).getName());
            }
            int customerChoice = scanner.nextInt();
            scanner.nextLine();

            customers.get(customerChoice - 1).addFavoritePizza(pizza);
            System.out.println("Pizza added to favorites!");
        }
    }

    private static void orderPizza(Scanner scanner, List<Customer> customers, List<Pizza> pizzas, PromotionManager promotionManager, LoyaltyProgram loyaltyProgram) {
        System.out.println("\n=== Order a Pizza ===");
        if (pizzas.isEmpty()) {
            System.out.println("No pizzas available to order.");
            return;
        }

        System.out.println("Available Pizzas:");
        for (int i = 0; i < pizzas.size(); i++) {
            System.out.println((i + 1) + ". " + pizzas.get(i).getDescription());
        }
        System.out.print("Choose a pizza to order (1-" + pizzas.size() + "): ");
        int pizzaChoice = scanner.nextInt();
        scanner.nextLine();

        Pizza selectedPizza = pizzas.get(pizzaChoice - 1);

        // Display original cost
        System.out.println("Original cost: Rs." + selectedPizza.getCost() + " /=");

        // Show loyalty points
        System.out.println("You have " + loyaltyProgram.getPoints() + " loyalty points.");
        System.out.print("Do you want to redeem points for a discount? (yes/no): ");
        String redeemChoice = scanner.nextLine();
        double discountFromPoints = 0;

        if ("yes".equalsIgnoreCase(redeemChoice)) {
            System.out.print("Enter the number of points to redeem: ");
            int pointsToRedeem = scanner.nextInt();
            scanner.nextLine();
            if (pointsToRedeem <= loyaltyProgram.getPoints()) {
                discountFromPoints = pointsToRedeem * 1.0; // Example: Each point gives Rs. 1 discount
                loyaltyProgram.redeemPoints(pointsToRedeem);
                System.out.println("Redeemed " + pointsToRedeem + " points for a Rs." + discountFromPoints + " discount.");
            } else {
                System.out.println("Not enough points to redeem.");
            }
        }

        // Apply promotions
        System.out.print("Do you want to apply any promotion? (yes/no): ");
        String applyPromotion = scanner.nextLine();
        double promotionDiscount = 0;

        if ("yes".equalsIgnoreCase(applyPromotion)) {
            System.out.println("Available Promotions:");
            promotionManager.getPromotions().forEach(promotion ->
                    System.out.println(promotion.getName() + ": " + promotion.getDiscount() + "% off")
            );
            System.out.print("Enter promotion name: ");
            String promoName = scanner.nextLine();
            Promotion promotion = promotionManager.getPromotion(promoName);
            if (promotion != null) {
                promotionDiscount = selectedPizza.getCost() * (promotion.getDiscount() / 100);
                System.out.println("Applied promotion: " + promoName + " with Rs." + promotionDiscount + " discount.");
            } else {
                System.out.println("Invalid promotion!");
            }
        }

        // Calculate final cost
        double finalCost = selectedPizza.getCost() - promotionDiscount - discountFromPoints;
        System.out.println("Total cost after discounts: Rs." + finalCost + " /=");

        System.out.print("Pick up or Delivery? (Enter 'pickup' or 'delivery'): ");
        String deliveryChoice = scanner.nextLine();
        if ("delivery".equalsIgnoreCase(deliveryChoice)) {
            System.out.print("Enter delivery address: ");
            String address = scanner.nextLine();
            System.out.println("Your pizza will be delivered to: " + address);
        } else {
            System.out.println("You will pick up your pizza at the shop.");
        }

        System.out.print("Choose payment method (1 for Credit Card, 2 for Digital Wallet): ");
        int paymentChoice = scanner.nextInt();
        PaymentMethod paymentMethod = (paymentChoice == 1) ? new CreditCardPayment() : new DigitalWalletPayment();
        new PaymentProcessor(paymentMethod).process(finalCost);

        // Add loyalty points for this order
        loyaltyProgram.addPoints(finalCost);
        System.out.println("Earned loyalty points: " + (int) (finalCost / 100) + " (1 point per Rs. 100 spent).");
        System.out.println("Your new loyalty points balance: " + loyaltyProgram.getPoints());

        CustomizationHandler toppingHandler = new ToppingHandler();
        toppingHandler.setNextHandler(new CheeseHandler());
        scanner.nextLine();
        System.out.print("Add extra customization (e.g., 'extra topping' or 'extra cheese'): ");
        toppingHandler.handleRequest(scanner.nextLine());

        OrderContext orderContext = new OrderContext();
        orderContext.setState(new InPreparationState());
        orderContext.request();

        double totalAmount = finalCost;
        Customer customer = customers.get(0); // Assuming the first customer for simplicity

        Order order = new Order(totalAmount, loyaltyProgram, customer);
        order.addObserver(customer);
        new PlaceOrderCommand(order).execute();

        Pizza enhancedPizza = new ExtraToppingDecorator(selectedPizza);
        System.out.println("Enhanced pizza: " + enhancedPizza.getDescription());

        order.setStatus("Your pizza is being prepared!");
        order.setStatus("Out for delivery!");
        order.setStatus("Delivered!");
    }


//    private static void orderPizza(Scanner scanner, List<Customer> customers, List<Pizza> pizzas, PromotionManager promotionManager, LoyaltyProgram loyaltyProgram) {
//        System.out.println("\n=== Order a Pizza ===");
//        if (pizzas.isEmpty()) {
//            System.out.println("No pizzas available to order.");
//            return;
//        }
//
//        System.out.println("Available Pizzas:");
//        for (int i = 0; i < pizzas.size(); i++) {
//            System.out.println((i + 1) + ". " + pizzas.get(i).getDescription());
//        }
//        System.out.print("Choose a pizza to order (1-" + pizzas.size() + "): ");
//        int pizzaChoice = scanner.nextInt();
//        scanner.nextLine();
//
//        Pizza selectedPizza = pizzas.get(pizzaChoice - 1);
//
//        // Display original cost
//        System.out.println("Original cost: Rs." + selectedPizza.getCost() + " /=");
//
//        // Apply promotions
//        System.out.print("Do you want to apply any promotion? (yes/no): ");
//        String applyPromotion = scanner.nextLine();
//        if ("yes".equalsIgnoreCase(applyPromotion)) {
//            System.out.println("Available Promotions:");
//            promotionManager.getPromotions().forEach(promotion ->
//                    System.out.println(promotion.getName() + ": " + promotion.getDiscount() + "% off")
//            );
//            System.out.print("Enter promotion name: ");
//            String promoName = scanner.nextLine();
//            Promotion promotion = promotionManager.getPromotion(promoName);  // This is where you get a promotion by name
//            if (promotion != null) {
//                double discount = promotion.getDiscount();
//                System.out.println("Applied promotion: " + promoName + " with " + discount + "% off!");
//
//                // Apply promotion and show the new cost
//                double newCost = selectedPizza.applyPromotion(discount);
//                System.out.println("Total cost after promotion: Rs." + newCost + " /=");
//            } else {
//                System.out.println("Invalid promotion!");
//            }
//        }
//
//        System.out.print("Pick up or Delivery? (Enter 'pickup' or 'delivery'): ");
//        String deliveryChoice = scanner.nextLine();
//        if ("delivery".equalsIgnoreCase(deliveryChoice)) {
//            System.out.print("Enter delivery address: ");
//            String address = scanner.nextLine();
//            System.out.println("Your pizza will be delivered to: " + address);
//        } else {
//            System.out.println("You will pick up your pizza at the shop.");
//        }
//
//        System.out.print("Choose payment method (1 for Credit Card, 2 for Digital Wallet): ");
//        int paymentChoice = scanner.nextInt();
//        PaymentMethod paymentMethod = (paymentChoice == 1) ? new CreditCardPayment() : new DigitalWalletPayment();
//        new PaymentProcessor(paymentMethod).process(20.00);
//
//        CustomizationHandler toppingHandler = new ToppingHandler();
//        toppingHandler.setNextHandler(new CheeseHandler());
//        scanner.nextLine();
//        System.out.print("Add extra customization (e.g., 'extra topping' or 'extra cheese'): ");
//        toppingHandler.handleRequest(scanner.nextLine());
//
//        OrderContext orderContext = new OrderContext();
//        orderContext.setState(new InPreparationState());
//        orderContext.request();
//
//        double totalAmount = selectedPizza.getCost(); // Or the final cost after promotion
//        Customer customer = customers.get(0); // Assuming the first customer for simplicity
//
//        Order order = new Order(totalAmount, loyaltyProgram, customer);
//        order.addObserver(customers.get(0));
//        new PlaceOrderCommand(order).execute();
//
//        Pizza enhancedPizza = new ExtraToppingDecorator(selectedPizza);
//        System.out.println("Enhanced pizza: " + enhancedPizza.getDescription());
//
//        order.setStatus("Your pizza is being prepared!");
//        order.setStatus("Out for delivery!");
//        order.setStatus("Delivered!");
//    }



    private static void viewAllPizzas(List<Pizza> pizzas) {
        System.out.println("\n=== All Created Pizzas ===");
        if (pizzas.isEmpty()) {
            System.out.println("No pizzas found.");
        } else {
            pizzas.forEach(pizza -> System.out.println(pizza.getDescription()));
        }
    }

    private static void viewFavoritePizzas(List<Customer> customers) {
        System.out.println("\n=== Favorite Pizzas ===");
        if (customers.isEmpty()) {
            System.out.println("No customers available.");
            return;
        }
        customers.forEach(customer -> {
            System.out.println(customer.getName() + "'s Favorite Pizzas:");
            customer.getFavoritePizzas().forEach(pizza -> System.out.println(pizza.getDescription()));
        });
    }

    private static void viewAvailablePromotions(PromotionManager promotionManager) {
        System.out.println("\n=== Available Promotions ===");
        promotionManager.getPromotions().forEach(promotion -> System.out.println(promotion.getName() + ": " + (promotion.getDiscount() * 100) + "% off"));
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
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        List<Customer> customers = new ArrayList<>(); // List of registered customers
//        List<Pizza> pizzas = new ArrayList<>(); // List of all created pizzas
//
//        while (true) {
//            // Display the menu
//            System.out.println("\n=== Pizza Ordering System ===");
//            System.out.println("1. Register New User");
//            System.out.println("2. View All Registered Users");
//            System.out.println("3. Create Pizza");
//            System.out.println("4. Order Pizza");
//            System.out.println("5. View All Created Pizzas");
//            System.out.println("6. View Favorite Pizza List");
//            System.out.println("7. Exit");
//            System.out.print("Choose an option (1-7): ");
//            int choice = scanner.nextInt();
//            scanner.nextLine(); // Consume leftover newline
//
//            switch (choice) {
//                case 1: // Register New User
//                    registerUser(scanner, customers);
//                    break;
//                case 2: // View All Registered Users
//                    viewAllUsers(customers);
//                    break;
//                case 3: // Create Pizza
//                    createPizza(scanner, customers, pizzas);
//                    break;
//                case 4: // Order Pizza
//                    orderPizza(scanner, customers, pizzas);
//                    break;
//                case 5: // View All Created Pizzas
//                    viewAllPizzas(pizzas);
//                    break;
//                case 6: // View Favorite Pizza List
//                    viewFavoritePizzas(customers);
//                    break;
//                case 7: // Exit
//                    System.out.println("Thank you for using Pizza Ordering System. Goodbye!");
//                    scanner.close();
//                    System.exit(0);
//                    break;
//                default:
//                    System.out.println("Invalid option. Please choose again.");
//            }
//        }
//    }
//
//    // Method to register a new user
//    private static void registerUser(Scanner scanner, List<Customer> customers) {
//        System.out.println("\n=== Register New User ===");
//        System.out.print("Enter your name: ");
//        String name = scanner.nextLine();
//
//        System.out.print("Enter your email: ");
//        String email = scanner.nextLine();
//
//        System.out.print("Create a password: ");
//        String password = scanner.nextLine();
//
//        // Create a new customer and add to the list
//        Customer customer = new Customer(name, email, password);
//        customers.add(customer);
//        System.out.println("User registered successfully!");
//    }
//
//    // Method to view all registered users
//    private static void viewAllUsers(List<Customer> customers) {
//        System.out.println("\n=== Registered Users ===");
//        for (Customer customer : customers) {
//            System.out.println("Name: " + customer.getName() + ", Email: " + customer.getEmail());
//        }
//    }
//
//    // Method to create a pizza and add it to the favorites
//    private static void createPizza(Scanner scanner, List<Customer> customers, List<Pizza> pizzas) {
//        System.out.println("\n=== Create a Pizza ===");
//
//        System.out.print("Enter a custom pizza name: ");
//        String pizzaName = scanner.nextLine();
//
//        System.out.print("Choose your size (e.g., Small, Medium, Large): ");
//        String size = scanner.nextLine();
//
//        System.out.print("Choose your crust (e.g., Thin, Thick): ");
//        String crust = scanner.nextLine();
//
//        System.out.print("Choose your sauce (e.g., Tomato, Alfredo): ");
//        String sauce = scanner.nextLine();
//
//        // Build the pizza using PizzaBuilder
//        Pizza.PizzaBuilder pizzaBuilder = new Pizza.PizzaBuilder()
//                .setSize(size)
//                .setCrust(crust)
//                .setSauce(sauce)
//                .setName(pizzaName);
//
//        while (true) {
//            System.out.print("Add a topping (e.g., Pepperoni, Mushrooms) or type 'done': ");
//            String topping = scanner.nextLine();
//            if (topping.equalsIgnoreCase("done")) break;
//            pizzaBuilder.addTopping(topping);
//        }
//
//        System.out.print("Choose your cheese (e.g., Mozzarella, Cheddar): ");
//        String cheese = scanner.nextLine();
//        pizzaBuilder.setCheese(cheese);
//
//        Pizza pizza = pizzaBuilder.build();
//        pizzas.add(pizza); // Add pizza to the list of all pizzas
//        System.out.println("Your pizza: " + pizza.getDescription());
//
//        // Add to favorites or not
//        System.out.print("Would you like to add this pizza to your favorites? (yes/no): ");
//        String addToFavorites = scanner.nextLine();
//        if (addToFavorites.equalsIgnoreCase("yes")) {
//            // Choose the customer to add the pizza to favorites
//            System.out.println("Select a customer to add the pizza to favorites:");
//            for (int i = 0; i < customers.size(); i++) {
//                System.out.println((i + 1) + ". " + customers.get(i).getName());
//            }
//            int customerChoice = scanner.nextInt();
//            scanner.nextLine(); // Consume leftover newline
//
//            Customer selectedCustomer = customers.get(customerChoice - 1);
//            selectedCustomer.addFavoritePizza(pizza);
//            System.out.println("Pizza added to your favorites!");
//        }
//    }
//
//    // Method to order a pizza
//    private static void orderPizza(Scanner scanner, List<Customer> customers, List<Pizza> pizzas) {
//        System.out.println("\n=== Order a Pizza ===");
//
//        if (pizzas.isEmpty()) {
//            System.out.println("No pizzas available to order.");
//            return;
//        }
//
//        // Display available pizzas
//        System.out.println("Available Pizzas:");
//        for (int i = 0; i < pizzas.size(); i++) {
//            System.out.println((i + 1) + ". " + pizzas.get(i).getDescription());
//        }
//        System.out.print("Choose a pizza to order (1-" + pizzas.size() + "): ");
//        int pizzaChoice = scanner.nextInt();
//        scanner.nextLine(); // Consume leftover newline
//
//        Pizza selectedPizza = pizzas.get(pizzaChoice - 1);
//
//        // Ask for delivery or pickup
//        System.out.print("Pick up or Delivery? (Enter 'pickup' or 'delivery'): ");
//        String deliveryChoice = scanner.nextLine();
//
//        if (deliveryChoice.equalsIgnoreCase("delivery")) {
//            System.out.print("Enter delivery address: ");
//            String address = scanner.nextLine();
//            System.out.println("Your pizza will be delivered to: " + address);
//        } else {
//            System.out.println("You will pick up your pizza at the shop.");
//        }
//
//        // Choose payment method
//        System.out.print("Choose payment method (1 for Credit Card, 2 for Digital Wallet): ");
//        int paymentChoice = scanner.nextInt();
//        PaymentMethod paymentMethod = paymentChoice == 1 ? new CreditCardPayment() : new DigitalWalletPayment();
//        PaymentProcessor paymentProcessor = new PaymentProcessor(paymentMethod);
//        paymentProcessor.process(20.00); // Example price
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
//        // Order context and state transitions
//        OrderContext orderContext = new OrderContext();
//        orderContext.setState(new InPreparationState());
//        orderContext.request();
//
//        // Execute place order command
//        Order order1 = new Order();
//        order1.addObserver(customers.get(0)); // Assuming the first customer is ordering
//        PlaceOrderCommand placeOrder = new PlaceOrderCommand(order1);
//        placeOrder.execute();
//
//        // Decorator: Enhance the pizza
//        Pizza enhancedPizza = new ExtraToppingDecorator(selectedPizza);
//        System.out.println("Enhanced pizza: " + enhancedPizza.getDescription());
//
//        // Feedback command
//        System.out.print("Provide feedback: ");
//        scanner.nextLine(); // Consume leftover newline
//        String feedback = scanner.nextLine();
//        FeedbackCommand feedbackCommand = new FeedbackCommand(feedback);
//        feedbackCommand.execute();
//
//        // Update order status
//        order1.setStatus("Your pizza is being prepared!");
//        order1.setStatus("Out for delivery!");
//        order1.setStatus("Delivered!");
//    }
//
//    // Method to view all created pizzas
//    private static void viewAllPizzas(List<Pizza> pizzas) {
//        System.out.println("\n=== All Created Pizzas ===");
//        for (Pizza pizza : pizzas) {
//            System.out.println(pizza.getDescription());
//        }
//    }
//
//    // Method to view the favorite pizza list of customers
//    private static void viewFavoritePizzas(List<Customer> customers) {
//        System.out.println("\n=== Favorite Pizzas ===");
//        if (customers.isEmpty()) {
//            System.out.println("No customers available.");
//            return;
//        }
//        for (Customer customer : customers) {
//            System.out.println(customer.getName() + "'s Favorite Pizzas:");
//            for (Pizza pizza : customer.getFavoritePizzas()) {
//                System.out.println(pizza.getDescription());
//            }
//        }
//    }
//}


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
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        List<Customer> customers = new ArrayList<>(); // List of registered customers
//        List<Pizza> pizzas = new ArrayList<>(); // List of all created pizzas
//
//        System.out.println("Welcome to the Pizza Fest!");
//
//        // User registration
//        System.out.print("Enter your name: ");
//        String name = scanner.nextLine();
//
//        System.out.print("Enter your email: ");
//        String email = scanner.nextLine();
//
//        System.out.print("Create a password: ");
//        String password = scanner.nextLine();
//
//        // Create a new customer and add to the list
//        Customer customer = new Customer(name, email, password);
//        customers.add(customer);
//        System.out.println("User registered successfully!");
//
//        // Create an order and associate it with the customer
//        Order order1 = new Order();
//        order1.addObserver(customer);
//
//        // Collect pizza details
//        System.out.println("Choose your pizza details!");
//
//        System.out.print("Enter a custom pizza name: ");
//        String pizzaName = scanner.nextLine();
//
//        System.out.print("Choose your size (e.g., Small, Medium, Large): ");
//        String size = scanner.nextLine();
//
//        System.out.print("Choose your crust (e.g., Thin, Thick): ");
//        String crust = scanner.nextLine();
//
//        System.out.print("Choose your sauce (e.g., Tomato, Alfredo): ");
//        String sauce = scanner.nextLine();
//
//        // Build the pizza using PizzaBuilder
//        Pizza.PizzaBuilder pizzaBuilder = new Pizza.PizzaBuilder()
//                .setSize(size)
//                .setCrust(crust)
//                .setSauce(sauce)
//                .setName(pizzaName);
//
//        while (true) {
//            System.out.print("Add a topping (e.g., Pepperoni, Mushrooms) or type 'done': ");
//            String topping = scanner.nextLine();
//            if (topping.equalsIgnoreCase("done")) break;
//            pizzaBuilder.addTopping(topping);
//        }
//
//        System.out.print("Choose your cheese (e.g., Mozzarella, Cheddar): ");
//        String cheese = scanner.nextLine();
//        pizzaBuilder.setCheese(cheese);
//
//        Pizza pizza = pizzaBuilder.build();
//        pizzas.add(pizza); // Add pizza to the list of all pizzas
//        System.out.println("Your pizza: " + pizza.getDescription());
//
//        // Add to favorites or not
//        System.out.print("Would you like to add this pizza to your favorites? (yes/no): ");
//        String addToFavorites = scanner.nextLine();
//        if (addToFavorites.equalsIgnoreCase("yes")) {
//            customer.addFavoritePizza(pizza);
//            System.out.println("Pizza added to your favorites!");
//        }
//
//        // Extra cheese and special packaging
//        System.out.print("Would you like extra cheese? (yes/no): ");
//        String extraCheese = scanner.nextLine();
//
//        System.out.print("Would you like special packaging? (yes/no): ");
//        String specialPackaging = scanner.nextLine();
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
//        paymentProcessor.process(20.00); // Example price
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
//        // Pick up or delivery
//        System.out.print("Pick up or Delivery? (Enter 'pickup' or 'delivery'): ");
//        String deliveryChoice = scanner.nextLine();
//
//        if (deliveryChoice.equalsIgnoreCase("delivery")) {
//            System.out.print("Enter delivery address: ");
//            String address = scanner.nextLine();
//            System.out.println("Your pizza will be delivered to: " + address);
//        } else {
//            System.out.println("You will pick up your pizza at the shop.");
//        }
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
//        scanner.nextLine(); // Consume leftover newline
//        String feedback = scanner.nextLine();
//        FeedbackCommand feedbackCommand = new FeedbackCommand(feedback);
//        feedbackCommand.execute();
//
//        // Update order status
//        order1.setStatus("Your pizza is being prepared!");
//        order1.setStatus("Out for delivery!");
//        order1.setStatus("Delivered!");
//
//        // Display all registered users
//        System.out.println("\nAll Registered Users:");
//        for (Customer registeredCustomer : customers) {
//            System.out.println("Name: " + registeredCustomer.getName() + ", Email: " + registeredCustomer.getEmail());
//        }
//
//        // Display all pizzas
//        System.out.println("\nAll Pizzas Created:");
//        for (Pizza createdPizza : pizzas) {
//            System.out.println(createdPizza.getDescription());
//        }
//
//        // Display favorite pizzas
//        System.out.println("\nYour Favorite Pizzas:");
//        for (Pizza favoritePizza : customer.getFavoritePizzas()) {
//            System.out.println(favoritePizza.getDescription());
//        }
//
//        scanner.close();
//    }
//}


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
//        // User registration
//        System.out.println("Welcome to the Pizza Fest!");
//        System.out.print("Enter your name: ");
//        String name = scanner.nextLine();
//
//        System.out.print("Enter your email: ");
//        String email = scanner.nextLine();
//
//        System.out.print("Create a password: ");
//        String password = scanner.nextLine();
//
//        // Create a new customer
//        Customer customer = new Customer(name, email, password);
//        System.out.println("User registered successfully!");
//
//        // Create an order and associate it with the customer
//        Order order1 = new Order();
//        order1.addObserver(customer);
//
//        // Collect pizza details
//        System.out.println("Choose your pizza details!");
//
//        System.out.print("Enter a custom pizza name: ");
//        String pizzaName = scanner.nextLine();
//
//        System.out.print("Choose your size (e.g., Small, Medium, Large): ");
//        String size = scanner.nextLine();
//
//        System.out.print("Choose your crust (e.g., Thin, Thick): ");
//        String crust = scanner.nextLine();
//
//        System.out.print("Choose your sauce (e.g., Tomato, Alfredo): ");
//        String sauce = scanner.nextLine();
//
//        // Build the pizza using PizzaBuilder
//        Pizza.PizzaBuilder pizzaBuilder = new Pizza.PizzaBuilder()
//                .setSize(size)
//                .setCrust(crust)
//                .setSauce(sauce)
//                .setName(pizzaName);
//
//        while (true) {
//            System.out.print("Add a topping (e.g., Pepperoni, Mushrooms) or type 'done': ");
//            String topping = scanner.nextLine();
//            if (topping.equalsIgnoreCase("done")) break;
//            pizzaBuilder.addTopping(topping);
//        }
//
//        System.out.print("Choose your cheese (e.g., Mozzarella, Cheddar): ");
//        String cheese = scanner.nextLine();
//        pizzaBuilder.setCheese(cheese);
//
//        Pizza pizza = pizzaBuilder.build();
//        System.out.println("Your pizza: " + pizza.getDescription());
//
//        // Add to favorites or not
//        System.out.print("Would you like to add this pizza to your favorites? (yes/no): ");
//        String addToFavorites = scanner.nextLine();
//        if (addToFavorites.equalsIgnoreCase("yes")) {
//            // Implement adding to favorites (assuming some logic here)
//            System.out.println("Pizza added to your favorites!");
//        }
//
//        // Extra cheese and special packaging
//        System.out.print("Would you like extra cheese? (yes/no): ");
//        String extraCheese = scanner.nextLine();
//
//        System.out.print("Would you like special packaging? (yes/no): ");
//        String specialPackaging = scanner.nextLine();
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
//        paymentProcessor.process(20.00); // Example price
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
//        // Pick up or delivery
//        System.out.print("Pick up or Delivery? (Enter 'pickup' or 'delivery'): ");
//        String deliveryChoice = scanner.nextLine();
//
//        if (deliveryChoice.equalsIgnoreCase("delivery")) {
//            System.out.print("Enter delivery address: ");
//            String address = scanner.nextLine();
//            System.out.println("Your pizza will be delivered to: " + address);
//        } else {
//            System.out.println("You will pick up your pizza at the shop.");
//        }
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
//        scanner.nextLine(); // Consume leftover newline
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
//
//
