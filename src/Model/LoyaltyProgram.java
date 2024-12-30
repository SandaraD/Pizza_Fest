package Model;

public class LoyaltyProgram {
    private int points;


    public void addPoints(double amountSpent) {
        int pointsEarned = (int) (amountSpent / 100); //1 point for every 100 spent
        this.points += pointsEarned;
    }

    public void redeemPoints(int pointsToRedeem) {
        if (points >= pointsToRedeem) {
            points -= pointsToRedeem;
            System.out.println("You redeemed " + pointsToRedeem + " points! Remaining points: " + points);
        } else {
            System.out.println("Not enough points to redeem.");
        }
    }

    public int getPoints() {
        return points;
    }
}
