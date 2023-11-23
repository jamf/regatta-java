package com.jamf.regatta.data.example.entity;

import java.util.Objects;

public final class CertificateChain {

	private String certificatePemBase64;
	private String privateKeyPkcs8Base64;
	private EncryptionInfo encryptionInfo;

	public CertificateChain() {
	}

	public CertificateChain(String certificatePemBase64, String privateKeyPkcs8Base64, EncryptionInfo encryptionInfo) {
		this.certificatePemBase64 = certificatePemBase64;
		this.privateKeyPkcs8Base64 = privateKeyPkcs8Base64;
		this.encryptionInfo = encryptionInfo;
	}

	public String getCertificatePemBase64() {
		return certificatePemBase64;
	}

	public void setCertificatePemBase64(String certificatePemBase64) {
		this.certificatePemBase64 = certificatePemBase64;
	}

	public String getPrivateKeyPkcs8Base64() {
		return privateKeyPkcs8Base64;
	}

	public void setPrivateKeyPkcs8Base64(String privateKeyPkcs8Base64) {
		this.privateKeyPkcs8Base64 = privateKeyPkcs8Base64;
	}

	public EncryptionInfo getEncryptionInfo() {
		return encryptionInfo;
	}

	public void setEncryptionInfo(EncryptionInfo encryptionInfo) {
		this.encryptionInfo = encryptionInfo;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		var that = (CertificateChain) obj;
		return Objects.equals(this.certificatePemBase64, that.certificatePemBase64) &&
				Objects.equals(this.privateKeyPkcs8Base64, that.privateKeyPkcs8Base64) &&
				Objects.equals(this.encryptionInfo, that.encryptionInfo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(certificatePemBase64, privateKeyPkcs8Base64, encryptionInfo);
	}

	@Override
	public String toString() {
		return "CertificateChain[" +
				"certificatePemBase64=" + certificatePemBase64 + ", " +
				"privateKeyPkcs8Base64=" + privateKeyPkcs8Base64 + ", " +
				"encryptionInfo=" + encryptionInfo + ']';
	}
}
