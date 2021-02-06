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
    static String iTunes = "$itunes = New-Object -ComObject iTunes.Application; ";
    static String iTunesPath = "$itunes.LibraryPlaylist.addFile(\"";

    /* Powershell command for setting back the time and date */
    static String resyncDateAndTime =  "powershell.exe -command W32tm /resync /force";

    protected static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    /* The user inputs the whole path to the directory where the audio files are */
    protected static String readInput() {
        try {
            System.out.println("Type in the path to your music folder:");
            return input.readLine();
        } catch (Exception e) {
            return "ERROR: " + e;
        }
    }

    /*
    * It then runs through the given directory (recursively) and checks for audio files
    * that have specific audio formats. The whole path to each audio file is then passed
    * into the CommandExec class where they are executed as commands via cmd.exe or powershell.exe.
    */
    protected static void handleInput(String path) throws IOException {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        /* Starts up iTunes */
        CommandExec.cmdInputExec("start itunes.exe");

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                Pattern pattern = Pattern.compile(".+?\\.(m4a|mp3|aif|wma)$", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(listOfFiles[i].getName());
                if (matcher.matches()) {
                    /* Changing the systems date and time according to the file */
                    CommandExec.cmdInputExec(ExtractDateTime.extract(listOfFiles[i]+ ""));
                    /* Adding the file to iTunes */
                    System.out.println("Adding audio file: " + listOfFiles[i].getName());
                    CommandExec.powerInputExec(iTunes + iTunesPath + path +
                            "/"+ listOfFiles[i].getName() + "\")");
                } else {
                    System.out.println(listOfFiles[i].getName().trim() + " is not an audio file. Skipping...");
                }
            } else if (listOfFiles[i].isDirectory()){
                handleInput(path + listOfFiles[i].getName());
            } else {
                continue;
            }
        }

        /* Setting back date and time, finishing process */
        CommandExec.cmdInputExec(resyncDateAndTime);
        System.out.println("Done");
    }

    public static void processInput() {
        try {
            handleInput(readInput());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
