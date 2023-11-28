package com.jamf.regatta.core.api;

import com.google.protobuf.ByteString;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Etcd binary bytes, easy to convert between byte[], String and ByteString.
 */
public final class ByteSequence {
    public static final ByteSequence EMPTY = new ByteSequence(ByteString.EMPTY);
    public static final ByteSequence NAMESPACE_DELIMITER = ByteSequence.from(new byte[]{'/'});

    private final int hashVal;
    private final ByteString byteString;

    private ByteSequence(ByteString byteString) {
        Objects.requireNonNull(byteString, "byteString should not be null");
        this.byteString = byteString;
        this.hashVal = byteString.hashCode();
    }

    /**
     * Tests if this <code>ByteSequence</code> starts with the specified prefix.
     *
     * @param prefix the prefix.
     * @return <code>true</code> if the byte sequence represented by the argument is a prefix of the
     * byte sequence represented by this string; <code>false</code> otherwise.
     */
    public boolean startsWith(ByteSequence prefix) {
        if (prefix == null) {
            return false;
        }
        return byteString.startsWith(prefix.byteString);
    }

    /**
     * Concatenate the given {@code ByteSequence} to this one.
     *
     * @param other string to concatenate
     * @return a new {@code ByteSequence} instance
     */
    public ByteSequence concat(ByteSequence other) {
        Objects.requireNonNull(other, "other byteSequence should not be null");
        return new ByteSequence(this.byteString.concat(other.byteString));
    }

    /**
     * Concatenate the given {@code ByteSequence} to this one.
     *
     * @param other string to concatenate
     * @return a new {@code ByteSequence} instance
     */
    public ByteSequence concat(ByteString other) {
        Objects.requireNonNull(other, "other byteSequence should not be null");
        return new ByteSequence(this.byteString.concat(other));
    }

    /**
     * Return the substring from {@code ByteSequence}, inclusive, to the end of the
     * string.
     *
     * @param beginIndex start at this index
     * @return substring sharing underlying data
     * @throws IndexOutOfBoundsException if {@code beginIndex < 0} or
     *                                   {@code beginIndex > size()}.
     */
    public ByteSequence substring(int beginIndex) {
        return this.substring(beginIndex, this.byteString.size());
    }

    /**
     * Return the substring from {@code beginIndex}, inclusive, to {@code endIndex},
     * exclusive.
     *
     * @param beginIndex start at this index
     * @param endIndex   the last character is the one before this index
     * @return substring sharing underlying data
     * @throws IndexOutOfBoundsException if {@code beginIndex < 0},
     *                                   {@code endIndex > size()}, or
     *                                   {@code beginIndex > endIndex}.
     */
    public ByteSequence substring(int beginIndex, int endIndex) {
        return new ByteSequence(this.byteString.substring(beginIndex, endIndex));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ByteSequence) {
            ByteSequence other = (ByteSequence) obj;
            if (other.hashCode() != hashCode()) {
                return false;
            }
            return byteString.equals(other.byteString);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return hashVal;
    }

    public String toString(Charset charset) {
        return byteString.toString(charset);
    }

    public byte[] getBytes() {
        return byteString.toByteArray();
    }

    public boolean isEmpty() {
        return byteString.isEmpty();
    }

    public int size() {
        return byteString.size();
    }

    @Override
    public String toString() {
        return byteString.toStringUtf8();
    }

    public static ByteSequence fromUtf8String(String source) {
        return new ByteSequence(ByteString.copyFromUtf8(source));
    }

    /**
     * Create new ByteSequence from a String.
     *
     * @param  source  input String
     * @param  charset the character set to use to transform the String into bytes
     * @return         the ByteSequence
     */
    public static ByteSequence from(String source, Charset charset) {
        byte[] bytes = source.getBytes(charset);
        return new ByteSequence(ByteString.copyFrom(bytes));
    }

    /**
     * Create new ByteSequence from a {@link ByteString}.
     *
     * @param  source input {@link ByteString}
     * @return        the ByteSequence
     */
    public static ByteSequence from(ByteString source) {
        return new ByteSequence(source);
    }

    /**
     * Create new ByteSequence from raw bytes.
     *
     * @param  source input bytes
     * @return        the ByteSequence
     */
    public static ByteSequence from(byte[] source) {
        return new ByteSequence(ByteString.copyFrom(source));
    }
}
