package io.interlockledger.extensions;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class ForByteArray {

	public static byte[] AES256Decrypt(final byte[] cipherData, final byte[] key, final byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		if (cipherData != null) {
				final IvParameterSpec ivspec = new IvParameterSpec(iv);
				final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
				final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
				cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
				return cipher.doFinal(cipherData);
		}
		return null;
	}

	public static byte[] AES256Encrypt(final byte[] plainData, final byte[] key, final byte[] iv)  throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		if (plainData != null) {
				final IvParameterSpec ivspec = new IvParameterSpec(iv);
				final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
				final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
				cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
				return cipher.doFinal(ZeroPadded(plainData));
		}
		return null;
	}

	private static byte[] ZeroPadded(byte[] plainData) {
		if (plainData != null && (plainData.length % 16) != 0) {
			return Arrays.copyOf(plainData, 16 * ((plainData.length + 15) / 16));
		}
		return plainData;
	}

	public static byte[] RSADecrypt(final RSAPrivateKey privateKey, final byte[] cipherData)
	    throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(cipherData);
	}

	public static byte[] RSAEncrypt(final PublicKey publicKey, final byte[] plainData)
	    throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(plainData);
	}

	public static String ToSafeBase64(final byte[] data) {
		if (data == null)
			return "";
		return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
	}

}
