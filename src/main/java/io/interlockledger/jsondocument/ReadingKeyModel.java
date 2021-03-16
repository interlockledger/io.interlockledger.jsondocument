package io.interlockledger.jsondocument;

import java.util.Base64;

public class ReadingKeyModel {

	private byte[] _encryptedIV;
	private byte[] _encryptedKey;
	private String _publicKeyHash;
	private String _readerId;

	public ReadingKeyModel() {
	}

	public ReadingKeyModel(String readerId, String publicKeyHash, byte[] encryptedIV, byte[] encryptedKey) {
		_encryptedIV = encryptedIV;
		_encryptedKey = encryptedKey;
		_publicKeyHash = publicKeyHash;
		_readerId = readerId;
	}

	public ReadingKeyModel(String readerId, String publicKeyHash, String encryptedIV, String encryptedKey) {
		this(readerId, publicKeyHash, Base64.getDecoder().decode(encryptedIV), Base64.getDecoder().decode(encryptedKey));
	}

	public byte[] getEncryptedIV() {
		return _encryptedIV;
	}

	public byte[] getEncryptedKey() {
		return _encryptedKey;
	}

	public String getPublicKeyHash() {
		return _publicKeyHash;
	}

	public String getReaderId() {
		return _readerId;
	}

	public void setEncryptedIV(byte[] encryptedIV) {
		_encryptedIV = encryptedIV;
	}

	public void setEncryptedKey(byte[] encryptedKey) {
		_encryptedKey = encryptedKey;
	}

	public void setPublicKeyHash(String publicKeyHash) {
		_publicKeyHash = publicKeyHash;
	}

	public void setReaderId(String readerId) {
		_readerId = readerId;
	}

}
