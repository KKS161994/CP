import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Resumable file iterator, translated from Python's resumable_iterator.py
 * (AsyncResumableFileIterator).
 *
 * The Python version walked a list of file paths line-by-line and could
 * checkpoint its position via get_state()/set_state() — a dict of
 * {file_index, offset}. After restoring that state on a fresh iterator, it
 * resumed exactly where it left off.
 *
 * NOTE: Python's async aiofiles + __aiter__/__anext__ are modeled here as a
 * synchronous java.util.Iterator whose resume position is held explicitly in
 * two fields (fileIndex, offset). seek(offset) is done with a RandomAccessFile
 * so the (file_index, offset) checkpoint restores the exact byte position.
 * The original Python source was incomplete (no __anext__, no __main__); the
 * iteration/next() logic was completed faithfully from get_state/set_state.
 */
public class ResumableIterator implements java.util.Iterator<String> {

    private final String[] filePaths;
    private int fileIndex = 0;
    private long offset = 0;
    private RandomAccessFile currentFile = null;
    private String pending = null; // line read by hasNext(), consumed by next()

    public ResumableIterator(String[] filePaths) {
        this.filePaths = filePaths;
    }

    public void open() throws IOException {
        openCurrentFile();
    }

    private void openCurrentFile() throws IOException {
        if (currentFile != null) {
            currentFile.close();
            currentFile = null;
        }
        if (fileIndex >= filePaths.length) {
            return;
        }
        currentFile = new RandomAccessFile(filePaths[fileIndex], "r");
        currentFile.seek(offset);
    }

    @Override
    public boolean hasNext() {
        if (pending != null) {
            return true;
        }
        try {
            pending = readNextLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pending != null;
    }

    @Override
    public String next() {
        if (!hasNext()) {
            throw new NoSuchElementException(); // StopAsyncIteration analog
        }
        String line = pending;
        pending = null;
        return line;
    }

    // Advances across files, keeping fileIndex/offset checkpoint up to date.
    private String readNextLine() throws IOException {
        while (true) {
            if (currentFile == null) {
                if (fileIndex >= filePaths.length) {
                    return null;
                }
                openCurrentFile();
                if (currentFile == null) {
                    return null;
                }
            }
            String line = currentFile.readLine();
            if (line != null) {
                offset = currentFile.getFilePointer();
                return line;
            }
            // Exhausted this file; advance to the next one.
            currentFile.close();
            currentFile = null;
            fileIndex++;
            offset = 0;
            if (fileIndex >= filePaths.length) {
                return null;
            }
        }
    }

    public Map<String, Long> getState() {
        Map<String, Long> state = new HashMap<>();
        state.put("file_index", (long) fileIndex);
        state.put("offset", offset);
        return state;
    }

    public void setState(Map<String, Long> state) throws IOException {
        this.fileIndex = state.get("file_index").intValue();
        this.offset = state.get("offset");
        this.pending = null;
        openCurrentFile();
    }

    public void close() throws IOException {
        if (currentFile != null) {
            currentFile.close();
            currentFile = null;
        }
    }

    // Mirrors the Python AsyncResumableFileIterator.main() demo.
    public static void main(String[] args) throws IOException {
        ResumableIterator iterator = new ResumableIterator(new String[]{"a.txt", "b.txt"});
        iterator.open();

        if (iterator.hasNext()) {
            String line = iterator.next();
            System.out.println(line);
        }

        Map<String, Long> state = iterator.getState();
        iterator.close();

        ResumableIterator iterator2 = new ResumableIterator(new String[]{"a.txt", "b.txt"});
        iterator2.setState(state);

        while (iterator2.hasNext()) {
            System.out.println(iterator2.next());
        }
        iterator2.close();
    }
}
