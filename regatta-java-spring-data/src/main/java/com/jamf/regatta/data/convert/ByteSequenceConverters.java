package com.jamf.regatta.data.convert;

import com.jamf.regatta.core.api.ByteSequence;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

public class ByteSequenceConverters {
    private static class ByteSequenceToStringConverter implements Converter<ByteSequence, String> {
        @Override
        public String convert(ByteSequence source) {
            return source.toString();
        }
    }

    private static class StringToByteSequenceConverter implements Converter<String, ByteSequence> {
        @Override
        public ByteSequence convert(String source) {
            return ByteSequence.fromUtf8String(source);
        }
    }

    private static class ByteSequenceToBytesConverter implements Converter<ByteSequence, byte[]> {
        @Override
        public byte[] convert(ByteSequence source) {
            return source.getBytes();
        }
    }

    private static class BytesToByteSequenceConverter implements Converter<byte[], ByteSequence> {
        @Override
        public ByteSequence convert(byte[] source) {
            return ByteSequence.from(source);
        }
    }

    public static List<Converter<?,?>> converters() {
        return List.of(
                new ByteSequenceToStringConverter(),
                new StringToByteSequenceConverter(),
                new ByteSequenceToBytesConverter(),
                new BytesToByteSequenceConverter()
        );
    }
}
