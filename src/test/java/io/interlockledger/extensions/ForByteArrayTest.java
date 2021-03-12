package io.interlockledger.extensions;

import static org.junit.Assert.*;

import org.junit.Test;

public class ForByteArrayTest {

	@Test
	public final void testToSafeBase64() {
		assertEquals("", ForByteArray.ToSafeBase64(null));
		assertEquals("", ForByteArray.ToSafeBase64(new byte[0]));
		assertEquals("AQIDBAUGBwgJAA", ForByteArray.ToSafeBase64(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 }));
	}

}
