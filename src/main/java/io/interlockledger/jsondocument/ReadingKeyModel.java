package io.interlockledger.jsondocument;

public class ReadingKeyModel {

	private byte[] EncryptedIV;
	private byte[] EncryptedKey;
	private String PublicKeyHash;
	private String ReaderId;

	public byte[] getEncryptedIV() {
		return EncryptedIV;
	}

	public byte[] getEncryptedKey() {
		return EncryptedKey;
	}

	public String getPublicKeyHash() {
		return PublicKeyHash;
	}

	public String getReaderId() {
		return ReaderId;
	}

	public void setEncryptedIV(byte[] encryptedIV) {
		EncryptedIV = encryptedIV;
	}

	public void setEncryptedKey(byte[] encryptedKey) {
		EncryptedKey = encryptedKey;
	}

	public void setPublicKeyHash(String publicKeyHash) {
		PublicKeyHash = publicKeyHash;
	}

	public void setReaderId(String readerId) {
		ReaderId = readerId;
	}

}
