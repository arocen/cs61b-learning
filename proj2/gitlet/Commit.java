package gitlet;

// TODO: any imports you need here

import java.io.File;
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
public class Commit {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    /** The objects directory where commits are saved. */
    public static final File COMMITS_DIR = Utils.join(Repository.GITLET_DIR, "commits");
    public static String head;
    /** The message of this Commit. */
    private String message;
    /** The timestamp of this Commit. */
    private String time;
    /** List of filenames tracked in this commit, in lexical order. */
    private List<String> filenames;
    /** Hash code of commit. */
    public String hash;

    /** Launcher of Commit class.
     *
     * @param M
     * @param trackedFilenames Filenames that have been tracked by using add command.
     */
    public Commit(String M, List<String> trackedFilenames) {
        time = getTime();
        filenames = trackedFilenames;
        message = M;
        // hash = Utils.sha1(this);
        // TODO: add references from filenames to blobs(hashcode). Move head pointer.
    }
    /** Initial commit, message and filenames are null.*/
    public Commit() {
        time = "00:00:00 UTC, Thursday, 1 January 1970";
    }
    /** Hash commit. */

    /** Save commit. */

    /** Saved contents of a single file. Will be removed if un-staged. */
    private class blob implements Serializable {
        /** Location of the saved serialized file. */
        public File location;
        /** Hash of the file. */
        public String hash;
        public static final File BLOBS_DIR = Utils.join(Repository.GITLET_DIR, "blobs");

        /** Launcher of blob. */
        public blob(String stagedFilename) {
            File filePath = Utils.join(stageDir, stagedFilename);
            byte[] contents = Utils.readContents(filePath);
            hash = Utils.sha1(contents);
        }

        /** Save this blob. */
        private void save() {
            Utils.writeObject(this.locate(), this);
        }
        /** Get the save location of this object according to its hash code. */
        private File locate() {
            // Get the first two characters of hash as the parent folder of saved blob.
            String parent_folder = hash.substring(0, 1);
            // Get the substring of hash without first two characters as the save name of blob.
            String save_name = hash.substring(2);
            return Utils.join(BLOBS_DIR, parent_folder, save_name);
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
