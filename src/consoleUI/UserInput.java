package consoleUI;

import core.FileReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInput {

    private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    static String file;

    public static String readLine() {
        try {
            System.out.println("Type in the path to the directory: ");
            return input.readLine();
        } catch (IOException e) {
            return e.toString();
        }
    }

    public static void handleInput(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                Pattern pattern = Pattern.compile(".+?\\.(m4a|mp3|aif|wma)$", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(listOfFiles[i].getName());
                if (matcher.matches()) {
                    FileReader.read(listOfFiles[i]+ "");
                    break;
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
            handleInput(readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
