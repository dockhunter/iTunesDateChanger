package consoleUI;

import core.WrongFormatException;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static core.Collector.collectFiles;
import static core.Collector.pathValidator;
import static core.CommandExec.jPowerShellExec;
import static core.CommandExec.powerShellExec;

public class UserInput {

    // Powershell commands for starting up and adding files to iTunes.
    private static final String iTunesOpenCommand = "$itunes = New-Object -ComObject iTunes.Application; ";
    private static String commandFeed = "";
    public static int sumOfFiles = 0;
    public static int processedFileCount = 0;
    public static List<String> supportedAudioFiles = new ArrayList<>();
    public static List<String> convertibleAudioFiles = new ArrayList<>();

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
    // It then runs through the given directory (recursively) and checks for audio files
    // that have specific audio formats. The whole path to each audio file is then passed
    // into an execution function where they are executed via powershell commands.
    //
    public static void processInput(String userPath) throws IOException, WrongFormatException {
        if (pathValidator(userPath)) {
            collectFiles(userPath);
            for (String audioFile : supportedAudioFiles) {
                commandFeed += audioFile;
                if (iTunesOpenCommand.length() + commandFeed.length() > 25000) {
                    executeInput(commandFeed.split(";").length/2);
                    commandFeed = "";
                } else if (commandFeed.split(";").length - 1 == 400) {
                    executeInput(200);
                    commandFeed = "";
                }
            }
            if(convertibleAudioFiles.size() != 0) {
                ProgressBarBuilder pbb = new ProgressBarBuilder();
                ProgressBar.wrap(convertibleAudioFiles,pbb).forEach(
                        audioFile -> jPowerShellExec(iTunesOpenCommand + audioFile));
            }

            executeInput(commandFeed.split(";").length/2);
            // Setting back date and time, finishing process.
            powerShellExec( "W32tm /resync /force",0);
            cleanUpVariables();

            System.out.println("Process finished.\nYou may enter another path: ");
        } else if (userPath.matches("(EXIT)|(exit)|(q)|(Q)")) {
            System.exit(0);
        } else {
            System.err.println("Incorrect path! Please enter a valid path:");
            startInput();
        }
    }

    // Feeds the concatenated commands into the powershell command line for execution.
    public static void executeInput(int numberOfFiles) {
        try {
            System.out.println("Executing: " + iTunesOpenCommand + commandFeed +
                    "\nAudio files processed: " + sumOfFiles +
                    "\\" + (processedFileCount += numberOfFiles));
            // Executing the powershell commands
            powerShellExec(iTunesOpenCommand + commandFeed, numberOfFiles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cleanUpVariables() {
        commandFeed = "";
        sumOfFiles = 0;
        processedFileCount = 0;
        convertibleAudioFiles.clear();
        supportedAudioFiles.clear();
    }
}
