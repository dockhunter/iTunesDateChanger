package coreMac;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static consoleUIMac.UserInput.audioFiles;
import static consoleUIMac.UserInput.sumOfFiles;

public class Collector {
    // Validates the path inputted by the user.
    public static boolean pathValidator(String userPath) {
        File folder = new File(userPath.replaceAll("(\\\\|/)$", ""));
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            return true;
        } else {
            return false;
        }
    }

    //
    // Runs through the given directory (recursively) and checks for audio files
    // that have specific audio formats. Each audio file is then saved into and array list.
    //
    public static void collectFiles(String userPath) {
        File folder = new File(userPath.replaceAll("(\\\\|/)$", ""));
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            Pattern pattern = Pattern.compile(".+?\\.(m4a|mp3|aif|aac|aiff|wma|wav)$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(file.getName());
            if (file.isFile() && matcher.find()) {
                audioFiles.add(file);
                sumOfFiles++;
            } else if (file.isDirectory()) {
                collectFiles(userPath + "\\" + file.getName());
            }
        }
        System.out.println("Number of compatible audio files: " + sumOfFiles);
    }
}