import consoleUI.UserInput;
import static consoleUI.UserInput.processInputMac;
import static core.CommandExec.terminalInputExec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("Path to music folder:");
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            processInputMac(input.readLine());
            /* Setting back date and time, finishing process */
            terminalInputExec(UserInput.resyncDateAndTimeMac);
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
