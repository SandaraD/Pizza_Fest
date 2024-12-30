package Promotion_Strategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PromotionManager {
    private List<Promotion> promotions = new ArrayList<>();

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
        return null;
    }

    public void addPromotion(Promotion promotion) {
        promotions.add(promotion);
    }

}
