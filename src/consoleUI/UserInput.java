package consoleUI;
import core.ExtractDateTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInput {

    protected static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    /* The user inputs the whole path to the directory where the audio files are */
    public static String readInput() {
        try {
            System.out.println("Type in the path to your music folder:\n" +
                    "(i.e.: G:/Daniel/My Music/BoxSingles)\n");
            return input.readLine();
        } catch (IOException e) {
            return e.toString() + " ERROR";
        }
    }

    /* It then runs through the given directory (recursively) and checks for audio files
    * that have specific audio formats. The whole path to each audio file is then passed
    * to the ExtractDateTime class */
    protected static void handleInput(String path) throws IOException {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < 10; i++) {
            if (listOfFiles[i].isFile()) {
                Pattern pattern = Pattern.compile(".+?\\.(m4a|mp3|aif|wma)$", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(listOfFiles[i].getName());
                if (matcher.matches()) {
                    ExtractDateTime.extract(listOfFiles[i]+ "");
                } else {
                    System.out.println(listOfFiles[i].getName().trim() + " is not an audio file. Skipping...");
                }
            } else if (listOfFiles[i].isDirectory()){
                handleInput(path + listOfFiles[i].getName());
            }
        }
    }

    public static void processInput() throws WrongFormatException {
        try {
            handleInput(readInput());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
