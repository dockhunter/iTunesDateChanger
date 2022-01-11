package core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static consoleUI.UserInput.*;
import static core.CommandExec.powerShellExec;

public class Collector {
    // Validating if the path is executable.
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

    // Recursively collecting audio files that can be found in the provided path.
    public static void collectFiles(String userPath) {
        File input = new File(userPath.replaceAll("(\\\\|/)$", ""));
        List<File> listOfFiles = new ArrayList<>();

        // If the provided path is a single file, it is added straight to the array
        // otherwise and array of found files is created.
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
                String fileName = file.getName();
                Pattern pattern = Pattern.compile(".+?\\.(m4a|mp3|aif|aac|aiff|wav)$", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(fileName);
                if (file.isFile()) {
                    if (matcher.matches()) {
                        String filePath = "(\\\"" + file + "\\\"); ";
                        String fileDate = ExtractDateTime.extract(file + "") + "; ";
                        supportedAudioFiles.add(fileDate + iTunesAddCommand + filePath);
                        sumOfFiles++;
                    } else if (fileName.matches(".+?\\.(wma|WMA)$")) {
                        String filePath = "(\"" + file + "\")";
                        String fileDate = ExtractDateTime.extract(file + "") + " ;";
                        convertibleAudioFiles.add(fileDate + iTunesConvertCommand + filePath);
                        sumOfFiles++;
                    }
                } else if (file.isDirectory()) {
                    collectFiles(userPath + "\\" + fileName);
                }
        }
        System.out.println("Number of compatible audio files: " + sumOfFiles +
                "\nNumber of audio files (.wma) needs conversion: " + convertibleAudioFiles.size());
    }
}