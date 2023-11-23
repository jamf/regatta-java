package com.jamf.regatta.data.example.entity;

import java.util.Objects;

public final class EncryptionInfo {

	private String cipher;
	private String keyId;
	private String ivBase64;

	public EncryptionInfo() {
	}

	public EncryptionInfo(String cipher, String keyId, String ivBase64) {
		this.cipher = cipher;
		this.keyId = keyId;
		this.ivBase64 = ivBase64;
	}

	public String getCipher() {
		return cipher;
	}

	public void setCipher(String cipher) {
		this.cipher = cipher;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getIvBase64() {
		return ivBase64;
	}

	public void setIvBase64(String ivBase64) {
		this.ivBase64 = ivBase64;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		var that = (EncryptionInfo) obj;
		return Objects.equals(this.cipher, that.cipher) &&
				Objects.equals(this.keyId, that.keyId) &&
				Objects.equals(this.ivBase64, that.ivBase64);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cipher, keyId, ivBase64);
	}

	@Override
	public String toString() {
		return "EncryptionInfo[" +
				"cipher=" + cipher + ", " +
				"keyId=" + keyId + ", " +
				"ivBase64=" + ivBase64 + ']';
	}
}
