import consoleUI.UserInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        try {
            UserInput.readInput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
