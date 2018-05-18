package game.save_data;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DataEncrypter {
	
	private IvParameterSpec ivspec;
	private SecretKeySpec skey;
	private Cipher ci;
	private String keyPath, ivPath;
	
	
	public DataEncrypter(String ivPath, String keyPath) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		this.ivPath = ivPath;
		this.keyPath = keyPath;
		loadKey();
		loadIV();
		createCipher(Cipher.ENCRYPT_MODE);
	}
	
	public static void generateIV(String ivFile) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
		try(FileOutputStream out = new FileOutputStream(ivFile)) {
			SecureRandom srandom = new SecureRandom();
			byte[] iv = new byte[128/8];
			srandom.nextBytes(iv);
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			out.write(iv);
		}
	}
	
	public static void generateKey(String keyFile) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		try (FileOutputStream out = new FileOutputStream(keyFile)){
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecretKey skey = kgen.generateKey();
			byte[] keyb = skey.getEncoded();
			out.write(keyb);
		}
	}
	
	public String encryptString(String str, String file, boolean append) throws FileNotFoundException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		String currentText = decryptString(file);
		try (FileOutputStream out = new FileOutputStream(file)) {
			byte[] input;
			if(append) {
				input = (currentText + str).getBytes("UTF-8");
			} else {
				input = str.getBytes("UTF-8");
			}
			ci.init(Cipher.ENCRYPT_MODE, skey, ivspec);
		    byte[] encoded = ci.doFinal(input);
		    out.write(encoded);
			return new String(encoded);
		}
	}
	
	public String decryptString(String file) throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException, InvalidAlgorithmParameterException {
		ci.init(Cipher.DECRYPT_MODE, skey, ivspec);
		byte[] encoded = Files.readAllBytes(Paths.get(file));
		return new String(ci.doFinal(encoded), "UTF-8");
	}
	
	private void loadKey() throws IOException {
		byte[] keyb = Files.readAllBytes(Paths.get(keyPath));
		skey = new SecretKeySpec(keyb, "AES");
	}
	
	private void loadIV() throws IOException {
		byte[] iv = Files.readAllBytes(Paths.get(ivPath));
		ivspec = new IvParameterSpec(iv);
	}
	
	private void createCipher(int mode) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
		ci.init(mode, skey, ivspec);
	}
}
