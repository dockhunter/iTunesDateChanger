package core;

import consoleUI.UserInput;
import me.tongfei.progressbar.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class CommandExec {

    /* The received data is fed into the windows command prompt or powershell.
     *
     * Some commands will change the date and time setting for Windows.
     * It is advisable to not browse the internet during this process as
     * certain websites can raise issues regarding outdated or not-yet existing certificates.
     */
    public static void powerShellExec (String file) throws IOException {
        try {
            String command = "powershell.exe " + file;
            // Executing the command
            Process powerShellProcess = Runtime.getRuntime().exec(command);
            // Getting the results
            reader("bufferedReader", powerShellProcess.getInputStream());
            reader("errorReader", powerShellProcess.getErrorStream());
            powerShellProcess.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reader(String name, InputStream stream) {
        try (ProgressBar pb = new ProgressBar("Audio files processed", UserInput.count)){
            String line = "";
            InputStreamReader inputStream = new InputStreamReader (stream);
            BufferedReader reader = new BufferedReader (inputStream);
            while ((line = reader.readLine()) != null) {
                if(line.matches("(InProgress : False)")) {
                    pb.step();
                    UserInput.count--;
                } else if (name == "errorReader") {
                    System.out.println(line);
                }
            }
            inputStream.close ();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

