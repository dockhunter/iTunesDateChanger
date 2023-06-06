package consoleUI;

import core.CollectorOnMac;
import core.WrongFormatException;
import helpers.CommandHelper;
import helpers.ExtractDateTimeHelper;
import me.tongfei.progressbar.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputFromMac extends UIHandler {

    // Command for setting back the time and date.
    public String resyncDateAndTime =  "sudo systemsetup -setnetworktimeserver time.apple.com";
    public static List<File> audioFiles = new ArrayList<>();
    public static int sumOfFiles = 0;

    private CommandHelper commandHelper = new CommandHelper();
    private CollectorOnMac collectorOnMac = new CollectorOnMac();
    private ExtractDateTimeHelper dateTimeHelper = new ExtractDateTimeHelper();

    //
    // Processing the users input by first validating the path
    // that the user typed in, if the path is valid,
    // a collector function collects all the supported audio files it finds.
    // The collected files are then executed / added to iTunes via terminal commands.
    //
    @Override
    public void processInput(String userPath) throws IOException {
        // Starting up iTunes / the Music app.
        commandHelper.macTerminalExec("open -a music.app");

        try {
            if (collectorOnMac.pathValidator(userPath)) {
                collectorOnMac.collectFiles(userPath);
                for (File file : ProgressBar.wrap(audioFiles, "Progress: ")) {
                    // Changing the systems date and time according to the file.
                    commandHelper.macTerminalExec(dateTimeHelper.extractOnMac(file + ""));
                    // Adding the file to iTunes.
                    System.out.println("Adding audio file: " + file.getName());
                    commandHelper.macTerminalExec("open \"" + userPath +
                            "/" + file.getName() + "\"");
                }
                // Setting back date and time, finishing process.
                commandHelper.macTerminalExec(resyncDateAndTime);
                sumOfFiles = 0;
                System.out.println("Process finished.\nYou may enter another path: ");
            } else if (userPath.matches("(EXIT)|(exit)|(q)|(Q)")) {
                System.exit(0);
            } else {
                System.err.println("Incorrect path! Please enter a valid path:");
                startInput();
            }
        } catch (WrongFormatException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
