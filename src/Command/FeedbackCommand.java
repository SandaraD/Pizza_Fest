package Command;

public class FeedbackCommand implements Command {
    private String feedback;
    private int rating;

    public FeedbackCommand(String feedback, int rating) {
        this.feedback = feedback;
        this.rating = rating;
    }

    @Override
    public void execute() {
        System.out.println("Feedback received: " + feedback);
        System.out.println("Rating given: " + rating + " ");
    }
}
