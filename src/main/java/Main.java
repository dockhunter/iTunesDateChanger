import consoleUI.UserInput;
import core.WrongFormatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        System.out.println("Type in the full path to your music folder: ");
        try {
            while(true) {
                /* Processing the input from the user */
                UserInput.startInput();
                /* Executing the powershell commands & finishing the process */
                UserInput.finishProcess();
            }
        } catch (IOException | WrongFormatException e) {
            System.err.println("Wrong format. " + e.getMessage());
        }
//        System.out.println(System.getProperty("os.name"));
//        System.getProperties().list(System.out);
    }
}
