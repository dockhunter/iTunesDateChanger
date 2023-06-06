import static consoleUI.InputFromWin.powerShellReady;

import consoleUI.InputFromWin;
import consoleUI.InputFromMac;
import helpers.CommandHelper;

public class Main {

    public static void main(String[] args) {
        // Variable for determining the os type and powershell version
        String osType = System.getProperty("os.name");

        CommandHelper commandHelper = new CommandHelper();
        commandHelper.powerShellExec("$PSVersionTable",0);

        try {
            // According to the os type the matching package is chosen.
            if (osType.matches("(Windows).*") && powerShellReady) {
                System.out.println("Enter a path to music folder: ");

                Thread winThread = new Thread(new InputFromWin());
                winThread.start();
            } else if (osType.matches("(Windows).*") && !powerShellReady) {
                System.err.println("ERROR: PowerShell not found,\n" +
                        "please make sure you have PowerShell installed!");
                return;
            } else if (osType.matches("(Mac).*")) {
                System.out.println("Enter a path to music folder: ");

                Thread macThread = new Thread(new InputFromMac());
                macThread.start();
            } else {
                System.err.println("ERROR: Unsupported Operation System!");
                return;
            }
        } catch (Exception e) {
            System.err.println("Wrong format! " + e.getMessage());
        }
    }

}
