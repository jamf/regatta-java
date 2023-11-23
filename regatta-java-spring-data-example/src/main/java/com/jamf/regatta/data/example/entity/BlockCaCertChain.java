package com.jamf.regatta.data.example.entity;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

@KeySpace("block-ca-cert-chains")
public class BlockCaCertChain {

	@Id
	String customerId;
	CertificateChain certificateChain;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public CertificateChain getCertificateChain() {
		return certificateChain;
	}

	public void setCertificateChain(CertificateChain certificateChain) {
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
}
