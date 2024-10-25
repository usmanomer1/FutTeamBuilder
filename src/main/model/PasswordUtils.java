package model;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtils {
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final SecureRandom RANDOM = new SecureRandom();

    // Private constructor to prevent instantiation
    private PasswordUtils() {}

    /**
     * Hashes the password using the provided SecretKeyFactory.
     *
     * @param password the plain-text password
     * @param factory the SecretKeyFactory to use for hashing
     * @return the hashed password in the format iterations:salt:hash
     */
    public static String hashPassword(String password, SecretKeyFactory factory) {
        try {
            byte[] salt = getSalt();
            byte[] hash = pbkdf2(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH, factory);
            return ITERATIONS + ":" + Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Hashes the password using the default SecretKeyFactory.
     *
     * @param password the plain-text password
     * @return the hashed password in the format iterations:salt:hash
     */
    public static String hashPassword(String password) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            return hashPassword(password, factory);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error initializing SecretKeyFactory", e);
        }
    }

    /**
     * Verifies the password against the stored hash using the provided SecretKeyFactory.
     *
     * @param password the plain-text password
     * @param storedHash the stored hash in the format iterations:salt:hash
     * @param factory the SecretKeyFactory to use for hashing
     * @return true if the password matches the hash, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash, SecretKeyFactory factory) {
        String[] parts = storedHash.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = Base64.getDecoder().decode(parts[1]);
        byte[] hash = Base64.getDecoder().decode(parts[2]);

        try {
            byte[] testHash = pbkdf2(password.toCharArray(), salt, iterations, hash.length * 8, factory);
           
            for (int i = 0; i < testHash.length; i++) {
                if (testHash[i] != hash[i]) return false;
            }
            return true;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error verifying password", e);
        }
    }

    /**
     * Verifies the password against the stored hash using the default SecretKeyFactory.
     *
     * @param password the plain-text password
     * @param storedHash the stored hash in the format iterations:salt:hash
     * @return true if the password matches the hash, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            return verifyPassword(password, storedHash, factory);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error initializing SecretKeyFactory", e);
        }
    }

    private static byte[] getSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLength, SecretKeyFactory factory)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
        return factory.generateSecret(spec).getEncoded();
    }
}
