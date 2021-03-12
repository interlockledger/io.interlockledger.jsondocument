package io.interlockledger.extensions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ForString {

	private interface ITrimPattern {
		void AppendPatternMiddle(StringBuilder sb);
	}

	public static String Ellipsis(String s, int limit) {
		return Ellipsis_Unsafe(Safe(s), limit);
	}

	public static boolean IsNullOrEmpty(String s) {
		return (s == null || s == "");
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
		if (IsNullOrEmpty(s))
			return "";
		return s;
	}

	public static String Trim(String s, char... chars) {
		return TrimLeading(TrimTrailing(s, chars), chars);
	}

	public static String TrimLeading(String s, char... chars) {
		return Trim_Using(s, chars, sb -> {
			Trim_AppendSanitizedSelection(sb, chars);
			sb.append("(.*)");
		});
	}

	public static String TrimTrailing(String s, char... chars) {
		return Trim_Using(s, chars, sb -> {
			sb.append("(.*?)");
			Trim_AppendSanitizedSelection(sb, chars);
		});
	}

	public static String WithDefault(String s, String defaultValue) {
		if (IsNullOrWhiteSpace(s))
			return Safe(defaultValue);
		return s;
	}

	private static String Ellipsis_Unsafe(String s, int limit) {
		return s.length() <= limit ? s : limit > 3 ? s.substring(0, limit - 3) + "..." : s.substring(0, limit);
	}

	private static void JsonDeserialize(String s) {
		// TODO Auto-generated method stub

		// private static final JsonSerializerOptions _jsonValidationOptions = new
		// JsonSerializerOptions() { AllowTrailingCommas = true, NumberHandling =
		// JsonNumberHandling.Strict, ReadCommentHandling = JsonCommentHandling.Skip, };

	}

	private static void Trim_AppendSanitizedSelection(StringBuilder sb, char[] chars) {
		sb.append("[");
		for (final char c : chars) {
			switch (c) {
			case '<':
			case '>':
			case '(':
			case ')':
			case '[':
			case ']':
			case '{':
			case '}':
			case '\\':
			case '^':
			case '-':
			case '=':
			case '$':
			case '?':
			case '*':
			case '+':
			case '.':
			case '!':
			case '|':
				sb.append('\\');
				break;
			default:
				break;
			}
			sb.append(c);
		}
		sb.append("]+");
	}

	private static String Trim_Using(String s, char[] chars, final ITrimPattern pattern) {
		final String safe = Safe(s);
		if (chars != null && chars.length > 0) {
			final StringBuilder sb = new StringBuilder("^");
			pattern.AppendPatternMiddle(sb);
			sb.append('$');
			String regex = sb.toString();
			final Matcher matcher = Pattern.compile(regex, Pattern.DOTALL).matcher(safe);
			if (matcher.find())
				return matcher.group(1);
		}
		return safe;
	}

}
