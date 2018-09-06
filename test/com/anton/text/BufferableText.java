package com.anton.text;

import com.anton.buffer.Bufferable;

interface BufferableText<Key extends Comparable<? super Key>> extends Bufferable<String, Key> {
}
