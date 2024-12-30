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
import Payment_Strategy.CreditCardPayment;
import Payment_Strategy.DigitalWalletPayment;
import Payment_Strategy.PaymentMethod;
import Payment_Strategy.PaymentProcessor;
import Promotion_Strategy.Promotion;
import Promotion_Strategy.PromotionManager;

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

        // Create Promotion
        Promotion christmasPromotion = new Promotion(Month.DECEMBER, Month.DECEMBER, 10.0, "Christmas Discount");

        // Add promotions to the PromotionManager
        promotionManager.addPromotion(christmasPromotion);

        while (true) {
            System.out.println("\n=== Pizza Ordering System - PIZZA FEST 🍕===");
            System.out.println("1. Register New Customer");
            System.out.println("2. View All Registered Customers");
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
                    System.out.println("Thank you for using Pizza Ordering System.");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Please choose again ☹︎.");
            }
        }
    }


    private static void registerUser(Scanner scanner, List<Customer> customers) {
        System.out.println("\n=== Register New Customer ☺︎ ===");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

       //email validation
        String email;
        while (true) {
            System.out.print("Enter your email: ");
            email = scanner.nextLine();
            if (isValidEmail(email)) {
                break;
            } else {
                System.out.println("Invalid email format. Please try again. ☹︎");
            }
        }

        System.out.print("Create a password: ");
        String password = scanner.nextLine();

        customers.add(new Customer(name, email, password));
        System.out.println("Customer registered successfully! ☺︎ ");
    }

    // validate email
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }


    private static void viewAllUsers(List<Customer> customers) {
        System.out.println("\n=== Registered Customer ☺︎ ===");
        if (customers.isEmpty()) {
            System.out.println("No Customers found.☹︎ ");
        } else {
            customers.forEach(customer ->
                    System.out.println("Name: " + customer.getName() + ", Email: " + customer.getEmail()));
        }
    }


    private static void createPizza(Scanner scanner, List<Customer> customers, List<Pizza> pizzas) {
        System.out.println("\n=== Create a Pizza ===");

        System.out.print("Enter a custom pizza name: ");
        String pizzaName = scanner.nextLine();

        // Choose size
        String[] sizes = {"Small-4 slices", "Medium-6 slices", "Large-8 slices"};
        System.out.println("Choose your pizza size:");
        for (int i = 0; i < sizes.length; i++) {
            System.out.println((i + 1) + ". " + sizes[i]);
        }
        int sizeChoice = getValidChoice(scanner, sizes.length);
        String size = sizes[sizeChoice - 1];

        // Choose crust
        String[] crusts = {"Thin", "Thick"};
        System.out.println("Choose your pizza crust:");
        for (int i = 0; i < crusts.length; i++) {
            System.out.println((i + 1) + ". " + crusts[i]);
        }
        int crustChoice = getValidChoice(scanner, crusts.length);
        String crust = crusts[crustChoice - 1];

        // Choose sauce
        String[] sauces = {"Tomato", "BBQ (mild spicy)", "Chilli Sauce (very spicy)"};
        System.out.println("Choose your sauce:");
        for (int i = 0; i < sauces.length; i++) {
            System.out.println((i + 1) + ". " + sauces[i]);
        }
        int sauceChoice = getValidChoice(scanner, sauces.length);
        String sauce = sauces[sauceChoice - 1];

        Pizza.PizzaBuilder pizzaBuilder = new Pizza.PizzaBuilder()
                .setSize(size)
                .setCrust(crust)
                .setSauce(sauce)
                .setName(pizzaName);

        // Add toppings
        String[] availableToppings = {"Mushrooms (veg)", "Onions (veg)", "Olives (veg)", "Capsicum (veg)","Bell peppers (veg)",
                "Corn (veg)", "Tomato (veg)", "Chicken", "Ham", "Bacon", "Sausage", "Pepperoni" };
        System.out.println("Available toppings:");
        for (int i = 0; i < availableToppings.length; i++) {
            System.out.println((i + 1) + ". " + availableToppings[i]);
        }
        System.out.println("Type 'done' when you're done adding toppings.");
        while (true) {
            System.out.print("Add a topping: ");
            String toppingInput = scanner.nextLine();
            if ("done".equalsIgnoreCase(toppingInput)) break;

            try {
                int toppingChoice = Integer.parseInt(toppingInput);
                if (toppingChoice > 0 && toppingChoice <= availableToppings.length) {
                    pizzaBuilder.addTopping(availableToppings[toppingChoice - 1]);
                } else {
                    System.out.println("Invalid choice. Please select a valid topping.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number or 'done'.");
            }
        }

        // Choose cheese
        String[] cheeses = {"Mozzarella", "Cheddar", "Parmesan"};
        System.out.println("Choose your cheese:");
        for (int i = 0; i < cheeses.length; i++) {
            System.out.println((i + 1) + ". " + cheeses[i]);
        }
        int cheeseChoice = getValidChoice(scanner, cheeses.length);
        String cheese = cheeses[cheeseChoice - 1];
        pizzaBuilder.setCheese(cheese);

        Pizza pizza = pizzaBuilder.build();
        pizzas.add(pizza);
        System.out.println("Your pizza: " + pizza.getDescription());
        System.out.println("Total cost: Rs." + pizza.getCost() + " /=");

        // Add to favorites
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

    //choice
    private static int getValidChoice(Scanner scanner, int maxOption) {
        while (true) {
            System.out.print("Enter your choice (1-" + maxOption + "): ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice >= 1 && choice <= maxOption) {
                    return choice;
                }
            } else {
                scanner.nextLine();
            }
            System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void orderPizza(Scanner scanner, List<Customer> customers, List<Pizza> pizzas, PromotionManager promotionManager,
                                   LoyaltyProgram loyaltyProgram) {
        System.out.println("\n=== Order a Pizza ===");
        if (pizzas.isEmpty()) {
            System.out.println("No pizzas available to order. Please add number 3 to create pizza.");
            return;
        }

        // show available pizzas
        System.out.println("Available Pizzas:");
        for (int i = 0; i < pizzas.size(); i++) {
            System.out.println((i + 1) + ". " + pizzas.get(i).getDescription());
        }
        System.out.print("Choose a pizza to order (1-" + pizzas.size() + "): ");
        int pizzaChoice = scanner.nextInt();
        scanner.nextLine();

        Pizza selectedPizza = pizzas.get(pizzaChoice - 1);

        // quantity
        System.out.print("Enter the quantity of pizzas you want to order: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        // Total cost based on quantity
        double pizzaCost = selectedPizza.getCost();
        double totalCost = pizzaCost * quantity;

        // Display the original total cost
        System.out.println(" Cost for " + quantity + " pizza(s): Rs." + totalCost + " /=");

        // Show loyalty points
        System.out.println("You have " + loyaltyProgram.getPoints() + " loyalty points.");
        System.out.print("Do you want to redeem points for a discount? (yes/no): ");
        String redeemChoice = scanner.nextLine();
        double discountFromPoints = 0;

        if ("yes".equalsIgnoreCase(redeemChoice)) {
            System.out.print("Enter the number of points to redeem (1 point = Rs.10/=) : ");
            int pointsToRedeem = scanner.nextInt();
            scanner.nextLine();
            if (pointsToRedeem <= loyaltyProgram.getPoints()) {
                discountFromPoints = pointsToRedeem * 10; //1 point = Rs 10
                loyaltyProgram.redeemPoints(pointsToRedeem);
                System.out.println("Redeemed " + pointsToRedeem + " points for a Rs." + discountFromPoints + " discount.");
            } else {
                System.out.println("Not enough points to redeem.");
            }
        }

        // Apply promotion
        System.out.print("Do you want to apply any promotion? (yes/no): ");
        String applyPromotion = scanner.nextLine();
        double promotionDiscount = 0;

        if ("yes".equalsIgnoreCase(applyPromotion)) {
            System.out.println("Available Promotions:");
            promotionManager.getPromotions().forEach(promotion ->
                    System.out.println(promotion.getName() + ": " + promotion.getDiscount() + "% off")
            );
            System.out.print("Enter promotion name (Please enter promotion name correctly to get the promotion, Eg: Christmas Discount) : ");
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
        System.out.println("Total cost after discount: Rs." + finalCost + " /=");

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

        // Payment method options
        System.out.print("Choose payment method (1 for Credit Card, 2 for Digital Wallet): ");
        int paymentChoice = scanner.nextInt();
        PaymentMethod paymentMethod = (paymentChoice == 1) ? new CreditCardPayment() : new DigitalWalletPayment();
        new PaymentProcessor(paymentMethod).process(finalCost);

        // loyalty points for the just placed order
        loyaltyProgram.addPoints(finalCost);
        System.out.println("Earned loyalty points: " + (int) (finalCost / 100) + " (1 point per Rs. 100 spent).");
        System.out.println("Your new loyalty points balance: " + loyaltyProgram.getPoints());

        // pizza customization options
        CustomizationHandler toppingHandler = new ToppingHandler();
        toppingHandler.setNextHandler(new CheeseHandler());
        scanner.nextLine();
        System.out.print("Add extra customization (e.g., 'extra topping' or 'extra cheese'): ");
        toppingHandler.handleRequest(scanner.nextLine());

        // Order context and order status updates
        OrderContext orderContext = new OrderContext();
        orderContext.setState(new InPreparationState());
        orderContext.request();

        double totalAmount = finalCost;
        Customer customer = customers.get(0);

        Order order = new Order(totalAmount, loyaltyProgram, customer);
        order.addObserver(customer);
        new PlaceOrderCommand(order).execute();

        Pizza enhancedPizza = new ExtraToppingDecorator(selectedPizza);
        System.out.println("Pizza Order Summary: " + enhancedPizza.getDescription());

        // customer feedback
        System.out.print("Provide feedback: ");
        String feedback = scanner.nextLine();

        //customer rating
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

        //order statuses
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
        System.out.println("\n=== Favorite Pizzas ♥ ===");
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

