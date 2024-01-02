package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.util.Date; // TODO: You'll likely use this in this class
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
    /** The objects directory where blobs and commits are saved. */
    public static final File OBJECTS_DIR = join(Repository.GITLET_DIR, "objects");
    /** The message of this Commit. */
    private String message;
    /** The timestamp of this Commit. */
    private String time;

    /* TODO: fill in the rest of this class. */

    public Commit(String message) {
        time = getTime();
        // Files to commit that are in stage area.
        // TODO: rest part of commit launcher.
    }
    /** Saved contents of a single file. Will be removed if un-staged. */
    private class blob extends Object{
        /** Location of the saved serialized file. */
        public File location;
        /** Contents of the file. */
        public File contents;
        /** Hash of the file. */
        public String hash;

        /** Launcher of blob. */
        public blob(File stagedFile) {
            contents = stagedFile;
            hash = Utils.sha1(contents);
        }

        /** Serialize and save this blob. */
        public void save() {
            // TODO: serialize blob.

            // TODO: save blob.
        }
        /** Get the save location of this object according to its hash code. */
        private File locate() {
            // Get the first two characters of hash as the parent folder of saved blob.
            String parent_folder = hash.substring(0, 1);
            // Get the substring of hash without first two characters as the save name of blob.
            String save_name = hash.substring(2);
            return join(OBJECTS_DIR, parent_folder, save_name);
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
