package com.anton.buffer.text;

import com.anton.BufferIOException;

import java.io.*;

public class FileBufferText extends ContinuousBufferText {

    private String fileName;
    private int size;

    public FileBufferText(int bufferSize, String fileName) throws BufferIOException {
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
    public int getSize(){
        return size;
    }

    @Override
    protected char[] fetch() throws BufferIOException {
        char[] data = new char[size];
        try(Reader reader = new FileReader(fileName)){
            reader.read(data);
        } catch (IOException e) {
            throw new BufferIOException(e);
        }
        return data;
    }

    @Override
    protected void stash(char[] data) throws BufferIOException {
        try(Writer writer = new FileWriter(fileName)){
            writer.write(data);
        } catch (IOException e) {
            throw new BufferIOException(e);
        }
    }

    @Override
    public void close(){
        File f = new File(fileName);
        f.delete();
    }
}
