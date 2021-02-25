package core;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static consoleUI.UserInput.*;

public class Collector {
    public static boolean pathValidator(String userPath) {
        File folder = new File(userPath.replaceAll("(\\\\|/)$", ""));
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            return true;
        } else {
            return false;
        }
    }

    public static void collectFiles(String userPath) {
        File folder = new File(userPath.replaceAll("(\\\\|/)$", ""));
        File[] listOfFiles = folder.listFiles();

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