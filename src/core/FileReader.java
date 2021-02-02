package core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class FileReader {

    static String lastModifiedDate;

    public static String read(String fileNameAndPath) {
        try {
            Path file = Paths.get(fileNameAndPath);
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
            lastModifiedDate = attr.lastModifiedTime().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(lastModifiedDate);
        return lastModifiedDate;
    }
}
