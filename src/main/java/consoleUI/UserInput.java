package consoleUI;

import core.Counter;
import core.ExtractDateTime;
import core.CommandExec;
import core.WrongFormatException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInput {

    /* Powershell commands for starting up and adding files to iTunes */
    private static String iTunes = "$itunes = New-Object -ComObject iTunes.Application; ";
    private static String iTunesAdd = "$itunes.LibraryPlaylist.addFile(\\\"";
    private static String feed = "";
    public static int count = 0;

    /* Powershell command for setting back the time and date */
    public static String resyncDateAndTime =  "W32tm /resync /force";
    private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    /* Starting function that keeps the input feed alive */
    public static void startInput() throws IOException, WrongFormatException {
        try {
            processInput(input.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * It then runs through the given directory (recursively) and checks for audio files
     * that have specific audio formats. The whole path to each audio file is then passed
     * into the CommandExec class where they are executed as commands via cmd.exe or powershell.exe.
     */
    public static void processInput(String path) throws IOException, WrongFormatException {

        File folder = new File(path.replaceAll("(\\\\|/)$", ""));
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null && path.matches("\\p{Upper}:((\\\\|/).+)+")) {
            count = Counter.countFiles(listOfFiles);
            for (File file : listOfFiles) {
                    Pattern pattern = Pattern.compile(".+?\\.(m4a|mp3|aif|aac|aiff|wma|wav)$", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(file.getName());
                    if (file.isFile() && matcher.find()) {
                        /* Assembling the string for the powershell command. */
                        feed += ExtractDateTime.extract(file + "") + "; " +
                                iTunesAdd + path + "\\" + file.getName() + "\\\"); ";
                    } else if (file.isDirectory()) {
                        if (feed != "") {
                            System.out.println("Executing: " + iTunes + feed);
                            CommandExec.powerShellExec(iTunes + feed);
                            feed = "";
                            count = 0;
                        }
                        processInput(path + "\\" + file.getName());
                    } else {
                        System.out.println(file.getName().trim() + " is not an audio file. Skipping...");
                    }
            }
        } else if (path.matches("(EXIT)|(exit)|(q)|(Q)")) {
            System.exit(0);
        } else {
            System.err.println("Path not valid. Please enter a valid path.");
            startInput();
        }
    }

    public static void finishProcess() {
        try {
            System.out.println("Executing: " + iTunes + feed);
            /* Executing the powershell commands */
            CommandExec.powerShellExec(iTunes + feed);
            /* Setting back date and time, finishing process */
            CommandExec.powerShellExec(resyncDateAndTime);
            System.out.println("Process finished successfully.\nYou can enter another folder path: ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
