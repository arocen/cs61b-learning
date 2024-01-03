package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
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
        String currentCommitHash = Commit.head;
        Commit current = Commit.load(currentCommitHash);
        List<Commit.blob> curBlobs = current.blobs;

        // If this file exists in current commit, and is identical(with same hashcode)
        List<String> curHashes = curBlobs.stream().map(Commit.blob::hash).collect(Collectors.toList());
        if (curHashes.contains(fileToBeAdded)) {
            // Check if the file with same filename already in staging area
            if (plainFilenamesIn(STAGE_DIR).contains(filename)) {
                // If True, remove that staged file
                File stagedFile = join(STAGE_DIR, filename);
                stagedFile.delete();
            }
        }
        // Else add the newest version to staging area(a copy of raw file)
        try {
            Files.copy(path.toPath(), join(STAGE_DIR, filename).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw error("Error copying file to staging area.");
        }
    }
    /** Commit staged files. */
    public static void commit() {
        // TODO
        // TODO: Clear all files in staging area after commit
    }
}
