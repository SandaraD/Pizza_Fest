package Promotion;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class Promotion {
    private String name;
    private double discount;  // Discount percentage (e.g., 25% or 10%)
    private Month startMonth;
    private Month endMonth;
    private PromotionType promotionType;  // New field to distinguish between promotion types

    // Enum to differentiate between different promotion types
    public enum PromotionType {
        BUY_TWO_GET_DISCOUNT,
        CHRISTMAS_DISCOUNT
    }

    // Constructor for "Buy 2, Get 25% off" promotion
    public Promotion(PromotionType promotionType, double discount, String name) {
        this.promotionType = promotionType;
        this.discount = discount;
        this.name = name;
    }

    // Constructor for "Christmas Discount" promotion
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

    public Month getStartMonth() {
        return startMonth;
    }

    public Month getEndMonth() {
        return endMonth;
    }

    public PromotionType getPromotionType() {
        return promotionType;
    }

    // Apply the promotion based on the promotion type and pizza count
    public double applyPromotion(double totalPrice, int pizzaCount, LocalDate orderDate) {
        if (promotionType == PromotionType.BUY_TWO_GET_DISCOUNT && pizzaCount >= 2) {
            // Apply 25% discount if 2 or more pizzas are ordered
            return totalPrice * (1 - discount / 100);
        } else if (promotionType == PromotionType.CHRISTMAS_DISCOUNT && isChristmasMonth(orderDate)) {
            // Apply 10% discount for Christmas month (December)
            return totalPrice * (1 - discount / 100);
        }
        return totalPrice;  // No discount if conditions are not met
    }

    // Check if the order was placed in Christmas month (December)
    private boolean isChristmasMonth(LocalDate orderDate) {
        return orderDate.getMonth() == Month.DECEMBER;
    }
}


