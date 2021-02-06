package consoleUI;

import core.ExtractDateTime;
import core.CommandExec;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInput {

    /* Powershell commands for adding files to iTunes */
    static String iTunes = " $itunes = New-Object -ComObject iTunes.Application; ";
    static String iTunesAdd = "$itunes.LibraryPlaylist.addFile(\"";

    /* Powershell command for setting back the time and date */
    static String resyncDateAndTime =  "powershell.exe -command W32tm /resync /force";

    /* The user inputs the whole path to the directory where the audio files are */
    public static void readInput() {
        try {
            System.out.println("Path to music folder:");
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            processInput(input.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * It then runs through the given directory (recursively) and checks for audio files
    * that have specific audio formats. The whole path to each audio file is then passed
    * into the CommandExec class where they are executed as commands via cmd.exe or powershell.exe.
    */
    protected static void processInput(String path) throws IOException {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        try {
            /* Starts up iTunes */
            CommandExec.cmdInputExec("start itunes.exe");

            if (listOfFiles != null)
                for (File listOfFile : listOfFiles) {
                    if (listOfFile.isFile()) {
                        Pattern pattern = Pattern.compile(".+?\\.(m4a|mp3|aif|wma)$", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(listOfFile.getName());
                        if (matcher.matches()) {
                            /* Changing the systems date and time according to the file */
                            CommandExec.cmdInputExec(ExtractDateTime.extract(listOfFile + ""));
                            /* Adding the file to iTunes */
                            System.out.println("Adding audio file: " + listOfFile.getName());
                            CommandExec.powerInputExec(iTunes + iTunesAdd + path +
                                    "/" + listOfFile.getName() + "\")");
                        } else {
                            System.out.println(listOfFile.getName().trim() + " is not an audio file. Skipping...");
                        }
                    } else if (listOfFile.isDirectory()) {
                        processInput(path + listOfFile.getName());
                    }
                }

            /* Setting back date and time, finishing process */
            CommandExec.cmdInputExec(resyncDateAndTime);
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
