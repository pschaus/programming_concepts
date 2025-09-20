package algorithms;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Grade
public class FileSystemTest {


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testAddFile() {
        FileSystem.Directory root = new FileSystem.Directory("root");
        FileSystem.File file1 = new FileSystem.File("file1", 100);
        FileSystem.File file2 = new FileSystem.File("file2", 200);

        root.addFile(file1);
        assertEquals(100, root.getTotalSize());

        root.addFile(file2);
        assertEquals(300, root.getTotalSize());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testAddDirectory() {
        FileSystem.Directory root = new FileSystem.Directory("root");
        FileSystem.Directory subDir1 = new FileSystem.Directory("subDir1");
        FileSystem.File file1 = new FileSystem.File("file1", 100);
        FileSystem.File file2 = new FileSystem.File("file2", 200);

        root.addDirectory(subDir1);
        subDir1.addFile(file1);
        assertEquals(100, root.getTotalSize());

        subDir1.addFile(file2);
        assertEquals(300, root.getTotalSize());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testGetTotalSize() {
        FileSystem.Directory root = new FileSystem.Directory("root");
        FileSystem.Directory subDir1 = new FileSystem.Directory("subDir1");
        FileSystem.Directory subDir2 = new FileSystem.Directory("subDir2");
        FileSystem.File file1 = new FileSystem.File("file1", 100);
        FileSystem.File file2 = new FileSystem.File("file2", 200);
        FileSystem.File file3 = new FileSystem.File("file3", 300);

        root.addFile(file1);
        root.addDirectory(subDir1);
        subDir1.addFile(file2);
        subDir1.addDirectory(subDir2);
        subDir2.addFile(file3);

        assertEquals(600, root.getTotalSize());
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testIterator() {
        FileSystem.Directory root = new FileSystem.Directory("root");
        FileSystem.Directory subDir1 = new FileSystem.Directory("subDir1");
        FileSystem.Directory subDir2 = new FileSystem.Directory("subDir2");
        FileSystem.File file1 = new FileSystem.File("file1", 100);
        FileSystem.File file2 = new FileSystem.File("file2", 200);
        FileSystem.File file3 = new FileSystem.File("file3", 300);

        root.addFile(file1);
        root.addDirectory(subDir1);
        subDir1.addFile(file2);
        subDir1.addDirectory(subDir2);
        subDir2.addFile(file3);

        Iterator<FileSystem.File> iterator = root.iterator();
        int totalSize = 0;
        while (iterator.hasNext()) {
            FileSystem.File file = iterator.next();
            totalSize += file.getSize();
        }
        assertEquals(600, totalSize);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testIteratorWithFilter() {
        FileSystem.Directory root = new FileSystem.Directory("root");
        FileSystem.Directory subDir1 = new FileSystem.Directory("subDir1");
        FileSystem.Directory subDir2 = new FileSystem.Directory("subDir2");
        FileSystem.File file1 = new FileSystem.File("file1", 100);
        FileSystem.File file2 = new FileSystem.File("file2", 200);
        FileSystem.File file3 = new FileSystem.File("file3", 300);

        root.addFile(file1);
        root.addDirectory(subDir1);
        subDir1.addFile(file2);
        subDir1.addDirectory(subDir2);
        subDir2.addFile(file3);

        Iterator<FileSystem.File> iterator = root.iterator(file -> file.getSize() > 100);
        int totalSize = 0;
        while (iterator.hasNext()) {
            FileSystem.File file = iterator.next();
            totalSize += file.getSize();
        }
        assertEquals(500, totalSize);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testEmptyIterator() {
        FileSystem.Directory root = new FileSystem.Directory("root");
        Iterator<FileSystem.File> iterator = root.iterator();
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }


    @Test
    @Grade(value = 3, cpuTimeout = 1000, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    public void testDeepIntegrated() {
        FileSystem.Directory root = new FileSystem.Directory("root");
        FileSystem.Directory current = root;
        current.addFile(new FileSystem.File("file_"+0, 0));
        int n = 200;
        for  (int i = 1; i < n; i++) {
            FileSystem.Directory subCurrent = new FileSystem.Directory("file_"+0);
            subCurrent.addFile(new FileSystem.File("file_i", i));
            current.addDirectory(subCurrent);
            current = subCurrent;
        }
        assertEquals(n*(n-1)/2, root.getTotalSize());

        for (int mod = 2; mod < 6; mod++) {
            int totSize = 0;
            int modulo = mod;
            Iterator<FileSystem.File> iterator = root.iterator(file -> file.getSize() % modulo == 0);
            while (iterator.hasNext()) {
                FileSystem.File file = iterator.next();
                totSize += file.getSize();
            }
            int expected = IntStream.range(0, n).filter(i -> i % modulo == 0).sum();
            assertEquals(expected, totSize);
        }
    }


}
