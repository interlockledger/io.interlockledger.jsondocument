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

}
