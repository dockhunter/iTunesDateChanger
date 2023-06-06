package helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class ExtractDateTimeHelper {

    //
    // The path of each audio file from the UserInput class is processed
    // by extracting their "last modified date and time" attributes into an array
    // and returned as a string for command execution.
    //
    public String[] extractOnWin(String fileNameAndPath) {
        try {
            Path file = Paths.get(fileNameAndPath);
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
            return attr.lastModifiedTime().toString().split("[a-zA-Z]");
            // Extract and format the date for powershell.
        } catch (IOException exception) {
            System.err.println(exception);
            return null;
        }
    }

    public String extractOnMac(String fileNameAndPath) {
        try {
            Path file = Paths.get(fileNameAndPath);
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
            String[] dateAndTime = attr.lastModifiedTime().toString().split("[a-zA-Z]");

            // Extract and format the date for terminal command.
            return "sudo date -f %Y%m%d-%H%M%S " +
                    dateAndTime[0].replace("-","") + "-" +
                    dateAndTime[1].replace(":","");

        } catch (IOException e) {
            return e.toString();
        }
    }
}
