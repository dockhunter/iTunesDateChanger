package core;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Counter {
    public static int countFiles(File[] listOfFiles) {
        int count = 0;

        for (File file : listOfFiles) {
            Pattern pattern = Pattern.compile(".+?\\.(m4a|mp3|aif|aac|aiff|wma|wav)$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(file.getName());
            if (file.isFile() && matcher.find()) {
                count++;
            }
        }
        System.out.println("Number of compatible audio files: " + count);
        return count;
    }
}