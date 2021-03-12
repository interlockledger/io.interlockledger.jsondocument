package io.interlockledger.jsondocument;

import static io.interlockledger.extensions.ForByteArray.*;
import static io.interlockledger.extensions.ForString.*;
import static io.interlockledger.extensions.ForX509Certificate.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.interlockledger.iltags.ilint.ILIntCodec;
import io.interlockledger.iltags.ilint.ILIntException;

public class EncryptedTextModel {
	private String _cipher;
	private byte[] _cipherText;
	private Iterable<ReadingKeyModel> _readingKeys;
	private int _readingKeysCount;

	public String DecodedWith(X509Certificate certificate, RSAPrivateKey privateKey)
			throws ILIntException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
			BadPaddingException, IllegalBlockSizeException {
		if (certificate == null)
			return "ERROR: No certificate provided to decode EncryptedText";
		if (privateKey == null)
			return "ERROR: No private key to be able to decode EncryptedText";
		final String certKeyId = ToKeyId(certificate);
		final String pubKeyHash = ToPubKeyHash(certificate);
		if (pubKeyHash == null)
			return "ERROR: Non-RSA certificate is not currently supported";
		final Iterable<ReadingKeyModel> readingKeys = getReadingKeys();
		if (readingKeys == null)
			return "ERROR: No reading keys able to decode EncryptedText";
		for (final ReadingKeyModel rk : readingKeys) {
			if (rk.getPublicKeyHash() == pubKeyHash && rk.getReaderId() == certKeyId) {
				final String cipher = WithDefault(getCipher(), "AES256").toUpperCase();
				if (cipher != "AES256")
					return "ERROR: Cipher " + cipher + " is not currently supported";
				if (getCipherText() == null)
					return null;
				final byte[] aesKey = RSADecrypt(privateKey, rk.getEncryptedKey());
				final byte[] aesIV = RSADecrypt(privateKey, rk.getEncryptedIV());
				final byte[] jsonBytes = AES256Decrypt(getCipherText(), aesKey, aesIV);
				final ByteBuffer bb = ByteBuffer.wrap(jsonBytes);
				final long tagType = ILIntCodec.decode(bb);
				if (tagType != 17)
					return "ERROR: Something went wrong while decrypting the content. Unexpected initial bytes";
				final long textSize = ILIntCodec.decode(bb);
				final int sizeSize = ILIntCodec.getEncodedSize(textSize);
				return new String(Arrays.copyOfRange(jsonBytes, sizeSize + 1, jsonBytes.length - 1),
						StandardCharsets.UTF_8);
			}
		}
		return "ERROR: Your key does not match one of the authorized reading keys";
	}

	public String getCipher() {
		return _cipher;
	}

	public byte[] getCipherText() {
		return _cipherText;
	}

	public Iterable<ReadingKeyModel> getReadingKeys() {
		return _readingKeys;
	}

	public void setCipher(String cipher) {
		_cipher = cipher;
	}

	public void setCipherText(byte[] cipherText) {
		_cipherText = cipherText;
	}

	public void setReadingKeys(Iterable<ReadingKeyModel> readingKeys) {
		_readingKeys = readingKeys;
		_readingKeysCount = Count(_readingKeys);
	}

	public void setReadingKeys(ReadingKeyModel... readingKeys) {
		setReadingKeys(ToIterable(readingKeys));
	}

	@Override
	public String toString() {
		return String.format("Encrypted Json with %s for %d keys with content \"%s\"", _cipher, _readingKeysCount,
				Ellipsis(ToSafeBase64(_cipherText), 135));
	}

	public static byte[] RSADecrypt(RSAPrivateKey privateKey, byte[] data) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	private static int Count(Iterable<ReadingKeyModel> readingKeys) {
		if (readingKeys == null)
			return 0;
		int count = 0;
		for (@SuppressWarnings("unused")
		final ReadingKeyModel readingKey : readingKeys) {
			count++;
		}
		return count;
	}

	private static Iterable<ReadingKeyModel> ToIterable(ReadingKeyModel... readingKeys) {
		final ArrayList<ReadingKeyModel> keys = new ArrayList<>();
		Collections.addAll(keys, readingKeys);
		return keys;
	}

}
