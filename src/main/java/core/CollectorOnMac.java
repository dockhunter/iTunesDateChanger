package core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static consoleUI.InputFromMac.*;

public class CollectorOnMac extends Collector {

    //
    // Runs through the given directory (recursively) and checks for audio files
    // that have specific audio formats. Each audio file is then saved into and array list.
    //
    public void collectFiles(String userPath) {
        File input = new File(userPath.replaceAll("(\\\\|/)$", ""));
        List<File> listOfFiles = new ArrayList<>();

        if (input.isFile()) {
            listOfFiles.add(input);
        } else {
            for (File file : input.listFiles()) {
                listOfFiles.add(file);
            }
        }

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
