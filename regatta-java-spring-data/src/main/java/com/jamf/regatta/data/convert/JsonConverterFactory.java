/*
 * Copyright JAMF Software, LLC
 */

package com.jamf.regatta.data.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamf.regatta.core.api.ByteSequence;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public class JsonConverterFactory implements ConditionalGenericConverter {

    private final ObjectMapper mapper;

    public JsonConverterFactory(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    private static boolean isMarshallableType(TypeDescriptor targetType) {
        return targetType.isAssignableTo(TypeDescriptor.valueOf(byte[].class)) || targetType.isAssignableTo(TypeDescriptor.valueOf(ByteSequence.class));
    }

    private static boolean hasJsonValueType(TypeDescriptor targetType) {
        return Optional.ofNullable(targetType.getType().getAnnotation(RegattaValueMapping.class))
                .map(RegattaValueMapping::value)
                .map(type -> type == RegattaValueMapping.Type.JSON)
                .orElse(false);
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return (isMarshallableType(targetType) && hasJsonValueType(sourceType))
                || (isMarshallableType(sourceType) && hasJsonValueType(targetType));
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
