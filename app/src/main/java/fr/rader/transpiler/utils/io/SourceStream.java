package fr.rader.transpiler.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SourceStream extends FileInputStream {

    private static final byte BUFFER_SIZE = 3;

    // data[0] -> previous character
    // data[1] -> current character
    // data[2] -> next character
    private final char[] data = new char[BUFFER_SIZE];

    private final long sourceSize;

    private long offset = 0;

    public static final int PREVIOUS = 0;
    public static final int CURRENT = 1;
    public static final int NEXT = 2;

    public SourceStream(File file) throws IOException {
        super(file);

        this.sourceSize = file.length();

        this.initData();
    }

    public char next() throws IOException {
        shiftData();

        return peek(CURRENT);
    }

    public char peek(int index) {
        if (index > NEXT || index < PREVIOUS) {
            throw new IllegalStateException("index must be between 0 inclusive and 2 inclusive");
        }

        return data[index];
    }

    public void skip() throws IOException {
        shiftData();
    }

    public boolean hasNext() {
        return offset < sourceSize;
    }

    private void initData() throws IOException {
        data[NEXT] = fetchChar();
    }

    private void shiftData() throws IOException {
        data[PREVIOUS] = data[CURRENT];
        data[CURRENT] = data[NEXT];
        data[NEXT] = fetchChar();

        offset++;
    }

    private char fetchChar() throws IOException {
        int value = read();
 
        return (value == -1) ? 0 : (char) value;
    }
}
