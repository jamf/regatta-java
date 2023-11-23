package com.jamf.regatta;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.netty.NegotiationType;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	private static final String TEST_JSON_VALUE = """
			{
			    "name": "John Doe",
			    "age": 25,
			    "city": "Example City",
			}
			""";
	private static final String TEST_KEY = "testKey";
	private static final ByteSequence TEST_KEY_BYTE = ByteSequence.from(TEST_KEY, StandardCharsets.UTF_8);
	private static final ByteSequence TABLE = ByteSequence.from("block-ca-cert-chains", StandardCharsets.UTF_8);

	public static void main(String[] args) {
		try (var client = Client.builder()
				.target("reg.dev.wandera.co.uk:443")
				.negotiationType(NegotiationType.TLS)
				.insecureSkipTLSVerify(true)
				.build().getKVClient()) {

			LOGGER.info("Writing to regatta under key: {}", TEST_KEY);
			client.put(
					TABLE,
					TEST_KEY_BYTE,
					ByteSequence.from(TEST_JSON_VALUE, StandardCharsets.UTF_8)
			);

			LOGGER.info("Querying regatta, key: {}", TEST_KEY);
			var getResponse = client.get(
					TABLE,
					TEST_KEY_BYTE
			);

			var result = getResponse.kvs().get(0).value();
			LOGGER.info("Result: '{}'", result);

			LOGGER.info("Deleting from regatta, key: {}", TEST_KEY);
			client.delete(
					TABLE,
					TEST_KEY_BYTE
			);

		} catch (Exception e) {
			LOGGER.error("Exception thrown", e);
			throw new RuntimeException(e);
		}
	}
}
