package com.jamf.regatta.data.example.entity;

import com.jamf.regatta.data.convert.RegattaValueMapping;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import java.util.List;

@KeySpace("regatta-test")
@RegattaValueMapping(RegattaValueMapping.Type.JSON)
public record TestEntity(@Id String id, List<AdditionalInfo> additionalInfo) {
}
