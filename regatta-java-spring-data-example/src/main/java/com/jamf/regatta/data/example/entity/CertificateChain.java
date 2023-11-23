package com.jamf.regatta.data.example.entity;

public record CertificateChain(String certificatePemBase64, String privateKeyPkcs8Base64, EncryptionInfo encryptionInfo) {}
