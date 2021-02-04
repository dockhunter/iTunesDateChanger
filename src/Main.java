import consoleUI.UserInput;
import java.lang.invoke.WrongMethodTypeException;

public class Main {

    public static void main(String[] args) throws Exception {
        try{
            UserInput.processInput();
        } catch (WrongMethodTypeException e) {
            e.printStackTrace();
        }
    }
}
