package com.jamf.regatta.data.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jamf.regatta.core.api.ByteSequence;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public class XmlConverterFactory implements ConditionalGenericConverter {

    private final XmlMapper mapper;

    public XmlConverterFactory(XmlMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return (isMarshallableType(targetType) && hasJsonValueType(sourceType))
                || (isMarshallableType(sourceType) && hasJsonValueType(targetType));
    }

    private static boolean isMarshallableType(TypeDescriptor targetType) {
        return targetType.isAssignableTo(TypeDescriptor.valueOf(byte[].class)) || targetType.isAssignableTo(TypeDescriptor.valueOf(ByteSequence.class));
    }

    private static boolean hasJsonValueType(TypeDescriptor targetType) {
        return Optional.ofNullable(targetType.getType().getAnnotation(RegattaValueMapping.class))
                .map(RegattaValueMapping::value)
                .map(type -> type == RegattaValueMapping.Type.XML)
                .orElse(false);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return null;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        try {
            if (sourceType.isAssignableTo(TypeDescriptor.valueOf(byte[].class))) {
                return mapper.readValue((byte[]) source, targetType.getObjectType());
            } else if (sourceType.isAssignableTo(TypeDescriptor.valueOf(ByteSequence.class))) {
                return mapper.readValue(((ByteSequence) source).getBytes(), targetType.getObjectType());
            } else if (targetType.isAssignableTo(TypeDescriptor.valueOf(ByteSequence.class))) {
                return ByteSequence.from(mapper.writeValueAsBytes(source));
            }
            return mapper.writeValueAsBytes(source);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
