package game.save_data;

/**
 * This is just for fun and not ment to be secure
 * @author QuantumPie
 *
 */
public class DataEncrypter {
	
	public static String encrypt(String str, int key) {
		String newStr = "";
		for(int i = 0; i < str.length(); i++) {
			newStr += Character.toString((char) (str.charAt(i) + key));
		}
		return newStr;
	}
	
	public static String decrypt(String str, int key) {
		String newStr = "";
		for(int i = 0; i < str.length(); i++) {
			newStr += Character.toString((char) (str.charAt(i) - key));
		}
		return newStr;
	}
}
