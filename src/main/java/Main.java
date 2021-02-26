import consoleUI.UserInput;
import core.CommandExec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        try {
            System.out.println("Path to music folder:");
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            UserInput.processInput(input.readLine());
            /* Setting back date and time, finishing process */
            CommandExec.cmdInputExec(UserInput.resyncDateAndTime);
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
