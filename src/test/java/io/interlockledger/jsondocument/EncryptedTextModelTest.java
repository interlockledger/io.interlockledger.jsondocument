package io.interlockledger.jsondocument;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509ExtensionUtils;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.interlockledger.extensions.ForByteArray;
import io.interlockledger.extensions.ForX509Certificate;
import io.interlockledger.iltags.ilint.ILIntException;

public class EncryptedTextModelTest {

	public final String _jsonData = "{\n" + "    \"cipher\": \"AES256\",\n"
	        + "    \"cipherText\": \"l7YQnQ2vpLTjG5RpKKVJVTreRBIZzxGgFQSpXbV6qKaJymMVq+K9HIjntj+rfymH0x3rOELNMwYTupet00LaFYD0ABLglEfHC2ks9/1VHCK8IUXfnkXzvefLfNCoXKC0\",\n"
	        + "    \"readingKeys\": [\n" + "      {\n"
	        + "        \"encryptedIV\": \"uRi5A9i3ltVjhk1SjgGIlpCPCnsm1lMwPXm7MfBkFnOD3p7NLgKSNm4A3saPDJZb++oA0/0nAnmWgyRgtjdedvrVDmKgUHycubX5uL0IyWPijBYlWvaAYQT+aiGwa0l3g/541TyIyIA7eFHddSV0dQkbskOahGAJTRyebHKqgvvGtjJXY8ic9qHiQVWkI/QoQvd+Lggs2Rl5KHXNwUR/d210Cb+EueokfXncZ6IZPqCPebl1F9ICGODM7GYojrcQwSo1LekiCYD15MVtKsru3nOFQDySToNG49xea5zN0locuPiLDu/G6eZOa8+qvDWj+FcWqFIi/dKNTtu9lxwficL2wtDeoqcJCYUtRB2iR5xEKuSWwtswfGKU4dL8PSLyWituirdsC09VlvHLC4wY6om4kpdqS9ROke2W37bTHovOtbTtzPUsymGXZP0Sqdanvl81dkha2U4pYR+6QujEwpnWhAVKQFCAV5wT/GFUz4NLVkzS1WWZXNRsi1wH58nBlyvULU5UdrRYECa0ggkU9YjDiwB+n52Kjo0i/MeXKpBzvjrNOyEb6eQ+CxkJg9dkb2xgpz1n+oafrvB2ndLcU/eLr+i/3ezgnU1zlrTPvFFI/sN746IdkANRf/W/VhdnQvmXuHspf0yLR4NQgg4F8N4NOy5X8udQTM0GU+0Lq/k=\",\n"
	        + "        \"encryptedKey\": \"HSIgLCPveNTLENz6E6fSeCINTwdg2jv75omCjecmsD3olWWKqbI2D++3I4yDjAMmSe8/reGrUroC7VFON4R3RDl8S4PvWGaGECVOcuCmFywhTsP8wvn1esiSNcY3Y1/P5o08M0YJZkQOJKWlCX93435uUbVO6i1fSPEJ25zygWqmgcTPrZcQryfoBWEkyCrvUDxaoFLCW5kRVxcbyBNlu9/68Lw+CHU51lclvMOx8N/CgxJI+i8ITTDV9Vl91UzVPJs/Oqyag8fcQXQkqezmAhVvMCvq/z8LtuJIiwqzfbTDdj5SHyx2oSF2xuuZXdG+3lPQQjL3Ol29sneenuDsPe1WoQeEiK9yFkhgpf0591PUNSgs9TmEy8dU0c2u4rOd3zk9FR1DPDrGqr7oaq/2wNgJTCiUEwOYxMzksZlK6mOJHRD41thaXwHn7LbTF9HaGjy78OL1CAQsSuA6bGFRaKOyPCqsZszatWUAaYRSHG5BCDu+421DHQjKZemFegXI53tvGb0qGSOI6KxUb00XOuSFzRSPhZeSkEM6wB+Fn3bkbiVgJKPtBVq6IhVNDQaUnffFJdPRjURBYXcMDGcKt3wPDeBb9LUQgoa/U7Htfhh1wJQt2gRMO+080VUIoX57BFUfOk8xDF7WZ5sGbPYR8DBFgmaPsgaMKp0sA6kGZ7E=\",\n"
	        + "        \"publicKeyHash\": \"gD9gipsp9EHdbsPziZE5j00gd0xnHn2zDbsLV63wraU#SHA256\",\n" + "        \"readerId\": \"Key!Vtaxt0ECwcfb5LOh_KT459qISOU#SHA1\"\n"
	        + "      },\n" + "      {\n"
	        + "        \"encryptedIV\": \"pez9Aswz3HmM8y0lCGXphbCqBnXF6LwDMjShL+VRds8tgubkWrgfC+Jzjq+92+vIJ+neTJk3wGL3+zTOaIWMS1wgUAbnFgw4g3OjgJ2iXUQh7HNuU70CPPbZiIVy2HfeOU6a7i31Bbh1bJBEZpk+PcfvW7gR2dyDB5QEeUHNqe0BLy7FrJIcAC/9z78nXslmAuFIORTBXHhVh2HA2Vy1dt2Pu1iC54eB2oWcCzHyzGg8ufjpd4FAJPgcwYTEKsWsbK9rI+XdIX1rcC1oANBfJgX5kge0H3ylHUJqzHqr9Nzu+5wn/PXodG2MdxWSIUo707iNyS5lbi3CHQycYRPIRg==\",\n"
	        + "        \"encryptedKey\": \"xabr5stxYw0Mwa4106bGbgwdKLZQocrNLlVsvT5cKTMgq9UP/tVTygFQkQOlpvZKcZb9YySMym4OdepC7Q1G4s/5iAJ94LesoGtxCG3yEYWFAFUnAHsEmOAdTYNr7+LS7RQDM3DU87QlOFutREGxdcTqMGy8b2wvU8JoxCycib6U0qrrdGYTu4TCKdzx6Nv3DQa2bIpqLqem/G6DKFo5hBV/Sg3r8nh+mDC3DNFgttGUYR/lJIkLWBHIyg8SEBX0mmMNHeQsM/BEFp3cC3tjduut39vKibVdFxi3/BvKwslahRLHiLQr+O4PGElUxgtDIX261DTlZI6nwDxTE8AG4g==\",\n"
	        + "        \"publicKeyHash\": \"MsDSiYKdKW-xEPyDzZQw78yBzZxm44IJ9fJf--oXTjI#SHA256\",\n" + "        \"readerId\": \"Key!uZv_T2_t06HPbNzmg4_sOQFcu2M#SHA1\"\n"
	        + "      }\n" + "    ]\n" + "  }";

	@Test
	@Ignore
	public final void testDecodedWith()
	    throws JsonMappingException, JsonProcessingException, OperatorCreationException, CertificateException, CertIOException, NoSuchAlgorithmException, InvalidKeyException,
	    NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, ILIntException, InvalidAlgorithmParameterException {
		final ObjectMapper mapper = new ObjectMapper();
		final EncryptedTextModel model = mapper.readValue(_jsonData, EncryptedTextModel.class);
		final KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(1024);
		final KeyPair kp = kpg.generateKeyPair();
		final X509Certificate cert = generate(kp, "SHA1WITHRSA", "DecodeWith", 2);
		final RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) kp.getPrivate();
		String json = model.DecodedWith(cert, rsaPrivateKey);
		assertNotNull("Should have some result from trying to decrypt json", json);
		assertEquals("ERROR: Your key does not match one of the authorized reading keys", json);
		final byte[] iv = new byte[] {
		        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15
		};
		final byte[] key = new byte[] {
		        16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31
		};
		final String jsonToEncrypt = "{ \"a\": \"something\" }";
		final byte[] bytesToEncrypt = jsonToEncrypt.getBytes();
		final byte[] bytesEncrypted = ForByteArray.AES256Encrypt(bytesToEncrypt, key, iv);
		model.setCipherText(Base64.getEncoder().encode(bytesEncrypted));
		final ReadingKeyModel rk = new ReadingKeyModel(ForX509Certificate.ToKeyId(cert), ForX509Certificate.ToPubKeyHash(cert), ForByteArray.RSAEncrypt(kp.getPublic(), iv),
		        ForByteArray.RSAEncrypt(kp.getPublic(), key));
		model.setReadingKeys(rk);
		json = model.DecodedWith(cert, rsaPrivateKey);
		assertNotNull("Should have some result from trying to decrypt json", json);
		assertEquals(jsonToEncrypt, json);

	}

	@Test
	public final void testGetCipher() {
		final EncryptedTextModel model = new EncryptedTextModel("AES256", "NOTHING");
		assertEquals("AES256", model.getCipher());
	}

	@Test
	public final void testGetCipherText() {
		final EncryptedTextModel model = new EncryptedTextModel("NONE", "SOMETHING".getBytes(), null);
		assertEquals("SOMETHING", new String(model.getCipherText()));
	}

	@Test
	public final void testGetReadingKeys() {
		final EncryptedTextModel model = new EncryptedTextModel("NONE", "NOTHING", new ReadingKeyModel(), new ReadingKeyModel());
		final Iterable<ReadingKeyModel> models = model.getReadingKeys();
		assertNotNull(models);
		assertEquals("Count", 2, model.Count());
	}

	@Test
	public final void testParsingFromJsonWithJackson() {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final EncryptedTextModel model = mapper.readValue(_jsonData, EncryptedTextModel.class);
			assertNotNull("Should have parsed model", model);
			assertEquals("AES256", model.getCipher());
			assertByteArray(model.getCipherText(), "CipherText", -105);
			assertNotNull("Should have parsed ReadingKeys", model.getReadingKeys());
			assertEquals("ReadingKeys count doesn't match", 2, model.Count());
			int i = 0;
			for (final ReadingKeyModel rk : model.getReadingKeys()) {
				if (i == 0) {
					assertByteArray(rk.getEncryptedIV(), "0:EncryptedIV", -71);
					assertByteArray(rk.getEncryptedKey(), "0:EncryptedKey", 29);
					assertEquals("0:PublicKeyHash", "gD9gipsp9EHdbsPziZE5j00gd0xnHn2zDbsLV63wraU#SHA256", rk.getPublicKeyHash());
					assertEquals("0:readerId", "Key!Vtaxt0ECwcfb5LOh_KT459qISOU#SHA1", rk.getReaderId());
				} else {
					assertByteArray(rk.getEncryptedIV(), "1:EncryptedIV", -91);
					assertByteArray(rk.getEncryptedKey(), "1:EncryptedKey", -59);
					assertEquals("1:PublicKeyHash", "MsDSiYKdKW-xEPyDzZQw78yBzZxm44IJ9fJf--oXTjI#SHA256", rk.getPublicKeyHash());
					assertEquals("1:readerId", "Key!uZv_T2_t06HPbNzmg4_sOQFcu2M#SHA1", rk.getReaderId());
				}
				i++;
			}
		} catch (final JsonMappingException e) {
			e.printStackTrace();
			fail(e.toString());
		} catch (final JsonProcessingException e) {
			e.printStackTrace();
			fail(e.toString());
		}

	}

	@Test
	public final void testSetCipher() {
		final EncryptedTextModel model = new EncryptedTextModel("AES256", "NOTHING");
		assertEquals("AES256", model.getCipher());
		model.setCipher("AES128");
		assertEquals("AES128", model.getCipher());
	}

	@Test
	public final void testSetCipherText() {
		final EncryptedTextModel model = new EncryptedTextModel("NONE", "SOMETHING".getBytes(), null);
		assertEquals("SOMETHING", new String(model.getCipherText()));
		model.setCipherText("SOMETHING_ELSE".getBytes());
		assertEquals("SOMETHING_ELSE", new String(model.getCipherText()));
	}

	@Test
	public final void testSetReadingKeysReadingKeyModelArray() {
		final EncryptedTextModel model = new EncryptedTextModel("NONE", "NOTHING", new ReadingKeyModel(), new ReadingKeyModel());
		Iterable<ReadingKeyModel> models = model.getReadingKeys();
		assertNotNull(models);
		assertEquals("Count", 2, model.Count());
		model.setReadingKeys(new ReadingKeyModel());
		models = model.getReadingKeys();
		assertNotNull(models);
		assertEquals("Count", 1, model.Count());
	}

	@Test
	public final void testToString() {
		final EncryptedTextModel model = new EncryptedTextModel("NONE", "NOTHING+ELSE", new ReadingKeyModel(), new ReadingKeyModel());
		assertEquals("Encrypted Json with NONE for 2 keys with content \"NOTHING-ELSE\"", model.toString());
	}

	private void assertByteArray(final byte[] byteArray, final String arrayName, final int expectedFirstByte) {
		assertNotNull("Should have parsed " + arrayName, byteArray);
		assertTrue(arrayName + " should be non empty", byteArray.length > 0);
		assertEquals(arrayName + " should start with " + expectedFirstByte, expectedFirstByte, byteArray[0]);
	}

	/**
	 * Creates the hash value of the authority public key.
	 *
	 * @param publicKey of the authority certificate
	 *
	 * @return AuthorityKeyIdentifier hash
	 *
	 * @throws OperatorCreationException
	 */
	private static AuthorityKeyIdentifier createAuthorityKeyId(final PublicKey publicKey) throws OperatorCreationException {
		final SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
		final DigestCalculator digCalc = new BcDigestCalculatorProvider().get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1));
		return new X509ExtensionUtils(digCalc).createAuthorityKeyIdentifier(publicKeyInfo);
	}

	/**
	 * Creates the hash value of the public key.
	 *
	 * @param publicKey of the certificate
	 *
	 * @return SubjectKeyIdentifier hash
	 *
	 * @throws OperatorCreationException
	 */
	private static SubjectKeyIdentifier createSubjectKeyId(final PublicKey publicKey) throws OperatorCreationException {
		final SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
		final DigestCalculator digCalc = new BcDigestCalculatorProvider().get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1));
		return new X509ExtensionUtils(digCalc).createSubjectKeyIdentifier(publicKeyInfo);
	}

	/**
	 * Generates a self signed certificate using the BouncyCastle lib.
	 *
	 * @param keyPair       used for signing the certificate with PrivateKey
	 * @param hashAlgorithm Hash function
	 * @param cn            Common Name to be used in the subject dn
	 * @param days          validity period in days of the certificate
	 *
	 * @return self-signed X509Certificate
	 *
	 * @throws OperatorCreationException on creating a key id
	 * @throws CertIOException           on building JcaContentSignerBuilder
	 * @throws CertificateException      on getting certificate from provider
	 */
	private static final X509Certificate generate(final KeyPair keyPair, final String hashAlgorithm, final String cn, final int days)
	    throws OperatorCreationException, CertificateException, CertIOException {
		final Instant now = Instant.now();
		final Date notBefore = Date.from(now);
		final Date notAfter = Date.from(now.plus(Duration.ofDays(days)));

		final ContentSigner contentSigner = new JcaContentSignerBuilder(hashAlgorithm).build(keyPair.getPrivate());
		final X500Name x500Name = new X500Name("CN=" + cn);
		final X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(x500Name, BigInteger.valueOf(now.toEpochMilli()), notBefore, notAfter, x500Name,
		        keyPair.getPublic()).addExtension(Extension.subjectKeyIdentifier, false, createSubjectKeyId(keyPair.getPublic()))
		                .addExtension(Extension.authorityKeyIdentifier, false, createAuthorityKeyId(keyPair.getPublic()))
		                .addExtension(Extension.basicConstraints, true, new BasicConstraints(true));

		return new JcaX509CertificateConverter().setProvider(new BouncyCastleProvider()).getCertificate(certificateBuilder.build(contentSigner));
	}
}
