package com.anton;

/**
 * The buffer in overloaded
 */
public class BufferOverflowException extends BufferException {

    private int bytesRemain;
    private int bytesRequire;

    public BufferOverflowException(int bytesRemain, int bytesRequire) {
        super(bytesRemain + " bytes remain, " + bytesRequire + " bytes require");
        this.bytesRemain = bytesRemain;
        this.bytesRequire = bytesRequire;
    }

    public BufferOverflowException(int bytesRemain, int bytesRequire, Throwable cause) {
        super(bytesRemain + " bytes remain, " + bytesRequire + " bytes require in internal buffer", cause);
        this.bytesRemain = bytesRemain;
        this.bytesRequire = bytesRequire;
    }

    public int getBytesRemain() {
        return bytesRemain;
    }

    public int getBytesRequire() {
        return bytesRequire;
    }
}
