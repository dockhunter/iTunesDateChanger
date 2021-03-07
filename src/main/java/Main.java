import static consoleUI.UserInput.powerShellReady;
import static core.CommandExec.powerShellExec;

public class Main {

    public static void main(String[] args) {
        // Variable for determining the os type and powershell version
        String osType = System.getProperty("os.name");
        powerShellExec("$PSVersionTable",0);

        try {
            while(true) {
                System.out.println("Path to your music folder: ");
                // According to the os type the matching package is chosen.
                if (osType.matches("(Windows)") && powerShellReady) {
                    consoleUI.UserInput.startInput();
                } else if (!powerShellReady) {
                    System.err.println("ERROR: PowerShell not found,\n" +
                            "please make sure you have PowerShell installed!");
                    break;
                } else if (osType.matches("(Mac)")) {
                    consoleUIMac.UserInput.startInput();
                } else {
                    System.err.println("ERROR: Unsupported Operation System!");
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Wrong format! " + e.getMessage());
        }
    }

}
