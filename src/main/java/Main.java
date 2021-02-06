import consoleUI.UserInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws Exception {
        try {
            UserInput.processInput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
