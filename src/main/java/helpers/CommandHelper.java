package helpers;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellNotAvailableException;
import com.profesorfalken.jpowershell.PowerShellResponse;
import me.tongfei.progressbar.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static consoleUI.InputFromWin.*;

public class CommandHelper {

    //
    // Some commands will change the date and time setting for Windows.
    // It is advisable to not browse the internet during this process as
    // certain websites can raise issues regarding outdated or not-yet existing certificates.
    //
    // Used for converting audio files.
    public void jPowerShellExec (String commandString) {
        System.out.println("Executing: " + commandString + "\nAudio files processed: " + sumOfFiles +
                "\\" + (processedFileCount += 1));
        // Creates PowerShell session.
        try (PowerShell powerShell = PowerShell.openSession()) {
            // Execute a command in PowerShell session.
            PowerShellResponse response = powerShell.executeCommand(commandString);
            // Logging the results
            System.out.println("List Processes:" + response.getCommandOutput());
        } catch(PowerShellNotAvailableException exception) {
            System.err.println(exception);
        }
    }

    // Used for everything else.
    public void powerShellExec (String command, int numberOfFiles) {
        command = (command.matches(".+?`.+?")) ? command.replace("`", "``") : command;
        String[] cmdArray = new String[]{"powershell.exe ", command};
        try {
            // Executing the command.
            Process powerShellProcess = Runtime.getRuntime().exec(cmdArray);
            // Logging the results with or without a progress bar.
            reader("bufferedReader", powerShellProcess.getInputStream(), numberOfFiles);
            reader("errorReader", powerShellProcess.getErrorStream(), 0);
            powerShellProcess.getOutputStream().close();
        } catch (Exception exception) {
            System.err.println(exception);
        }
    }

    //
    // While executing each command, the powershell feedback is logged
    // and the progress bar is updated accordingly.
    //
    public void reader(String name, InputStream stream, int numberOfFiles) {
        InputStreamReader inputStream = new InputStreamReader (stream);
        BufferedReader reader = new BufferedReader (inputStream);
        String log = "";

        // If there are number of files provided - log with a progress bar.
        if (numberOfFiles > 0) {
            try (ProgressBar progressBar = new ProgressBar("Progress: ", numberOfFiles)) {
                while ((log = reader.readLine()) != null) {
                    // Updating the progress bar.
                    if (log.matches("(InProgress : False)")) {
                        progressBar.step();
                    } else if (name == "errorReader") {
                        System.out.println(log);
                    }
                }
                inputStream.close();
            } catch (Exception exception) {
                System.err.println(exception);
            }
            // In any other case, log is without a progress bar.
        } else {
            try {
                while ((log = reader.readLine()) != null) {
                    // For a function in Main to check for powershell.exe availability
                    if (log.matches("\n*(PSVersion).*")) {
                        powerShellReady = true;
                    } else if (name == "errorReader") {
                        System.out.println(log);
                    }
                }
                inputStream.close();
            } catch (Exception exception) {
                System.err.println(exception);
            }
        }
    }

    //
    // The received data is fed into the terminal.
    //
    // Some commands will change the date and time setting for the Mac.
    // It is advisable to not browse the internet during this process as
    // certain websites can raise issues regarding outdated or not-yet existing certificates.
    //
    public void macTerminalExec(String command) {
        try {
            // Executing through command prompt.
            String[] arguments = new String[] {"/bin/bash", "-c", command};
            ProcessBuilder builder = new ProcessBuilder(arguments);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            // Logging.
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            System.out.println(bufferedReader.readLine());
        }  catch (IOException exception) {
            System.err.println(exception);
        }
    }
}
