package com.jamf.regatta.data.example.repository;

import com.jamf.regatta.data.example.entity.TestEntity;
import org.springframework.data.repository.CrudRepository;

public interface TestRepository extends CrudRepository<TestEntity, String> {
}
