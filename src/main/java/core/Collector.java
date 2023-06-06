package core;

import java.io.File;

public class Collector {
    private String userPath = "";

    // Validating if the path is executable.
    public boolean pathValidator(String userPath) {
        this.userPath = userPath;
        File input = new File(userPath.replaceAll("(\\\\|/)$", ""));

        if (input.isFile()) {
            return true;
        } else if (input.isDirectory() && input.listFiles().length > 0) {
            return true;
        } else {
            return false;
        }
    }
}
