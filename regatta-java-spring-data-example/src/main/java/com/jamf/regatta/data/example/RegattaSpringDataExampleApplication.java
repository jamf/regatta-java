package com.jamf.regatta.data.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.keyvalue.core.KeyValueAdapter;
import org.springframework.data.keyvalue.core.KeyValueOperations;

import com.jamf.regatta.data.configuration.EnableRegattaRepositories;
import com.jamf.regatta.data.core.RegattaKeyValueAdapter;
import com.jamf.regatta.data.core.RegattaKeyValueTemplate;
import com.jamf.regatta.data.example.entity.BlockCaCertChain;

@SpringBootApplication
@EnableRegattaRepositories
public class RegattaSpringDataExampleApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegattaSpringDataExampleApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RegattaSpringDataExampleApplication.class, args);
	}

	@Bean
	public KeyValueOperations regattaTemplateRef() {
		return new RegattaKeyValueTemplate(regattaKeyValueAdapter());
	}

	@Bean
	public KeyValueAdapter regattaKeyValueAdapter() {
		return new RegattaKeyValueAdapter();
	}

	@Bean
	public CommandLineRunner findAllBlockCaCertChains(KeyValueOperations regattaTemplateRef) {
		return args -> {
			Iterable<BlockCaCertChain> certs = regattaTemplateRef.findAll(BlockCaCertChain.class);
			LOGGER.info("Certs fetched: {}", certs);
		};

	}

}
