package io.interlockledger.jsondocument;

import static io.interlockledger.extensions.ForByteArray.*;
import static io.interlockledger.extensions.ForString.*;

import java.util.ArrayList;
import java.util.Collections;

public class EncryptedTextModel {
	private String _cipher;
	private byte[] _cipherText;
	private Iterable<ReadingKeyModel> _readingKeys;
	private int _readingKeysCount;

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

	// public String DecodedWith(X509Certificate2 certificate) {
	// if (certificate is null)
	// return "ERROR: No key provided to decode EncryptedText";
	// if (!certificate.HasPrivateKey)
	// return "ERROR: Certificate has no private key to be able to decode
	// EncryptedText";
	// string certKeyId = certificate.ToKeyId();
	// string pubKeyHash = certificate.ToPubKeyHash();
	// if (pubKeyHash is null)
	// return "ERROR: Non-RSA certificate is not currently supported";
	// if (model.ReadingKeys.SkipNulls().None())
	// return "ERROR: No reading keys able to decode EncryptedText";
	// var authorizedKey = model.ReadingKeys.FirstOrDefault(rk => rk.PublicKeyHash
	// == pubKeyHash && rk.ReaderId == certKeyId);
	// if (authorizedKey is null)
	// return "ERROR: Your key does not match one of the authorized reading keys";
	// string cipher = model.Cipher.WithDefault("AES256").ToUpperInvariant();
	// if (cipher != "AES256")
	// return $"ERROR: Cipher {cipher} is not currently supported";
	// if (model.CipherText.None())
	// return null;
	// using var rsaAlgo = certificate.GetRSAPrivateKey();
	// var aesKey = RSADecrypt(rsaAlgo, authorizedKey.EncryptedKey);
	// var aesIV = RSADecrypt(rsaAlgo, authorizedKey.EncryptedIV);
	// var jsonBytes = AES256Decrypt(model.CipherText, aesKey, aesIV);
	// if (jsonBytes[0] != 17)
	// return "ERROR: Something went wrong while decrypting the content. Unexpected
	// initial bytes";
	// var skipTagAndSize = ILIntHelpers.ILIntDecode(jsonBytes[1..]).ILIntSize() +
	// 1;
	// return jsonBytes[skipTagAndSize..].AsUTF8String();

	// static byte[] RSADecrypt(RSA rsaAlgo, byte[] data, int maxRetries = 3) {
	// int retries = maxRetries;
	// while (true)
	// try {
	// try {
	// return rsaAlgo.Decrypt(data, RSAEncryptionPadding.OaepSHA1);
	// } catch (CryptographicException) {
	// return rsaAlgo.Decrypt(data,
	// RSAEncryptionPadding.CreateOaep(HashAlgorithmName.MD5));
	// }
	// } catch (CryptographicException e) {
	// if (retries-- <= 0)
	// throw new InvalidOperationException($"Failed to decrypt data with current
	// parameters after {maxRetries} retries", e);
	// }
	// }

	// static byte[] AES256Decrypt(byte[] cipherData, byte[] key, byte[] iv) {
	// if (cipherData is null)
	// throw new ArgumentNullException(nameof(cipherData));
	// using var source = new MemoryStream(cipherData);
	// using var algorithm = new RijndaelManaged {
	// KeySize = 256,
	// BlockSize = 128,
	// IV = iv,
	// Key = key,
	// Mode = CipherMode.CBC,
	// Padding = PaddingMode.Zeros
	// };
	// using var cs = new CryptoStream(source, algorithm.CreateDecryptor(),
	// CryptoStreamMode.Read);
	// using var dest = new MemoryStream();
	// cs.CopyTo(dest);
	// return dest.ToArray();
	// }
	// }

}
