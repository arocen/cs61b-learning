package gitlet;

import java.io.File;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // Handle the `init` command
                Repository.init();
                break;
            case "add":
                String fileToBeAdded = args[1];
                Repository.add(fileToBeAdded);
                break;
            case "commit":
                String message = args[1];
                // Message can't be blank
                if (message == "") {
                    System.out.print("Please enter a commit message.");
                    System.exit(0);
                }
                Repository.commit(message);
                break;
            // TODO: FILL THE REST IN
        }
    }

}
