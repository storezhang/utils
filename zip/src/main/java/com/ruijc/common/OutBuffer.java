package com.ruijc.common;

import java.io.IOException;
import java.io.OutputStream;

public class OutBuffer {
    private byte[] _buffer;
    private int _pos;
    private int _limitPos;
    private int _streamPos;
    private int _bufferSize;
    private OutputStream _stream;
    private long _processedSize;

    public void setStream(OutputStream _stream) {
        this._stream = _stream;
    }

    public void create(int bufferSize) {
        int kMinBlockSize = 1;
        if (bufferSize < kMinBlockSize)
            bufferSize = kMinBlockSize;
        if (_buffer != null && _bufferSize == bufferSize)
            return;
        _bufferSize = bufferSize;
        _buffer = new byte[bufferSize];
    }

    public void init() {
        _streamPos = 0;
        _limitPos = _bufferSize;
        _pos = 0;
        _processedSize = 0;
    }

    public long getProcessedSize() {
        long res = _processedSize + _pos - _streamPos;
        if (_streamPos > _pos)
            res += _bufferSize;
        return res;
    }

    public void writeByte(Byte b) throws IOException {
        _buffer[_pos++] = b;
        if (_pos == _limitPos)
            flushWithCheck();
    }

    public void writeBytes(byte[] data, int size) throws IOException {
        for (int i = 0; i < size; i++)
            writeByte(data[i]);
    }

    private void flushPart() throws IOException {
        int size = (_streamPos >= _pos) ? (_bufferSize - _streamPos) : (_pos - _streamPos);

        if (_stream != null) {
            _stream.write(_buffer, _streamPos, size);
        }
        _streamPos += size;
        if (_streamPos == _bufferSize)
            _streamPos = 0;
        if (_pos == _bufferSize) {
            _pos = 0;
        }
        _limitPos = (_streamPos > _pos) ? _streamPos : _bufferSize;
        _processedSize += size;
    }

    public void flush() throws IOException {
        while (_streamPos != _pos) {
            flushPart();
        }
    }

    private void flushWithCheck() throws IOException {
        flush();
    }
}
