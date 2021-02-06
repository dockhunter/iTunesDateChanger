import consoleUI.UserInput;

public class Main {

    public static void main(String[] args) throws Exception {
        try {
            UserInput.processInput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
