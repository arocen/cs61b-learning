package gitlet;

import java.io.File;
import java.util.List;

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
    /** Directory where staged file is saved. */
    public static final File STAGE_DIR = join(GITLET_DIR, "stage");


    /** Initialize a Gitlet system in current directory. */
    public static void init() {
        // Check existence
        if (GITLET_DIR.exists()) {
            System.out.print("A Gitlet version-control system already exists in the current directory.");
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
    /** Add files to staging area.
     *  The staging area is in 'stage' folder of '.gitlet'.
     *  If the version of file added is identical to that in current version, do not stage it to be added,
     *  and remove it from the staging area if it is already there.
     *
     *  In Gitlet, only one file may be added at a time.
     *
     *  @param filename Name of the single file to be added. */
    public void add(String filename) {
        File path = join(CWD, filename);
        // If the file to be added does not exist, raise error.
        if (!path.exists()) {
            System.out.print("File does not exist.");
            System.exit(0);
        }

        // TODO: think out whether using filename or hashcode to save files in staging area
        // Instantiate a blob from file to be added.
        Commit.blob fileToBeAdded = new Commit.blob(path, filename);
        // Load current commit
        String currentCommitHash = Commit.head;
        Commit current = Commit.load(currentCommitHash);
        // If this file exists in current commit, check if they are identical
        if (current.blobs.contains(fileToBeAdded)) {
            if identical {
                // Check if the file with same filename already in staging area
                // If True, remove it
                List<String> stagedFiles = plainFilenamesIn(STAGE_DIR);
                if (stagedFiles.contains(filename)) {

                }
            }
        }
        // TODO
    }
    /** Commit staged files. */
    public void commit() {
        // TODO
    }
}
