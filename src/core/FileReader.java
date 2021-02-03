package core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class FileReader {

    /* The path of each audio file from the UserInput class is processed
    * by setting their last modified date and time attributes into a set of array.
    * This array is then passed on to the DateChanger class. */
    public static void read(String fileNameAndPath) {
        try {
            Path file = Paths.get(fileNameAndPath);
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
            DateChanger.cmdInputExec(
                    attr.lastModifiedTime().toString().split("[a-zA-Z]"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
