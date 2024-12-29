package Main;

import ChainOfResponsibility.CheeseHandler;
import ChainOfResponsibility.CustomizationHandler;
import ChainOfResponsibility.ToppingHandler;
import Command.FeedbackCommand;
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
        Promotion christmasPromotion = new Promotion(Month.DECEMBER, Month.DECEMBER, 10.0, "Christmas Discount");

        // Add promotions to the manager
        promotionManager.addPromotion(christmasPromotion);

        while (true) {
            System.out.println("\n=== Pizza Ordering System ===");
            System.out.println("1. Register New User");
            System.out.println("2. View All Registered Users");
            System.out.println("3. Create Pizza");
            System.out.println("4. Order Pizza");
            System.out.println("5. View All Created Pizzas");
            System.out.println("6. View Favorite Pizza List");
            System.out.println("7. View Available Promotions");
            System.out.println("8. Exit");
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
                case 8 -> {
                    System.out.println("Thank you for using Pizza Ordering System. Goodbye!");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Please choose again.");
            }
        }
    }


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

        // Display available pizzas
        System.out.println("Available Pizzas:");
        for (int i = 0; i < pizzas.size(); i++) {
            System.out.println((i + 1) + ". " + pizzas.get(i).getDescription());
        }
        System.out.print("Choose a pizza to order (1-" + pizzas.size() + "): ");
        int pizzaChoice = scanner.nextInt();
        scanner.nextLine();

        Pizza selectedPizza = pizzas.get(pizzaChoice - 1);

        // Ask for quantity
        System.out.print("Enter the quantity of pizzas you want to order: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        // Calculate the total cost based on quantity
        double pizzaCost = selectedPizza.getCost();
        double totalCost = pizzaCost * quantity;

        // Display the original total cost
        System.out.println("Original cost for " + quantity + " pizza(s): Rs." + totalCost + " /=");

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
            System.out.print("Enter promotion name (Eg: Christmas Discount) : ");
            String promoName = scanner.nextLine();
            Promotion promotion = promotionManager.getPromotion(promoName);
            if (promotion != null) {
                promotionDiscount = totalCost * (promotion.getDiscount() / 100);
                System.out.println("Applied promotion: " + promoName + " with Rs." + promotionDiscount + " discount.");
            } else {
                System.out.println("Invalid promotion!");
            }
        }

        // Calculate final cost
        double finalCost = totalCost - promotionDiscount - discountFromPoints;
        System.out.println("Total cost after discounts: Rs." + finalCost + " /=");

        // Delivery or pickup
        System.out.print("Pick up or Delivery? (Enter 'pickup' or 'delivery'): ");
        String deliveryChoice = scanner.nextLine();
        if ("delivery".equalsIgnoreCase(deliveryChoice)) {
            System.out.print("Enter delivery address: ");
            String address = scanner.nextLine();
            System.out.println("Your pizza will be delivered to: " + address);
        } else {
            System.out.println("You will pick up your pizza at the shop.");
        }

        // Payment method selection
        System.out.print("Choose payment method (1 for Credit Card, 2 for Digital Wallet): ");
        int paymentChoice = scanner.nextInt();
        PaymentMethod paymentMethod = (paymentChoice == 1) ? new CreditCardPayment() : new DigitalWalletPayment();
        new PaymentProcessor(paymentMethod).process(finalCost);

        // Add loyalty points for this order
        loyaltyProgram.addPoints(finalCost);
        System.out.println("Earned loyalty points: " + (int) (finalCost / 100) + " (1 point per Rs. 100 spent).");
        System.out.println("Your new loyalty points balance: " + loyaltyProgram.getPoints());

        // Customization options
        CustomizationHandler toppingHandler = new ToppingHandler();
        toppingHandler.setNextHandler(new CheeseHandler());
        scanner.nextLine(); // Consume leftover newline
        System.out.print("Add extra customization (e.g., 'extra topping' or 'extra cheese'): ");
        toppingHandler.handleRequest(scanner.nextLine());

        // Order context and status updates
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

        // Feedback
        System.out.print("Provide feedback: ");
        String feedback = scanner.nextLine();

        int rating;
        do {
            System.out.print("Rate your experience (1-5) (1 = Worst, 5 = Best): ");
            rating = scanner.nextInt();
            if (rating < 1 || rating > 5) {
                System.out.println("Invalid rating. Please enter a number between 1 and 5.");
            }
        } while (rating < 1 || rating > 5);

        FeedbackCommand feedbackCommand = new FeedbackCommand(feedback, rating);
        feedbackCommand.execute();

        order.setStatus("Your pizza is being prepared!");
        order.setStatus("Out for delivery!");
        order.setStatus("Delivered!");
    }




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
        promotionManager.getPromotions().forEach(promotion -> System.out.println(promotion.getName() + ": " + " 10 % off"));
    }
}

