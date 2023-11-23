package com.jamf.regatta.data.example.entity;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

@KeySpace("block-ca-cert-chains")
public class BlockCaCertChain {

	@Id
	String customerId;
	List<CertificateChain> certificateChain;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public List<CertificateChain> getCertificateChain() {
		return certificateChain;
	}

	public void setCertificateChain(List<CertificateChain> certificateChain) {
		this.certificateChain = certificateChain;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BlockCaCertChain that = (BlockCaCertChain) o;
		return Objects.equals(customerId, that.customerId) && Objects.equals(certificateChain, that.certificateChain);
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerId, certificateChain);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", BlockCaCertChain.class.getSimpleName() + "[", "]")
				.add("customerId='" + customerId + "'")
				.add("certificateChain=" + certificateChain)
				.toString();
	}
}
