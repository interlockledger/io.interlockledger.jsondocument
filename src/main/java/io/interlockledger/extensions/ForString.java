package io.interlockledger.extensions;

public final class ForString {

	public static String Ellipsis(String s, int limit) {
		return UnsafeEllipsis(Safe(s), limit);
	}

	public static boolean IsNullOrWhiteSpace(String s) {
		return (s == null || s.strip() == "");
	}

	public static boolean IsValidJson(String s) {
		if (IsNullOrWhiteSpace(s))
			return false;
		try {
			JsonDeserialize(s.strip());
		} catch (final Exception e) {
			return false;
		}
		return true;
	}

	public static String Safe(String s) {
		return WithDefault(s, "");
	}

	public static String WithDefault(String s, String defaultValue) {
		if (IsNullOrWhiteSpace(s))
			return Safe(defaultValue);
		return s;
	}

	private static void JsonDeserialize(String s) {
		// TODO Auto-generated method stub

		// private static final JsonSerializerOptions _jsonValidationOptions = new
		// JsonSerializerOptions() { AllowTrailingCommas = true, NumberHandling =
		// JsonNumberHandling.Strict, ReadCommentHandling = JsonCommentHandling.Skip, };

	}

	private static String UnsafeEllipsis(String s, int limit) {
		return s.length() <= limit ? s : limit > 3 ? s.substring(0, limit - 3) + "..." : s.substring(0, limit);
	}

}
