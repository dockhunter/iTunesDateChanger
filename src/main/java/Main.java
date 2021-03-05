import core.WrongFormatException;
import java.io.IOException;
import static consoleUI.UserInput.startInput;
import static core.CommandExec.powerShellExec;

public class Main {

    public static void main(String[] args) {
        try {
            while(true) {
                System.out.println("Path to your music folder: ");
                // Processing the input from the user.
                startInput();
            }
        } catch (Exception e) {
            System.err.println("Wrong format. " + e.getMessage());
        }
//        System.out.println(System.getProperty("os.name"));
//        System.getProperties().list(System.out);
    }
}
