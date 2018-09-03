package com.anton.exceptions;

import java.io.IOException;

public class BufferIOException extends BufferException {
    public BufferIOException(IOException cause) {
        super("buffer I/O exception", cause);
    }
}
