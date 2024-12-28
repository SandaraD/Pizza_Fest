package ChainOfResponsibility;

public class CheeseHandler extends CustomizationHandler {
    @Override
    public void handleRequest(String request) {
        if (request.toLowerCase().contains("cheese")) {
            System.out.println("Adding extra cheese.");
        } else if (nextHandler != null) {
            nextHandler.handleRequest(request);
        }
    }
}
