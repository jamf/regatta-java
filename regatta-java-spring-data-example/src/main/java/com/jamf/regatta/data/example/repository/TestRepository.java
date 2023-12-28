package com.jamf.regatta.data.example.repository;

import com.jamf.regatta.data.example.entity.TestEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.List;

public interface TestRepository extends ListCrudRepository<TestEntity, String>, ListPagingAndSortingRepository<TestEntity, String> {

    List<TestEntity> findByLabel(String name);

}
