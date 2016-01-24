package pl.edu.pw.wsd.agency.pki;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyGenerator {

	/**
	 * Generates a 1024-bit RSA key pair
	 * @return pair of public and private keys
	 */
	public static KeyPair generateKeyPair(){
		KeyPairGenerator keyGen;
		try {
			keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(1024);
			KeyPair keypair = keyGen.genKeyPair();
			return keypair;
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}
}
