package Promotion_Strategy;

import java.time.LocalDate;
import java.time.Month;

public class Promotion {
    private String name;
    private double discount;  // Discount percentage (e.g., 25% or 10%)
    private Month startMonth;
    private Month endMonth;
    private PromotionType promotionType;  // New field to distinguish between promotion types

//Promotion Type
    public enum PromotionType {
        CHRISTMAS_DISCOUNT
    }


    public Promotion(Month startMonth, Month endMonth, double discount, String name) {
        this.promotionType = PromotionType.CHRISTMAS_DISCOUNT;
        this.startMonth = startMonth;
        this.endMonth = endMonth;
        this.discount = discount;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getDiscount() {
        return discount;
    }

    // Apply the promotion
    public double applyPromotion(double totalPrice, int pizzaCount, LocalDate orderDate) {
        if (promotionType == PromotionType.CHRISTMAS_DISCOUNT && isChristmasMonth(orderDate)) {
            // Apply 10% discount
            return totalPrice * (1 - discount / 100);
        }
        return totalPrice;
    }

    // Check if order was placed in December month
    private boolean isChristmasMonth(LocalDate orderDate) {
        return orderDate.getMonth() == Month.DECEMBER;
    }
}


