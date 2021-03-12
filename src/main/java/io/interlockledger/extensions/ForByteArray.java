package io.interlockledger.extensions;

import java.util.Base64;

public final class ForByteArray {

	public static String ToSafeBase64(byte[] data) {
		if (data == null)
			return "";
		return ForString.TrimTrailing(Base64.getUrlEncoder().encodeToString(data), '=');
	}

}
