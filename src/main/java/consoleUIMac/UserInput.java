package consoleUIMac;

import core.WrongFormatException;
import coreMac.ExtractDateTime;
import me.tongfei.progressbar.ProgressBar;

import static coreMac.CommandExec.*;
import static coreMac.Collector.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UserInput {

    // Command for setting back the time and date.
    public static String resyncDateAndTime =  "sudo systemsetup -setnetworktimeserver time.apple.com";
    public static List<File> audioFiles = new ArrayList<>();
    public static int sumOfFiles = 0;

    // Starting function that keeps the input feed alive.
    public static void startInput() throws WrongFormatException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        try {
            String userPath = input.readLine();
            processInput(userPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //
    // Processing the users input by first validating the path
    // that the user typed in, if the path is valid,
    // a collector function collects all the supported audio files it finds.
    // The collected files are then executed / added to iTunes via terminal commands.
    //
    public static void processInput(String userPath) throws IOException {
        // Starting up iTunes / the Music app.
        terminalInputExec("open -a music.app");

        try {
            if (pathValidator(userPath)) {
                collectFiles(userPath);
                for (File file : ProgressBar.wrap(audioFiles, "Progress: ")) {
                    // Changing the systems date and time according to the file.
                    terminalInputExec(ExtractDateTime.extract(file + ""));
                    // Adding the file to iTunes.
                    System.out.println("Adding audio file: " + file.getName());
                    terminalInputExec("open \"" + userPath +
                            "/" + file.getName() + "\"");
                }
                // Setting back date and time, finishing process.
                terminalInputExec(resyncDateAndTime);
                sumOfFiles = 0;
                System.out.println("Process finished.\nYou may enter another path: ");
            } else if (userPath.matches("(EXIT)|(exit)|(q)|(Q)")) {
                System.exit(0);
            } else {
                System.err.println("Incorrect path! Please enter a valid path:");
                startInput();
            }
        } catch (WrongFormatException e) {
            e.printStackTrace();
        }
    }

}
