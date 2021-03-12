package io.interlockledger.extensions;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class ForByteArray {

	public static byte[] AES256Decrypt(final byte[] cipherData, final byte[] key, final byte[] iv) {
		if (cipherData == null)
			try {
				final IvParameterSpec ivspec = new IvParameterSpec(iv);
				final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
				final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
				cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
				return cipher.doFinal(cipherData);
			} catch (final Exception e) {
				System.out.println("Error while decrypting: " + e.toString());
			}
		return null;
	}

	public static String ToSafeBase64(final byte[] data) {
		if (data == null)
			return "";
		return ForString.TrimTrailing(Base64.getUrlEncoder().encodeToString(data), '=');
	}

}
