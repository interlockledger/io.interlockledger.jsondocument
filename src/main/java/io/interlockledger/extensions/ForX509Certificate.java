package io.interlockledger.extensions;

import static io.interlockledger.extensions.ForByteArray.*;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import io.interlockledger.iltags.ilint.ILIntCodec;
import io.interlockledger.iltags.ilint.ILIntException;

public final class ForX509Certificate {

	public static String ToKeyId(X509Certificate certificate) throws ILIntException {
		try {
			return "Key!" + ToSafeBase64(HashSha1(certificate.getEncoded())) + "#SHA1";
		} catch (CertificateEncodingException | NoSuchAlgorithmException e) {
			throw new ILIntException(e);
		}
	}

	public static String ToPubKeyHash(X509Certificate certificate) throws ILIntException {
		final PublicKey pubKey = certificate.getPublicKey();
		if (pubKey == null || pubKey.getAlgorithm() != "RSA")
			return null;
		final RSAPublicKey rsaPubKey = (RSAPublicKey) pubKey;
		final byte[] modulus = rsaPubKey.getModulus().toByteArray();
		final byte[] exponent = rsaPubKey.getPublicExponent().toByteArray();
		final byte[] modulusTag = PseudoTag(16, modulus);
		final byte[] exponentTag = PseudoTag(16, exponent);
		final byte[] pubKeyRSAParametersTag = PseudoTag(40, modulusTag, exponentTag);
		try {
			return ToSafeBase64(HashSha256(pubKeyRSAParametersTag)) + "#SHA256";
		} catch (final NoSuchAlgorithmException e) {
			throw new ILIntException(e);
		}
	}

	private static byte[] HashSha1(byte[] data) throws NoSuchAlgorithmException {
		final MessageDigest digest = MessageDigest.getInstance("SHA-1");
		return digest.digest(data);
	}

	private static byte[] HashSha256(byte[] data) throws NoSuchAlgorithmException {
		final MessageDigest digest = MessageDigest.getInstance("SHA-256");
		return digest.digest(data);
	}

	private static byte[] PseudoTag(long tagId, byte[]... parts) throws ILIntException {
		final int tagIdSize = ILIntCodec.getEncodedSize(tagId);
		int partsTotalLength = 0;
		for (final byte[] part : parts) {
			partsTotalLength += part.length;
		}
		final ByteBuffer buffer = ByteBuffer
				.allocate(tagIdSize + ILIntCodec.getEncodedSize(partsTotalLength) + partsTotalLength);
		ILIntCodec.encode(tagId, buffer);
		ILIntCodec.encode(partsTotalLength, buffer);
		for (final byte[] part : parts) {
			buffer.put(part);
		}
		return buffer.array();
	}

}
