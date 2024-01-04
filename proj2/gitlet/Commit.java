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
     * TODO: add method to saving and loading master.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    /** The objects directory where commits are saved. */
    public static final File COMMITS_DIR = Utils.join(Repository.GITLET_DIR, "commits");
    /** Location where head pointer is saved. A path of head file. */
    public static final File HEAD_PATH = Utils.join(Repository.GITLET_DIR, "head");
    /** Point to the position of user. */
    public static String head;
    /** Point to master branch. */
    public static String master;
    /** The message of this Commit. */
    private String message;
    /** The timestamp of this Commit. */
    private String time;
    /** Blobs in this commit. */
    public List<blob> blobs;
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
        blobs = trackedBlobs;
        hash = hash();
        head = hash;
        // TODO: Move master pointer in some situations.
        this.save();
        saveHead();
    }
    /** Initial commit, message and filenames are null.
     *  Since head and master are both static fields, there will be no problem calling hash method within constructor.
     * */
    public Commit() {
        time = "00:00:00 UTC, Thursday, 1 January 1970";
        hash = hash();
        head = hash;
        master = hash;
        this.save();
        saveHead();
    }
    /** Load a saved commit according to a given hash code. */
    public static Commit load(String hashcode) {
        // Get location
        File savePath = blob.locate(COMMITS_DIR, hashcode);
        Commit loaded =  Utils.readObject(savePath, Commit.class);
        loaded.loadHead();
        return loaded;
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
    private void save() {
        File savePath = locate();
        // Create parent directory whose name is the first 2 characters of hash code.
        File parentDir = locateParentDir();
        parentDir.mkdir();
        // Create empty file before writing.
        if (!savePath.exists()) {
            try {
                savePath.createNewFile();
            } catch (IOException e) {
                // TODO: use a better way to handle this.
                throw Utils.error("IOException");
            }
        }
        // Write serialized Commit.
        Utils.writeObject(savePath, this);
        // Save static fields
        saveHead();
    }
    /** Create or overwrite head pointer. */
    private static void saveHead() {
        if (!HEAD_PATH.exists()) {
            try {
            HEAD_PATH.createNewFile();
            } catch (IOException e) {
                // TODO: use a better way to handle this.
                throw Utils.error("IOException");
            }
        }
        Utils.writeContents(HEAD_PATH, head);
    }
    private static void loadHead() {
        head = Utils.readContentsAsString(HEAD_PATH);
    }
    /** Reuse code from blob class to get location from hash code. */
    public File locate() {
        return blob.locate(COMMITS_DIR, hash);
    }
    /** Reuse code from blob class to get path of parent directory where commit is saved from hash code. */
    public File locateParentDir() {
        return blob.locateParentDir(COMMITS_DIR, hash);
    }
    public boolean equals(Commit obj) {
        return this.hash() == obj.hash();
    }
    public List<blob> getBlobs() {
        return blobs;
    }
    /** Get string hash of head commit. */
    public static String getHead() {
        loadHead();
        return head;
    }

    /** Saved contents of a single file. Will be removed if un-staged. */
    public static class blob implements Serializable {
        /** Location of the saved serialized file. */
        public String filename;
        /** Hash of the file. */
        public String hash;
        public static final File BLOBS_DIR = Utils.join(Repository.GITLET_DIR, "blobs");

        /** Launcher of blob.
         *
         * @param fileToBeAdded A single full path of staged file.
         * */
        public blob(File fileToBeAdded, String name) {
            byte[] contents = Utils.readContents(fileToBeAdded);
            filename = name;
            hash = hash();
        }
        /** Return hash field. */
        public String getHash() {
            return hash;
        }
        public String getFilename() {
            return filename;
        }
        /** Save this blob. */
        public void save() {
            File savePath = locate();
            File parentDir = locateParentDir();
            parentDir.mkdir();
            if (!savePath.exists()) {
                try {
                    savePath.createNewFile();
                } catch (IOException e) {
                    // Handle situations that file already exists.
                    // TODO: use a better way to handle this.
                    throw Utils.error("IOException");
                }
            }
            Utils.writeObject(savePath, this);
        }
        public File locate() {
            return locate(BLOBS_DIR, hash);
        }
        public File locateParentDir() {
            return locateParentDir(BLOBS_DIR, hash);
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
            // Serialize this to make sure it's a byte array.
            byte[] serializedBlob = Utils.serialize(this);
            return Utils.sha1(serializedBlob);
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
