package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import static gitlet.Utils.join;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add method about saving and loading master.
     * TODO: modify Commit to make it only save filename and hashcode of blobs. Contents of files are not included.
     *     Maybe use a treeMap?
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
    private static String head;
    /** Point to master branch. */
    private static String master;
    /** The message of this Commit. */
    private String message;
    /** The timestamp of this Commit. */
    private String time;
    /** Use treeMap to store filename-hash pairs in sorted order. */
    public Map<String, String> filenameHashPairs;
    /** Hash code of commit. */
    private String hash;
    /** Pointer to its parent commit. The hash code of parent commit, i.e. where head points to last time. */
    private String parent;

    /** Launcher of Commit class. */
    public Commit(String M, Map<String, String> trackedPairs) {
        time = getTime();
        message = M;
        parent = head;
        filenameHashPairs = trackedPairs;
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
        message = "initial commit";
        filenameHashPairs = new TreeMap<>();
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
        if (!savePath.exists()) {
            System.out.print("No commit with that id exists.");
            System.exit(0);
        }
        Commit loaded =  Utils.readObject(savePath, Commit.class);
        loaded.loadHead();
        return loaded;
    }
    /** Load a saved commit according to filename. */
    public static Commit loadByFilename(String filename) {
        File savePath = join(COMMITS_DIR, filename);
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
    public Map<String, String> getFilenameHashPairs() {
        return filenameHashPairs;
    }
    /** Get string hash of head commit. */
    public static String getHead() {
        loadHead();
        return head;
    }
    public String getParent() {
        return parent;
    }
    public String getMessage() {
        return message;
    }
    public String getHash() {
        return hash;
    }
    public String getMaster() {
        // TODO: load and update master from saved master file
        return master;
    }
    /** Print information of this commit. Used in log command. */
    public void print() {
        Formatter formatter = new Formatter();
        String localTime = convertToCurrentTimezone(time);
        // Caution: Do not forget the colon after Date
        formatter.format("===\ncommit %s\nDate: %s\n%s\n", hash, localTime, message);
        System.out.println(formatter);
        formatter.close();
    }
    /** Get system time and format it like "00:00:00 UTC, Thursday, 1 January 1970". */
    private static String getTime() {
        Instant currentInstant = Instant.now();
        ZoneId utcZone = ZoneId.of("UTC");
        ZonedDateTime utcDateTime = ZonedDateTime.ofInstant(currentInstant, utcZone);

        // Format the time
        // Use Locale.ENGLISH to prevent errors parsing different language
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss z, EEEE, d MMMM yyyy", Locale.ENGLISH);
        String formattedTime = utcDateTime.format(formatter);
        return formattedTime;
    }
    private static String convertToCurrentTimezone(String utcDatetime) {
        // Use Locale.ENGLISH to prevent errors parsing different language
        // Parse the UTC time
//        System.out.println(utcDatetime);  // Debug
        DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("HH:mm:ss z, EEEE, d MMMM yyyy", Locale.ENGLISH);
        ZonedDateTime utcDateTime = ZonedDateTime.parse(utcDatetime, utcFormatter);

        // Convert to the system's default time zone
        ZoneId systemZone = ZoneId.systemDefault();
        ZonedDateTime systemDateTime = utcDateTime.withZoneSameInstant(systemZone);

        // Format the time
        DateTimeFormatter localTimeFormatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss yyyy Z", Locale.ENGLISH);
        return systemDateTime.format(localTimeFormatter);
    }

    /** Saved contents of a single file. Will be removed if un-staged. */
    public static class blob implements Serializable {
        /** Location of the saved serialized file. */
        private String filename;
        /** Hash of the file. */
        private String hash;
        /** Contents of the file. */
        private byte[] contents;
        public static final File BLOBS_DIR = Utils.join(Repository.GITLET_DIR, "blobs");

        /** Launcher of blob.
         *
         * @param fileToBeAdded A single full path of staged file.
         * */
        public blob(File fileToBeAdded, String name) {
            contents = Utils.readContents(fileToBeAdded);
            filename = name;
            hash = hash();
        }
        /** Return contents of file. */
        public byte[] getContents() {
            return contents;
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
        public static blob load(String hashcode) {
            File savePath = locate(hashcode);
            if (!savePath.exists()) {
                System.out.print("No blob of that hash exists.");
                System.exit(0);
            }
            blob loaded =  Utils.readObject(savePath, blob.class);
            return loaded;
        }
        public File locate() {
            return locate(BLOBS_DIR, hash);
        }
        public File locateParentDir() {
            return locateParentDir(BLOBS_DIR, hash);
        }
        /** Get the save location of this object according to its hash code. */
        public static File locate(String hashcode) {
            return locate(BLOBS_DIR, hashcode);
        }
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
}
