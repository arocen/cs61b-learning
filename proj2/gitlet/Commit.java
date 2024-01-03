package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static gitlet.Utils.join;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    /** The objects directory where commits are saved. */
    public static final File COMMITS_DIR = Utils.join(Repository.GITLET_DIR, "commits");
    /** Point to the position of user. */
    public static String head;
    /** Point to master branch. */
    public static String master;
    /** The message of this Commit. */
    private String message;
    /** The timestamp of this Commit. */
    private String time;
    /** Hash codes of blobs, in the order of filenames. */
    private List<blob> blobs;
    /** Hash code of commit. */
    public String hash;
    /** Pointer to its parent commit. The hash code of parent commit, i.e. where head points to last time. */
    private String parent;

    /** Launcher of Commit class.
     *
     * @param M
     * @param trackedBlobs By default, the blobs tracked by its parent commit.
     */
    public Commit(String M, List<blob> trackedBlobs) {
        time = getTime();
        message = M;
        parent = head;
        // Load parent commit and get its blobs as default value.
        Commit parentCommit = load(parent);
        // TODO: Figure out how to make this compatible to add command.
        blobs = parentCommit.blobs;
//        // Do not update this commit to commit tree(i.e. save it) if tracked blobs(a list) equal to those of parent commit.
//        if (blobs.equals(trackedBlobs)) {
//            System.exit(0);
//        }
        blobs = trackedBlobs;
        hash = this.hash();
        head = hash;
        // TODO: Move master pointer in some situations.
    }
    /** Initial commit, message and filenames are null.
     *  Since head and master are both static fields, there will be no problem calling hash method within constructor.
     * */
    public Commit() {
        time = "00:00:00 UTC, Thursday, 1 January 1970";
        hash = this.hash();
        head = hash;
        master = hash;
    }
    /** Load a saved commit according to a given hash code. */
    public static Commit load(String hashcode) {
        // Get location
        File savePath = blob.locate(COMMITS_DIR, hashcode);
        return Utils.readObject(savePath, Commit.class);
    }
    /** Hash commit. Static fields are not hashed. */
    private String hash() {
        // Serialize this to make sure it's a byte array.
        byte[] serializedCommit = Utils.serialize(this);
        return Utils.sha1(serializedCommit);
    }
    /**
     * Save commit. Create an empty file if not exist before writing.
     * Throw an exception if file already exists.
     * */
    public void save() {
        File savePath = locate();
        // Create parent directory whose name is the first 2 characters of hash code.
        File parentDir = locateParentDir();
        parentDir.mkdir();
        // Create empty file before writing.
        if (!savePath.exists()) {
            try {
                savePath.createNewFile();
            } catch (IOException e) {
                // Handle situations that file already exists.
                // TODO: use a better way to handle this.
                throw Utils.error("Commit file already exists.");
            }
        }
        // Write serialized Commit.
        Utils.writeObject(savePath, this);
    }
    /** Reuse code from blob class to get location from hash code. */
    public File locate() {
        return blob.locate(COMMITS_DIR, this.hash);
    }
    /** Reuse code from blob class to get path of parent directory where commit is saved from hash code. */
    public File locateParentDir() {
        return blob.locateParentDir(COMMITS_DIR, this.hash);
    }
    public boolean equals(Commit obj) {
        return this.hash() == obj.hash();
    }

    /** Saved contents of a single file. Will be removed if un-staged. */
    public class blob implements Serializable {
        /** Location of the saved serialized file. */
        public String filename;
        /** Hash of the file. */
        public String hash;
        public static final File BLOBS_DIR = Utils.join(Repository.GITLET_DIR, "blobs");

        /** Launcher of blob.
         *
         * @param stagedFile A single full path of staged file.
         * */
        public blob(File stagedFile, String name) {
            byte[] contents = Utils.readContents(stagedFile);
            filename = name;
            hash = this.hash();
        }

        /** Save this blob. */
        public void save() {
            // TODO: fix bugs
            Utils.writeObject(this.locate(), this);
        }
        public File locate() {
            return locate(BLOBS_DIR, this.hash);
        }
        /** Get the save location of this object according to its hash code. */
        public static File locate(File grandParentDir, String hashcode) {
            // Get the first two characters of hash as the parent folder of saved blob.
            String parent_folder = hashcode.substring(0, 2);
            // Get the substring of hash without first two characters as the save name of blob.
            String save_name = hashcode.substring(2);
            return Utils.join(grandParentDir, parent_folder, save_name);
        }
        /** Get path of parent directory whose name is the first 2 characters of hash code. */
        public static File locateParentDir(File grandParentDir, String hashcode) {
            String parent_folder = hashcode.substring(0, 2);
            return Utils.join(grandParentDir, parent_folder);
        }
        public boolean equals(blob obj) {
            return this.hash() == obj.hash();
        }
        public String hash(){
            return Utils.sha1(this);
        }
    }
    /** Get system time and format it like "00:00:00 UTC, Thursday, 1 January 1970". */
    private static String getTime() {
        Instant currentInstant = Instant.now();
        ZoneId utcZone = ZoneId.of("UTC");
        ZonedDateTime utcDateTime = ZonedDateTime.ofInstant(currentInstant, utcZone);

        // Format the time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss 'UTC, 'EEEE, d MMMM yyyy");
        String formattedTime = utcDateTime.format(formatter);
        return formattedTime;
    }
}
