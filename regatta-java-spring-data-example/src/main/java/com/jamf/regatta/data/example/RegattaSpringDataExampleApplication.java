package com.jamf.regatta.data.example;

import com.jamf.regatta.core.Client;
import com.jamf.regatta.data.configuration.EnableRegattaRepositories;
import com.jamf.regatta.data.example.entity.AdditionalInfo;
import com.jamf.regatta.data.example.entity.TestEntity;
import com.jamf.regatta.data.example.repository.TestRepository;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.SSLException;
import java.util.List;

@SpringBootApplication
@EnableRegattaRepositories
public class RegattaSpringDataExampleApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegattaSpringDataExampleApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RegattaSpringDataExampleApplication.class, args);
    }

    @Bean
    public Client regattaClient() throws SSLException {
        return Client.builder()
                .target("localhost:5101")
                .negotiationType(NegotiationType.PLAINTEXT)
                .build();
    }


    @Bean
    public CommandLineRunner findBlockCaCertChain(TestRepository testRepository) {
        return args -> {
            LOGGER.info("Repo count: {}", testRepository.count());
            testRepository.save(new TestEntity("foo", List.of()));
            testRepository.findById("foo")
                    .ifPresent(entity -> LOGGER.info("Entity fetched: {}", entity));
            testRepository.save(new TestEntity("foo", List.of(new AdditionalInfo("foo", "bar"))));
            testRepository.findAll().forEach(
                    entity -> LOGGER.info("Entity fetched: {}", entity)
            );
            testRepository.deleteAll();
        };
    }

}
