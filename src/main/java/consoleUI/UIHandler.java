package consoleUI;

import core.WrongFormatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UIHandler implements Runnable {

    @Override
    public void run() {
        try {
            startInput();
        } catch (WrongFormatException | IOException | InterruptedException exception) {
            System.err.println(exception);
        }
    }

    // Starting function that keeps the input feed alive.
    public void startInput() throws WrongFormatException, IOException, InterruptedException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String userPath = input.readLine();
        processInput(userPath);
    }

    public void processInput(String userPath) throws IOException, WrongFormatException, InterruptedException {}
}
