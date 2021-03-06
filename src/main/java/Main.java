import static core.CommandExec.powerShellExec;

public class Main {

    public static void main(String[] args) {
        // Variable for determining the os type
        String osType = System.getProperty("os.name");

        try {
            while(true) {
//                System.out.println("Path to your music folder: ");
//                // Processing the input from the user,
//                // According to the os type the matching package is chosen.
//                if (osType.matches("(Windows)")) {
//                    consoleUI.UserInput.startInput();
//                } else if (osType.matches("(Mac)")) {
//                    consoleUIMac.UserInput.startInput();
//                } else {
//                    System.err.println("Unsupported Operation System");
//                }
                powerShellExec("$PSVersionTable", 0);
                break;
            }
        } catch (Exception e) {
            System.err.println("Wrong format. " + e.getMessage());
        }
    }

}
