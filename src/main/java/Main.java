import consoleUI.UserInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("Path to music folder:");
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            UserInput.processInput(input.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
