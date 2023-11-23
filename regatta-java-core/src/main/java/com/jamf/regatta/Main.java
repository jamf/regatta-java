package com.jamf.regatta;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jamf.regatta.proto.RangeResponse;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		try (Client client = Client.builder().target("reg.dev.wandera.co.uk:443").build()) {

			RangeResponse getResponse = client.getKVClient().get(
					ByteSequence.from("block-ca-cert-chains", StandardCharsets.UTF_8),
					ByteSequence.from("GLOBAL_CA", StandardCharsets.UTF_8));

			var result = getResponse.getKvs(0).getValue().toStringUtf8();
			LOGGER.info("Result: '{}'", result);

		} catch (Exception e) {
			LOGGER.error("Exception thrown", e);
			throw new RuntimeException(e);
		}
	}
}
