package consoleUI;

import core.ExtractDateTime;
import core.CommandExec;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInput {

    /* Powershell commands for adding files to iTunes */
    static String iTunes = " $itunes = New-Object -ComObject iTunes.Application; ";
    static String iTunesAdd = "$itunes.LibraryPlaylist.addFile(\"";

    /* Powershell command for setting back the time and date */
    public static String resyncDateAndTime =  "powershell.exe -command W32tm /resync /force";

    /*
    * It then runs through the given directory (recursively) and checks for audio files
    * that have specific audio formats. The whole path to each audio file is then passed
    * into the CommandExec class where they are executed as commands via cmd.exe or powershell.exe.
    */
    public static void processInput(String path) throws IOException {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        try {
            /* Starts up iTunes */
            CommandExec.cmdInputExec("start itunes.exe");

            if (listOfFiles != null)
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        Pattern pattern = Pattern.compile(".+?\\.(m4a|mp3|aif|wma|wav)$", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(file.getName());
                        if (matcher.matches()) {
                            /* Changing the systems date and time according to the file */
                            CommandExec.cmdInputExec(ExtractDateTime.extract(file + ""));
                            /* Adding the file to iTunes */
                            System.out.println(path + "\nAdding audio file: " + file.getName());
                            CommandExec.powerInputExec(iTunes + iTunesAdd + path +
                                    "/" + file.getName() + "\")");
                        } else {
                            System.out.println(file.getName().trim() + " is not an audio file. Skipping...");
                        }
                    } else if (file.isDirectory()) {
                        processInput(path + "/" + file.getName());
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
