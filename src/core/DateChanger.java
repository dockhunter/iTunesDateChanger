package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class DateChanger {

    /* The received date and time is fed into the windows command prompt.
    * This actually changes the date and time in the Windows system itself.
    * It is advisable to do not browse the internet during this process as
    * certain websites can raise issues regarding outdated or not-yet existing certificates. */
    protected static void cmdInputExec(String[] dateAndTime) throws IOException {
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    /* Changing the systems time and date according to the file */
                    "cmd.exe", "/c", "date " + dateAndTime[0] + " && time " + dateAndTime[1]);
            builder.redirectErrorStream(true);
            Process p = builder.start();

            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = r.readLine();
            System.out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
