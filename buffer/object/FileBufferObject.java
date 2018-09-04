package com.anton.buffer.object;

import com.anton.exceptions.BufferIOException;

import java.io.*;

public class FileBufferObject<T> extends ContinuousBufferObject<T> {

    private String fileName;
    private int size;

    public FileBufferObject(int bufferSize, String fileName) throws BufferIOException {
        this.size = bufferSize;
        this.fileName = fileName;
        File f = new File(this.fileName);
        try {
            f.createNewFile();
        } catch (IOException e) {
            throw new BufferIOException(e);
        }
    }

    @Override
    byte[] fetch() throws BufferIOException {
        byte[] data = new byte[size];
        try(InputStream inputStream = new FileInputStream(fileName)){
            inputStream.read(data);
        } catch (IOException e) {
            throw new BufferIOException(e);
        }
        return data;
    }

    @Override
    void stash(byte[] data) throws BufferIOException {
        try(OutputStream fileOutputStream = new FileOutputStream(fileName)){
            fileOutputStream.write(data);
        } catch (IOException e) {
            throw new BufferIOException(e);
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void close() {
        File f = new File(fileName);
        f.delete();
    }
}
