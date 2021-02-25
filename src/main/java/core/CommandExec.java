package core;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellNotAvailableException;
import com.profesorfalken.jpowershell.PowerShellResponse;
import me.tongfei.progressbar.ProgressBar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static consoleUI.UserInput.*;

public class CommandExec {

    // The received data is fed into the powershell command line.
    //
    // Some commands will change the date and time setting for Windows.
    // It is advisable to not browse the internet during this process as
    // certain websites can raise issues regarding outdated or not-yet existing certificates.
    //
    public static void powerShellExec (String command, int numberOfFiles) {
        try {
            // Executing the command
            Process powerShellProcess = Runtime.getRuntime().exec("powershell.exe " + command);
            // Getting the results
            reader("bufferedReader", powerShellProcess.getInputStream(), numberOfFiles);
            reader("errorReader", powerShellProcess.getErrorStream(), 0);
            powerShellProcess.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
    // While executing each command, the powershell feedback is logged
    // as well as the progress bar is updated accordingly.
    //
    public static void reader(String name, InputStream stream, int numberOfFiles) {
        try (ProgressBar progressBar = new ProgressBar("Progress: ", numberOfFiles)){
            String line = "";
            // Executes the powershell command.
            InputStreamReader inputStream = new InputStreamReader (stream);
            BufferedReader reader = new BufferedReader (inputStream);
            // Logging the results.
            while ((line = reader.readLine()) != null) {
                // Updating the progress bar.
                if(line.matches("(InProgress : False)")) {
                    progressBar.step();
                } else if (name == "errorReader") {
                    System.out.println(line);
                }
            }
            inputStream.close ();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void jPowerShellExec (String commandString) {
        System.out.println("Executing: " + commandString + "\nAudio files processed: " + sumOfFiles +
                        "\\" + (processedFileCount += 1));
        //Creates PowerShell session (we can execute several commands in the same session)
        try (PowerShell powerShell = PowerShell.openSession()) {
            //Execute a command in PowerShell session
            PowerShellResponse response = powerShell.executeCommand(commandString);
            //Print results
            System.out.println("List Processes:" + response.getCommandOutput());
        } catch(PowerShellNotAvailableException ex) {
            ex.printStackTrace();
        }
    }
}

