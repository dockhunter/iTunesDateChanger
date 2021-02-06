package core;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellNotAvailableException;
import com.profesorfalken.jpowershell.PowerShellResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CommandExec {

    /* The received data is fed into the windows command prompt or powershell.
     *
     * Some commands will change the date and time setting for Windows.
     * It is advisable to not browse the internet during this process as
     * certain websites can raise issues regarding outdated or not-yet existing certificates.
    */
    public static void cmdInputExec(String command) {
        try {
            /* Executing through command prompt */
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c " + command);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            /* Logging*/
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            System.out.println(bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void powerInputExec (String command) throws IOException {
        try (PowerShell powerShell = PowerShell.openSession()) {
            /* Custom configuration for jPowerShell */
            Map<String, String> myConfig = new HashMap<>();
            myConfig.put("maxWait", "1000");

            /* Executing commands through powershell */
            PowerShellResponse response = powerShell.configuration(myConfig).executeCommand(command);

            /* Logging */
            System.out.println(response.getCommandOutput());
        } catch (PowerShellNotAvailableException e) {
            e.printStackTrace();
        }
    }

}
