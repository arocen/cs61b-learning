package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.nio.file.*;

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
        STAGE_DIR.mkdir();
        Commit.COMMITS_DIR.mkdir();
        Commit.blob.BLOBS_DIR.mkdir();
        // Create initial commit
        Commit initCommit = new Commit();
    }
    /** Add files to staging area.
     *  The staging area is in 'stage' folder of '.gitlet'.
     *  If the version of file added is identical to that in current version, do not stage it to be added,
     *  and remove it from the staging area if it is already there.
     *
     *  In Gitlet, only one file may be added at a time.
     *
     *  @param filename Name of the single file to be added. */
    public static void add(String filename) {
        File path = join(CWD, filename);
        // If the file to be added does not exist, raise error.
        if (!path.exists()) {
            System.out.print("File does not exist.");
            System.exit(0);
        }

        // Use filename instead of hashcode to save files in staging area
        // Instantiate a blob from file to be added
        Commit.blob fileToBeAdded = new Commit.blob(path, filename);
        // Load current commit
        String currentCommitHash = Commit.getHead();
        Commit current = Commit.load(currentCommitHash);
        Map<String, String> curPairs = current.getFilenameHashPairs();

        // If this file exists in current commit, and is identical(with same hashcode), abort
        if (curPairs != null && curPairs.get(filename) != null) {
            if (curPairs.get(filename) == fileToBeAdded.getHash()) {
                // Check if the file with same filename already in staging area.
                // If True, remove that staged file.
                if (plainFilenamesIn(STAGE_DIR).contains(filename)) {
                    File stagedFile = join(STAGE_DIR, filename);
                    stagedFile.delete();
                }
                System.exit(0);
            }
        }
        // Else add the newest version to staging area(a copy of raw file)
        // Warning: ensure stage folder already created with init command, else copy operation raises error.
        try {
            Files.copy(path.toPath(), join(STAGE_DIR, filename).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw error("Error copying file to staging area.");
        }
    }
    /** Commit staged files. */
    public static void commit(String M) {
        // Get blobs from previous commit
        String currentCommitHash = Commit.getHead();
        // Modifications to this de-serialized object does not affect previous commit.
        Commit current = Commit.load(currentCommitHash);
        Map<String, String> curPairs = current.getFilenameHashPairs();

        // Compare files in current commits to staged files
        List<String> stagedFiles = plainFilenamesIn(STAGE_DIR);
        if (stagedFiles == null) {
            System.out.print("No changes added to the commit.");
            System.exit(0);
        }
        if (curPairs != null) {
            for (String checkedFilename : stagedFiles) {
                // Update current committed files with staged files having same name, and add new tracked files.
                Commit.blob updatedFile = new Commit.blob(join(STAGE_DIR, checkedFilename), checkedFilename);
                // Save these files as new blobs
                updatedFile.save();
                // put method can create or overwrite a pair
                curPairs.put(checkedFilename, updatedFile.getHash());
            }
        }
        // Create new commit, automatically saved
        Commit newest = new Commit(M, curPairs);

        // Clear all files in staging area after commit
        for (String P : plainFilenamesIn(STAGE_DIR)) {
            join(STAGE_DIR, P).delete();
        }
    }
    /** Create or overwrite file in CWD with the file of same name which is in head commit.
     *  The new version of the file is not staged. */
    public static void checkout(String filename) {
        checkout(Commit.getHead(), filename);
    }
    /** Create or overwrite file in CWD with the file of same name which is in commit with given hash.
     *  The new version of the file is not staged. */
    public static void checkout(String commitID, String filename) {
        // Load commit with given ID. Exception will be thrown during loading if no commit with that id exists.
        Commit checkoutCom = Commit.load(commitID);
        Map<String, String> checkoutPairs = checkoutCom.getFilenameHashPairs();
        // Check if given filename is in keys of headPairs.
        if (checkoutPairs == null || !checkoutPairs.containsKey(filename)) {
            System.out.print("File does not exist in that commit.");
            System.exit(0);
        }
        // Get file location with that name in head commit
        File source = Commit.blob.locate(checkoutPairs.get(filename));
        // Overwrite file to version in head commit
        try {
            Files.copy(source.toPath(), join(CWD, filename).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
