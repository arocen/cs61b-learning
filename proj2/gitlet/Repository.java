package gitlet;

import java.io.File;
import static gitlet.Utils.*;
import static gitlet.Utils.join;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  Run commands like init, add, commit, etc.
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");




    /* TODO: fill in the rest of this class. */

    /** Initialize a Gitlet system in current directory. */
    public static void init() {
        // Check existence
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        // Create folders
        GITLET_DIR.mkdir();
        Commit.COMMITS_DIR.mkdir();
        Commit.blob.BLOBS_DIR.mkdir();
        // Create initial commit
        Commit initCommit = new Commit();
        initCommit.save();
    }
    /** Add files to staging area. */
    public void add() {
        // TODO
    }
    /** Commit staged files. */
    public void commit() {
        // TODO
    }
}
