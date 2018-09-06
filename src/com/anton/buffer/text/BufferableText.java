package com.anton.buffer.text;

import com.anton.buffer.Bufferable;

interface BufferableText<Key extends Comparable<? super Key>> extends Bufferable<String, Key> {
}
