package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class iTunesHandler {

    protected static void addFileToiTunes(String pathToFile) throws IOException {

        /* Searching for iTunes in the Windows */
        String iTunes = "powershell.exe Get-CimInstance Win32_COMSetting | " +
                "Select-Object ProgId, Caption | " +
                "Where-Object Caption -ILike \"*itunes*\"";
        String addFile = "$itunes.AddFile(\" " + pathToFile + "\")";

        /* Executing the command in powershell */
        Process powerShellProcess = Runtime.getRuntime().exec(iTunes + addFile);
        powerShellProcess.getOutputStream().close();

        /* Logging */
        String line;
        System.out.println("Standard Output:");
        BufferedReader stdout = new BufferedReader(new InputStreamReader(
                powerShellProcess.getInputStream()));
        while ((line = stdout.readLine()) != null) {
            System.out.println(line);
        }
        stdout.close();
        System.out.println("Standard Error:");
        BufferedReader stderr = new BufferedReader(new InputStreamReader(
                powerShellProcess.getErrorStream()));
        while ((line = stderr.readLine()) != null) {
            System.out.println(line);
        }
        stderr.close();
        System.out.println("Done");
    }
}
