package io.interlockledger.extensions;

import static org.junit.Assert.*;

import org.junit.Test;

public class ForStringTest {

	@Test
	public final void testEllipsis() {
		fail("Not yet implemented");
	}

	@Test
	public final void testIsNullOrEmpty() {
		assertTrue("null should return true", ForString.IsNullOrEmpty(null));
		assertTrue("empty should return true", ForString.IsNullOrEmpty(""));
		assertFalse("whitespace should return false", ForString.IsNullOrEmpty(" \r\n "));
		assertFalse("some letter should return false", ForString.IsNullOrEmpty("a"));
		assertFalse("some letters with whitespace should return false", ForString.IsNullOrEmpty(" abc  "));
	}

	@Test
	public final void testIsNullOrWhiteSpace() {
		assertTrue("null should return true", ForString.IsNullOrWhiteSpace(null));
		assertTrue("empty should return true", ForString.IsNullOrWhiteSpace(""));
		assertTrue("whitespace should return true", ForString.IsNullOrWhiteSpace(" \r\n "));
		assertFalse("some letter should return false", ForString.IsNullOrWhiteSpace("a"));
		assertFalse("some letters with whitespace should return false", ForString.IsNullOrWhiteSpace(" abc  "));
	}

	@Test
	public final void testIsValidJson() {
		fail("Not yet implemented");
	}

	@Test
	public final void testSafe() {
		assertEquals("null should return empty string", "", ForString.Safe(null));
		assertEquals("empty should return empty string", "", ForString.Safe(""));
		assertEquals("only whitespace should return same string", " \r\n ", ForString.Safe(" \r\n "));
		assertEquals("some letters with whitespace should return same string", " abc  ", ForString.Safe(" abc  "));
	}

	@Test
	public final void testTrim() {
		assertEquals("null with no chars should return empty string", "", ForString.Trim(null));
		assertEquals("empty with no chars should return empty string", "", ForString.Trim(""));
		assertEquals("only whitespace with no chars should return same string", " \r\n ", ForString.Trim(" \r\n "));
		assertEquals("some letters with whitespace with no chars should return same string", " abc  ", ForString.Trim(" abc  "));
		assertEquals("null with one char should return empty string", "", ForString.Trim(null, ' '));
		assertEquals("empty with one char should return empty string", "", ForString.Trim("", ' '));
		assertEquals("only whitespace with one non-whitespace char should return same string", " \r\n ", ForString.Trim(" \r\n ", 'z'));
		assertEquals("only whitespace with one matching whitespace char should return trimmed string", "\r\n", ForString.Trim(" \r\n ", ' '));
		assertEquals("some letters with matching chars should return trimmed string", "cdefghijklmnopqrstuvwx", ForString.Trim("abcdefghijklmnopqrstuvwxyz", 'a', 'b', 'y', 'z', 'r'));
		assertEquals("some non-ascii letters with matching chars should return trimmed string", "bcdefghijklmnopqrstuvwxy", ForString.Trim("ãbcdefghijklmnopqrstuvwxyçã", 'ã', 'ç', 'é'));
		assertEquals("some letters with whitespace with one matching whitespace should return trimmed string", "abc", ForString.Trim(" abc ", ' '));
	}

	@Test
	public final void testTrimLeading() {
		fail("Not yet implemented");
	}

	@Test
	public final void testTrimTrailing() {
		fail("Not yet implemented");
	}

	@Test
	public final void testWithDefault() {
		fail("Not yet implemented");
	}

}
