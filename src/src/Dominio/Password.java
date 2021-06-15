package Dominio;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;


/**
*Esta clase representa la codificación y decodificación de una contraseña.
*/
public class Password {

	/**
     * Método que retorna la contraseña codificada.
     * @param password Indica una contraseña introducida.
     * @return Retorna retorna la contraseña codificada.
     */
    public static String hashPassword(String password) {
        String hash = null;
        try {
            //MD5 hashing algorithm
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            //Generate salt
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            
            //Convert bytes to hex
            StringBuilder sbSalt = new StringBuilder();
            for (int i = 0; i < salt.length; ++i){
                String hex = Integer.toString((salt[i] & 0xff) + 0x100, 16).substring(1);
                sbSalt.append(hex);
            }
            String saltS = sbSalt.toString();

            //Add salt to hash
            md.update(salt);
            //Hash password 
            byte[] bytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length ; ++i) {
                String hex = Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
                sb.append(hex);
            }

            //Get complete hashed password in hex format
            hash = sb.toString();
            hash = saltS + hash;
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }
	
	/**
     * Método que verifica la contraseña intoducida.
     * @param password Indica una contraseña introducida.
     * @param hashed Indica la contraseña hasheada.
     * @return Retorna cierto si la contraseña introducida coincide con la haseada, falso en caso contrario.
     */
    public static boolean checkPassword(final String password, final String hashed){
        String hash = null;
        try {
            //MD5 hashing algorithm
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            //Get salt
            byte[] salt = new byte[16];
            for (int i = 0; i < 16; ++i){
                int val = Integer.parseInt(hashed.subSequence(i*2, (i+1)*2).toString(),16);
                if (val > 127) val -= 256;
                salt[i] = (byte)val;
            }

            //Add salt to hash
            md.update(salt);
            //Hash password 
            byte[] bytes = md.digest(password.getBytes());
            //Convert bytes to hex
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length ; ++i) {
                String hex = Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
                sb.append(hex);
            }
            //Get complete hashed password in hex format
            hash = sb.toString();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return hashed.subSequence(32, 64).toString().equals(hash);
    }
}
