package io.interlockledger.extensions;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.Set;

import org.junit.Test;

import io.interlockledger.iltags.ilint.ILIntException;

public class ForX509CertificateTest {

	@Test
	public final void testToJsonPubKey() throws ILIntException {
		final String jsonPubKey = ForX509Certificate.ToJsonPubKey(_cert);
		assertNotNull(jsonPubKey);
		assertEquals("PubKey!KHwQBWmfyWZGEHMNGlcIV7sh-dgc6bDRMh5jAvR3nmj22hzpX1n_wX_QGbl9GXWTVo3gZOibBnrx3ErZSzA5EjqIPWVW3SoVE8amK6LlpNI7irDBbJjZJHrvbBJzKMbigChmI5bPY5AanW0WLMpi1hAOuePAzB8bDdcfS8ZG#RSA", jsonPubKey);
	}

	@Test
	public final void testToKeyId() throws ILIntException {
		final String keyId = ForX509Certificate.ToKeyId(_cert);
		assertNotNull(keyId);
		assertEquals("Key!JtklbnAV0t3Flq0XIpjGcRvNGS0#SHA1", keyId);
	}

	@Test
	public final void testToPubKeyHash() throws ILIntException {
		final String pubKeyHash = ForX509Certificate.ToPubKeyHash(_cert);
		assertNotNull(pubKeyHash);
		assertEquals("nwZuMuZJnx7mv1vAbYn4UO75-ax4I8cebW5OsagkCxk#SHA256", pubKeyHash);
	}

	@Test
	public final void testToTaggedRSAPubKey() throws ILIntException {
		final byte[] taggedPubKey = ForX509Certificate.ToTaggedRSAPubKey(_cert);
		assertNotNull(taggedPubKey);
		assertEquals("KHwQBWmfyWZGEHMNGlcIV7sh-dgc6bDRMh5jAvR3nmj22hzpX1n_wX_QGbl9GXWTVo3gZOibBnrx3ErZSzA5EjqIPWVW3SoVE8amK6LlpNI7irDBbJjZJHrvbBJzKMbigChmI5bPY5AanW0WLMpi1hAOuePAzB8bDdcfS8ZG", ForByteArray.ToSafeBase64(taggedPubKey));
	}

	private static final X509Certificate _cert = new X509Certificate() {

		private final byte[] _encoded = new byte[] {
		        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
		};

		@Override
		public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
			// TODO Auto-generated method stub

		}

		@Override
		public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {
			// TODO Auto-generated method stub

		}

		@Override
		public int getBasicConstraints() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Set<String> getCriticalExtensionOIDs() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public byte[] getEncoded() throws CertificateEncodingException {
			return _encoded;
		}

		@Override
		public byte[] getExtensionValue(String oid) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Principal getIssuerDN() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean[] getIssuerUniqueID() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean[] getKeyUsage() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Set<String> getNonCriticalExtensionOIDs() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Date getNotAfter() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Date getNotBefore() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PublicKey getPublicKey() {
			RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger("453652342342"),new BigInteger("453652342342453652342342453652342342453652342342453652342342453652342342453652342342453652342342453652342342453652342342453652342342453652342342453652342342453652342342453652342342453652342342453652342342453652342342453652342342453652342342453652342342453652342342453652342342"));
			return new RSAPublicKey() {
				
				@Override
				public BigInteger getModulus() {
					return spec.getModulus();
				}
				
				@Override
				public String getFormat() {
					return "?";
				}
				
				@Override
				public byte[] getEncoded() {
					// TODO Auto-generated method stub
					return spec.getPublicExponent().toByteArray();
				}
				
				@Override
				public String getAlgorithm() {
					return "RSA";
				}
				
				@Override
				public BigInteger getPublicExponent() {
					return spec.getPublicExponent();
				}
			};
		}

		@Override
		public BigInteger getSerialNumber() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getSigAlgName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getSigAlgOID() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public byte[] getSigAlgParams() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public byte[] getSignature() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Principal getSubjectDN() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean[] getSubjectUniqueID() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public byte[] getTBSCertificate() throws CertificateEncodingException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getVersion() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean hasUnsupportedCriticalExtension() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
			// TODO Auto-generated method stub

		}

		@Override
		public void verify(PublicKey key, String sigProvider)
		    throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
			// TODO Auto-generated method stub

		}
	};
}