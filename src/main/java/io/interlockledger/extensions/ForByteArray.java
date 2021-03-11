package io.interlockledger.extensions;

import java.util.Base64;

public final class ForByteArray {

	public static String ToSafeBase64(byte[] data) {
		return Base64.getUrlEncoder().encodeToString(data);
	}

}
