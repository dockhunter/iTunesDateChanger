package core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class ExtractDateTime {

    /* The path of each audio file from the UserInput class is processed
    by extracting their "last modified date and time" attributes into an array. */
    public static void extract(String fileNameAndPath) {
        try {
            Path file = Paths.get(fileNameAndPath);
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);

            /* The array is then passed on to the DateChanger class. */
            DateChanger.cmdInputExec(
                    attr.lastModifiedTime().toString().split("[a-zA-Z]"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
