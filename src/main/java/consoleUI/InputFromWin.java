package consoleUI;

import core.CollectorOnWin;
import core.WrongFormatException;
import helpers.CommandHelper;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputFromWin extends UIHandler {

    //
    // Powershell commands for starting up and adding files into iTunes.
    // FYI: "$itunes | Get-Member" will get all methods for the give app
    //
    private final String iTunesOpenCommand = "$itunes = New-Object -ComObject iTunes.Application; ";
    private final String iTunesTaskChecker = "$itunes = Get-Process iTunes -ErrorAction SilentlyContinue; " +
                                                    "if ($itunes) {$itunes | Stop-Process -Force}";
    private final String explorerTaskKill = "taskkill /f /im explorer.exe";
    private final String explorerTaskStart = "start explorer.exe";
    private StringBuilder commandFeed = new StringBuilder();
    private boolean explorerKilled = false;

    public static int sumOfFiles = 0;
    public static int processedFileCount = 0;
    public static boolean powerShellReady = false;

    public static List<String> supportedAudioFiles = new ArrayList<>();
    public static List<String> convertibleAudioFiles = new ArrayList<>();

    private CommandHelper commandHelper = new CommandHelper();
    private CollectorOnWin collectorOnWin = new CollectorOnWin();

    //
    // Runs through the given directory (recursively) and checks for audio files
    // that have specific audio formats. The whole path to each audio file is then passed
    // into an execution function where they are executed via powershell commands.
    //
    @Override
    public void processInput(String userPath) throws IOException, WrongFormatException, InterruptedException {
        if (collectorOnWin.pathValidator(userPath)) {
            collectorOnWin.collectFiles(userPath);

            // Checking if iTunes is already running close it.
            commandHelper.powerShellExec(iTunesTaskChecker, 0);

            if(supportedAudioFiles.size() != 0) {
                // For safety reasons killing the explorer in Windows.
                if (supportedAudioFiles.size() > 6000) {
                    commandHelper.powerShellExec(explorerTaskKill, 0);
                    explorerKilled = true;
                }
                // To prevent overloading powershell with commands
                // the commandFeed is executed if it has more than 200 files registered
                // or the string itself contains more than 25000 characters.
                for (String audioFile : supportedAudioFiles) {
                    commandFeed.append(audioFile);
                    if (iTunesOpenCommand.length() + commandFeed.length() > 25000) {
                        executeInput(commandFeed.toString().split(";").length/2);
                        commandFeed.setLength(0);
                    } else if (commandFeed.toString().split(";").length - 1 == 400) {
                        executeInput(200);
                        commandFeed.setLength(0);
                    }
                }
                executeInput(commandFeed.toString().split(";").length/2);
            }

            if (convertibleAudioFiles.size() != 0) {
                ProgressBarBuilder pbb = new ProgressBarBuilder();
                ProgressBar.wrap(convertibleAudioFiles,pbb).forEach(
                        audioFile -> commandHelper.jPowerShellExec(iTunesOpenCommand + audioFile));
            }

            if (explorerKilled) {
                commandHelper.powerShellExec(explorerTaskStart, 0);
            }

            // Setting back date and time, resetting variables and finishing the process.
            String resyncDate = "W32tm /resync /force";
            commandHelper.powerShellExec(resyncDate, 0);
            cleanUpVariables();
            
            // Time needed for iTunes to process added song(s)
            Thread.sleep(4000);
            System.out.println("Process finished.");
        } else if (userPath.matches("(EXIT)|(exit)|(q)|(Q)")) {
            System.exit(0);
        } else {
            System.err.println("Incorrect path! Please enter a valid path:");
            startInput();
        }
    }

    // Feeds the concatenated commands into the powershell command line for execution.
    public void executeInput(int numberOfFiles) {
        try {
            System.out.println(
                    "Executing: " + iTunesOpenCommand + commandFeed +
                    "\nProcessing audio files: " + sumOfFiles +
                    "\\" + (processedFileCount += numberOfFiles));

            // Executing the powershell commands.
            commandHelper.powerShellExec(iTunesOpenCommand + commandFeed, numberOfFiles);
        } catch (Exception exception) {
            System.err.println(exception);
        }
    }

    public void cleanUpVariables() {
        commandFeed.setLength(0);
        sumOfFiles = 0;
        processedFileCount = 0;
        convertibleAudioFiles.clear();
        supportedAudioFiles.clear();
    }
}
