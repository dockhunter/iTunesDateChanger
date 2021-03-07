package core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static consoleUI.UserInput.*;

public class Collector {
    // Validating if the path is executable
    public static boolean pathValidator(String userPath) {
        File input = new File(userPath.replaceAll("(\\\\|/)$", ""));
        File[] listOfFiles = input.listFiles();
        if (input.isFile()) {
            return true;
        } else if (input.isDirectory() && listOfFiles != null) {
            return true;
        } else {
            return false;
        }
    }

    // Recursively collecting audio files that can be found in the provided path
    public static void collectFiles(String userPath) {
        File input = new File(userPath.replaceAll("(\\\\|/)$", ""));
        List<File> listOfFiles = new ArrayList<>();

        // If the provided path is a file itself, it is added straight to the array
        if (input.isFile()) {
            listOfFiles.add(input);
        } else {
            for (File file : input.listFiles()) {
                listOfFiles.add(file);
            }
        }

        String iTunesAddCommand = "$itunes.LibraryPlaylist.addFile";
        String iTunesConvertCommand = "$itunes.ConvertFile";

        for (File file : listOfFiles) {
                Pattern pattern = Pattern.compile(".+?\\.(m4a|mp3|aif|aac|aiff|wav)$", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(file.getName());
                if (file.isFile() && matcher.find()) {
                    String filePath = "(\\\"" + userPath + "\\" + file.getName() + "\\\"); ";
                    String fileDate = ExtractDateTime.extract(file + "") + "; ";
                    supportedAudioFiles.add(fileDate + iTunesAddCommand + filePath);
                    sumOfFiles++;
                } else if (file.isFile() && file.getName().matches(".+?\\.(wma|WMA)$")) {
                    String filePath = "(\"" + userPath + "\\" + file.getName() + "\")";
                    String fileDate = ExtractDateTime.extract(file + "") + " ;";
                    convertibleAudioFiles.add(fileDate + iTunesConvertCommand + filePath);
                    sumOfFiles++;
                } else if (file.isDirectory()) {
                    collectFiles(userPath + "\\" + file.getName());
                }
        }
        System.out.println("Number of compatible audio files: " + sumOfFiles);
    }
}