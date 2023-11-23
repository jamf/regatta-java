package com.jamf.regatta.data.example;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.keyvalue.core.KeyValueOperations;

import com.jamf.regatta.Client;
import com.jamf.regatta.data.configuration.EnableRegattaRepositories;
import com.jamf.regatta.data.core.RegattaKeyValueAdapter;
import com.jamf.regatta.data.core.RegattaKeyValueTemplate;
import com.jamf.regatta.data.example.entity.BlockCaCertChain;

import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;

@SpringBootApplication
@EnableRegattaRepositories
public class RegattaSpringDataExampleApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegattaSpringDataExampleApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RegattaSpringDataExampleApplication.class, args);
	}

	@Bean
	public Client regattaClient() throws SSLException {
		return Client.builder().target("reg.dev.wandera.co.uk:443").negotiationType(NegotiationType.TLS).insecureSkipTLSVerify(true)
				.build();
	}

	@Bean
	public KeyValueOperations regattaTemplateRef(Client regattaClient) {
		return new RegattaKeyValueTemplate(new RegattaKeyValueAdapter(regattaClient));
	}

	@Bean
	public CommandLineRunner findBlockCaCertChain(KeyValueOperations regattaTemplateRef) {
		return args -> regattaTemplateRef.findById("GLOBAL_CA", BlockCaCertChain.class)
				.ifPresent(cert -> LOGGER.info("Cert fetched: {}", cert));
	}

}
