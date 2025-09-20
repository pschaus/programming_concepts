package algorithms;

import java.util.*;
import java.util.function.Predicate;
// END STRIP

/**
 * (Proposed at the Exam of August 2025)
 * In this exercise, you have to implement a basic filesystem for a new operating system.
 *
 * A filesystem is a tree-based data structure, where each node corresponds to
 * one directory. The root node is the entry point of the filesystem.
 * Each directory can contain multiple subdirectories, as well as multiple files.
 */

public class FileSystem
{
    /**
     * Class that represents one file on the filesystem. A
     * file is characterized by a name (e.g., "Hello.txt") and by a
     * size (expressed in bytes).
     */
    static class File {
        private final String name;
        private final int size;

        /**
         * Constructs a file with the given name and size.
         */
        public File(String name, int size) {
            this.name = name;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public int getSize() {
            return size;
        }
    }


    /**
     * Class that represents one directory on the filesystem. A
     * directory is characterized by a name, by its subdirectories,
     * and by the files it stores.
     */
    static class Directory implements Iterable<File> {

        // TODO: Add the member variables you need here

        // BEGIN STRIP
        private String name; // name of the directory
        private List<File> files; // list of the files in this directory
        private List<Directory> subDirectories; // list of subdirectories
        // END STRIP

        /**
         * Constructs a new Directory with the given name.
         * The directory is initially empty, with no files or subdirectories.
         *
         * @param name the name of the directory
         */
        public Directory(String name) {
            // TODO: constructor
            // BEGIN STRIP
            this.name = name;
            this.files = new ArrayList<>();
            this.subDirectories = new ArrayList<>();
            // END STRIP
        }

        /**
         * Returns the name of this directory.
         */
        public String getName() {
            // TODO
            // STUDENT return null;
            // BEGIN STRIP
            return name;
            // END STRIP
        }

        /**
         * Add a new file to this directory.
         *
         * @param file the file to be added
         */
        public void addFile(File file) {
            // TODO
            // BEGIN STRIP
            files.add(file);
            // END STRIP
        }

        /**
         * Add a new subdirectory to this directory.
         *
         * @param directory the subdirectory to be added
         */
        public void addDirectory(Directory directory) {
            // TODO
            // BEGIN STRIP
            subDirectories.add(directory);
            // END STRIP
        }

        /**
         * Returns the total size of all files in this directory and its subdirectories.
         *
         * @return the total size (expressed in bytes)
         */
        public int getTotalSize() {
            // TODO
            // STUDENT return -1;
            // BEGIN STRIP
            int totalSize = 0;

            for (File file : files) {
                totalSize += file.getSize();
            }

            // Recursively add the sizes of all subdirectories
            for (Directory directory : subDirectories) {
                totalSize += directory.getTotalSize();
            }

            return totalSize;
            // END STRIP
        }

        // BEGIN STRIP
        protected List<File> getAllFiles() {
            List<File> allFiles = new ArrayList<>(files);
            for (Directory directory : subDirectories) {
                allFiles.addAll(directory.getAllFiles());
            }
            return allFiles;

        }
        // END STRIP

        /**
         * Returns an iterator over all the files in the Directory,
         * including files in its subdirectories. The order of the files is arbitrary.
         *
         * The FileSystem is assumed not be modified while iterating over the files:
         * There is thus no need to worry about ConcurrentModificationException.
         *
         * @return the iterator over all the files
         */
        @Override
        public Iterator<File> iterator() {
            // TODO
            // STUDENT return null;
            // BEGIN STRIP
            return new FileIterator(this, f -> true);
            // END STRIP
        }

        /**
         * Returns an iterator over the files in the Directory that match the given filter,
         * including files in its subdirectories. The order of the files is arbitrary.
         *
         * The FileSystem is assumed not be modified while iterating over the files:
         * There is thus no need to worry about ConcurrentModificationException.
         *
         * @param filter a predicate to filter the files of interest
         * @return the iterator over the filtered files
         */
        public Iterator<File> iterator(Predicate<File> filter) {
            // TODO
            // STUDENT return null;
            // BEGIN STRIP
            return new FileIterator(this, filter);
            // END STRIP
        }
    }

    // BEGIN STRIP
    static class FileIterator implements Iterator<File> {
        private Deque<File> fileStack;
        private Predicate<File> filter;

        public FileIterator(Directory root, Predicate<File> filter) {
            this.fileStack = new ArrayDeque<File>();
            this.filter = filter;
            pushAllFiles(root);
        }

        private void pushAllFiles(Directory directory) {
            List<File> allFiles = directory.getAllFiles();
            for (File file : allFiles) {
                if (filter == null || filter.test(file)) {
                    fileStack.push(file);
                }
            }
        }

        @Override
        public boolean hasNext() {
            return !fileStack.isEmpty();
        }

        @Override
        public File next() {
            return fileStack.pop();
        }

    }
    // END STRIP
}
