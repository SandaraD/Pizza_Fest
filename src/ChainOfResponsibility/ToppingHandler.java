package ChainOfResponsibility;

public class ToppingHandler extends CustomizationHandler {
    @Override
    public void handleRequest(String request) {
        if (request.toLowerCase().contains("topping")) {
            System.out.println("Adding extra topping.");
        } else if (nextHandler != null) {
            nextHandler.handleRequest(request);
        }
    }
}
