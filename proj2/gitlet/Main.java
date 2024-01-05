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
            case "rm":
                // Usage: java gitlet.Main rm [file name]
                String fileToBeRemoved = args[1];
                Repository.rm(fileToBeRemoved);
                break;
            case "log":
                // Usage: java gitlet.Main log
                Repository.log();
                break;
            case "global-log":
                // Usage: java gitlet.Main global-log
                Repository.globalLog();
                break;
            case "find":
                // Usage: java gitlet.Main find [commit message]
                Repository.find(args[1]);
                break;
            case "checkout":
                // TODO
                // Version 1
                // Usage: java gitlet.Main checkout -- [file name]
                if (args.length == 3) {
                    Repository.checkout(args[2]);
                }
                // Version 2
                // Usage: java gitlet.Main checkout [commit id] -- [file name]
                if (args.length == 4) {
                    Repository.checkout(args[1], args[3]);
                }
                // Version 3
                // Usage: java gitlet.Main checkout [branch name]
                if (args.length == 3) {
                    Repository.checkoutBranch(args[1]);
                }

            // TODO: FILL THE REST IN
        }
    }

}
