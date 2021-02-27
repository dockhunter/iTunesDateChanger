package coreMac;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExec {

    /* The received data is fed into the windows command prompt or powershell.
     *
     * Some commands will change the date and time setting for Windows.
     * It is advisable to not browse the internet during this process as
     * certain websites can raise issues regarding outdated or not-yet existing certificates.
    */
    public static void terminalInputExec(String command) {
        try {
            /* Executing through command prompt */
            String[] arguments = new String[] {"/bin/bash", "-c", command};
            ProcessBuilder builder = new ProcessBuilder(arguments);
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
}
