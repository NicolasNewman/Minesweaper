package game.save_data;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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

import game.helper.Debugger;
import game.helper.Global;

/**
 * Used to encrypt the leaderboard data that are saved to a file as well as manage the IV and key
 * @author QuantumPie
 *
 */
public class DataEncrypter {
	
	private IvParameterSpec ivspec;
	private SecretKeySpec skey;
	private Cipher ci;
	private String keyPath, ivPath;
	
	
	public DataEncrypter(String ivPath, String keyPath) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		Debugger.DEBUG_print("Instance Created", "Instance of DataEncrypter created", true);
		this.ivPath = ivPath;
		this.keyPath = keyPath;
		loadKey();
		loadIV();
		createCipher(Cipher.ENCRYPT_MODE);
	}
	
	/**
	 * Generates an IV and saves it to a file
	 * Called in verifyDataExists in the Main class
	 * @param ivFile path of the iv file
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static void generateIV(String ivFile) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
		try(FileOutputStream out = new FileOutputStream(ivFile)) {
			SecureRandom srandom = new SecureRandom();
			byte[] iv = new byte[128/8];
			srandom.nextBytes(iv);
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			out.write(iv);
		}
	}
	
	/**
	 * Generates a key and saves it to a file
	 * Called in verifyDataExists in the Main class
	 * @param keyFile path of the key file
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void generateKey(String keyFile) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		try (FileOutputStream out = new FileOutputStream(keyFile)){
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecretKey skey = kgen.generateKey();
			byte[] keyb = skey.getEncoded();
			out.write(keyb);
		}
	}
	
	/**
	 * Encrypts str and saves it to file IF Global.SECURE_DATA is true
	 * Otherwise, save str to a file without being encrypted (used for debugging)
	 * @param str string to be encrypted
	 * @param file path of the file to save the data to
	 * @param append should the data be added to the file
	 * @return encrypted (or unencrypted) string
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 */
	public String encryptString(String str, String file, boolean append) throws FileNotFoundException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		String currentText = decryptString(file);
		if(Global.SECURE_DATA) {
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
		try (FileWriter out = new FileWriter(file, true)) {
		    out.write(str);
		}
		return str;
	}
	
	/**
	 * Decrypts the data in file and returns it IF Global.SECURE_DATA is true
	 * Otherwise, return the contents
	 * @param file path of the file containing the data
	 * @return contents of the file that are unencrypted
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 */
	public String decryptString(String file) throws IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException, InvalidAlgorithmParameterException {
		if(Global.SECURE_DATA) {
			ci.init(Cipher.DECRYPT_MODE, skey, ivspec);
			byte[] encoded = Files.readAllBytes(Paths.get(file));
			return new String(ci.doFinal(encoded), "UTF-8");
		}
		return new String(Files.readAllBytes(Paths.get(file)), "UTF-8");
	}
	
	/**
	 * Loads the key in the key file
	 * @throws IOException
	 */
	private void loadKey() throws IOException {
		byte[] keyb = Files.readAllBytes(Paths.get(keyPath));
		skey = new SecretKeySpec(keyb, "AES");
	}
	
	/**
	 * Loads the IV in the IV file
	 * @throws IOException
	 */
	private void loadIV() throws IOException {
		byte[] iv = Files.readAllBytes(Paths.get(ivPath));
		ivspec = new IvParameterSpec(iv);
	}
	
	/**
	 * Generates the cipher. Defaults to ENCRYPT_MODE
	 * @param mode of the cipher
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	private void createCipher(int mode) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
		ci.init(mode, skey, ivspec);
	}
}
