import consoleUIMac.UserInput;
import static consoleUIMac.UserInput.processInput;
import static coreMac.CommandExec.terminalInputExec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("Path to music folder:");
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            processInput(input.readLine());
            /* Setting back date and time, finishing process */
            terminalInputExec(UserInput.resyncDateAndTime);
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
