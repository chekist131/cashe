package com.anton.buffer;

import com.anton.BufferIOException;

import java.io.*;

public class FileBuffer<T extends Serializable, Key extends Comparable<? super Key>> extends ContinuousBuffer<T, Key> {

    private RandomAccessFile randomAccessFile;
    private File file;

    private int size;

    public FileBuffer(int bufferSize, String fileName) throws BufferIOException {
        this.size = bufferSize;
        file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new BufferIOException(e);
            }
        }
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            throw new BufferIOException(e);
        }
    }

    @Override
    byte[] fetch() throws BufferIOException {
        byte[] data = new byte[size];
        try{
            randomAccessFile.seek(0);
            randomAccessFile.read(data);
        } catch (IOException e) {
            throw new BufferIOException(e);
        }
        return data;
    }

    @Override
    void stash(byte[] data) throws BufferIOException {
        try{
            randomAccessFile.seek(0);
            randomAccessFile.write(data);
        } catch (IOException e) {
            throw new BufferIOException(e);
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void close() throws BufferIOException{
        try {
            randomAccessFile.close();
        } catch (IOException e) {
            throw new BufferIOException(e);
        }
        file.delete();
    }
}
