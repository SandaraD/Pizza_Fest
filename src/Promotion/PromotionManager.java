package Promotion;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class PromotionManager {
    private List<Promotion> promotions = new ArrayList<>();  // Use ArrayList for mutability

//    public PromotionManager() {
//        // Initialize the list of promotions with mutable list
//        promotions.add(new Promotion(Promotion.PromotionType.BUY_TWO_GET_DISCOUNT, 25.0, "Buy 2, Get 25% Off"));
//        promotions.add(new Promotion(Month.DECEMBER, Month.DECEMBER, 10.0, "Christmas Discount"));
//    }

    // Method to get the list of promotions
    public List<Promotion> getPromotions() {
        return promotions;
    }

    // Method to retrieve a promotion by name
    public Promotion getPromotion(String promoName) {
        for (Promotion promo : promotions) {
            if (promo.getName().equals(promoName)) {
                return promo;
            }
        }
        return null;  // Return null if no promotion is found by name
    }

    // Method to add a promotion
    public void addPromotion(Promotion promotion) {
        promotions.add(promotion);  // Now it will work, as the list is mutable
    }

    // Apply all promotions to the order and update the total price
    public double applyPromotions(double totalPrice, int pizzaCount, LocalDate orderDate) {
        // Loop through each promotion and apply it if applicable
        for (Promotion promo : promotions) {
            totalPrice = promo.applyPromotion(totalPrice, pizzaCount, orderDate);
        }
        return totalPrice;
    }

    // Optionally, if you want to check if a specific promotion is applicable, you can add that logic here.
    public boolean isPromotionApplicable(Promotion promotion, int pizzaCount, LocalDate orderDate) {
        return promotion.applyPromotion(0, pizzaCount, orderDate) != 0;
    }

    // Get all active promotions
    public List<Promotion> getActivePromotions() {
        return promotions;
    }

    // New method to display all promotions and their prices
    public void displayPromotionsWithPrice(int pizzaCount, LocalDate orderDate) {
        for (Promotion promo : promotions) {
            double discountedPrice = promo.applyPromotion(100, pizzaCount, orderDate);  // Example with a base price of 100
            System.out.println("Promotion: " + promo.getName());
            System.out.println("Discounted Price: " + discountedPrice);
        }
    }
}





//public class PromotionManager {
//    private List<Promotion> promotions = new ArrayList<>();
//
//    public void addPromotion(Promotion promotion) {
//        promotions.add(promotion);
//    }
//
//    public List<Promotion> getActivePromotions() {
//        List<Promotion> activePromotions = new ArrayList<>();
//        Month currentMonth = java.time.LocalDate.now().getMonth();
//
//        for (Promotion promotion : promotions) {
//            // Use isPromotionActive() or isActive(currentMonth)
//            if (promotion.isActive(currentMonth)) {
//                activePromotions.add(promotion);
//            }
//        }
//
//        return activePromotions;
//    }
//}
