package model;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.crypto.SecretKeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PasswordUtilsTest {

    @Test
    public void testHashPassword() {
        String password = "password123";
        String hash1 = PasswordUtils.hashPassword(password);
        String hash2 = PasswordUtils.hashPassword(password);

        assertNotNull(hash1);
        assertNotNull(hash2);
        assertNotEquals(password, hash1);
        assertNotEquals(password, hash2);
        assertNotEquals(hash1, hash2, "Hashes should be different due to different salts");
    }

    @Test
    public void testVerifyPasswordSuccess() {
        String password = "password123";
        String hash = PasswordUtils.hashPassword(password);

        assertTrue(PasswordUtils.verifyPassword(password, hash), "Password should match the hash");
    }

    @Test
    public void testVerifyPasswordFailure() {
        String password = "password123";
        String wrongPassword = "wrongpassword";
        String hash = PasswordUtils.hashPassword(password);

        assertFalse(PasswordUtils.verifyPassword(wrongPassword, hash), "Password should not match the hash");
    }

    @Test
    public void testVerifyPasswordAfterRestart() {
        String password = "password123";
        String hash = PasswordUtils.hashPassword(password);

        // Simulate application restart by using the hash as it would be loaded from storage
        assertTrue(PasswordUtils.verifyPassword(password, hash), "Password should match the hash after restart");
    }

    /**
     * Test that hashPassword throws RuntimeException when SecretKeyFactory.getInstance throws NoSuchAlgorithmException
     */
    @Test
    public void testHashPassword_NoSuchAlgorithmException() {
        String password = "password123";

        // Mock SecretKeyFactory to throw NoSuchAlgorithmException
        try (MockedStatic<SecretKeyFactory> mockedFactory = Mockito.mockStatic(SecretKeyFactory.class)) {
            mockedFactory.when(() -> SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"))
                         .thenThrow(new NoSuchAlgorithmException("Algorithm not found"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                PasswordUtils.hashPassword(password);
            });

            assertEquals("Error initializing SecretKeyFactory", exception.getMessage());
            assertTrue(exception.getCause() instanceof NoSuchAlgorithmException);
            assertEquals("Algorithm not found", exception.getCause().getMessage());
        }
    }

    /**
     * Test that verifyPassword throws RuntimeException when SecretKeyFactory.getInstance throws NoSuchAlgorithmException
     */
    @Test
    public void testVerifyPassword_NoSuchAlgorithmException() {
        String password = "password123";
        String storedHash = "65536:someSalt:someHash";

        // Mock SecretKeyFactory to throw NoSuchAlgorithmException
        try (MockedStatic<SecretKeyFactory> mockedFactory = Mockito.mockStatic(SecretKeyFactory.class)) {
            mockedFactory.when(() -> SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"))
                         .thenThrow(new NoSuchAlgorithmException("Algorithm not found"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                PasswordUtils.verifyPassword(password, storedHash);
            });

            assertEquals("Error initializing SecretKeyFactory", exception.getMessage());
            assertTrue(exception.getCause() instanceof NoSuchAlgorithmException);
            assertEquals("Algorithm not found", exception.getCause().getMessage());
        }
    }

    /**
     * Test that hashPassword throws RuntimeException when SecretKeyFactory.generateSecret throws InvalidKeySpecException
     */
    @Test
    public void testHashPassword_InvalidKeySpecException() {
        String password = "password123";

        // Mock SecretKeyFactory to throw InvalidKeySpecException
        SecretKeyFactory mockFactory = mock(SecretKeyFactory.class);
        try {
            when(mockFactory.generateSecret(any())).thenThrow(new InvalidKeySpecException("Invalid Key Spec"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                PasswordUtils.hashPassword(password, mockFactory);
            });

            assertEquals("Error hashing password", exception.getMessage());
            assertTrue(exception.getCause() instanceof InvalidKeySpecException);
            assertEquals("Invalid Key Spec", exception.getCause().getMessage());
        } catch ( InvalidKeySpecException e) {
            fail("Exception should not have been thrown by the mock setup");
        }
    }

    /**
     * Test that verifyPassword throws RuntimeException when SecretKeyFactory.generateSecret throws InvalidKeySpecException
     */
    @Test
    public void testVerifyPassword_InvalidKeySpecException() {
        String password = "password123";
        String storedHash = "65536:someSalt:someHash";

        // Mock SecretKeyFactory to throw InvalidKeySpecException
        SecretKeyFactory mockFactory = mock(SecretKeyFactory.class);
        try {
            when(mockFactory.generateSecret(any())).thenThrow(new InvalidKeySpecException("Invalid Key Spec"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                PasswordUtils.verifyPassword(password, storedHash, mockFactory);
            });

            assertEquals("Error verifying password", exception.getMessage());
            assertTrue(exception.getCause() instanceof InvalidKeySpecException);
            assertEquals("Invalid Key Spec", exception.getCause().getMessage());
        } catch ( InvalidKeySpecException e) {
            fail("Exception should not have been thrown by the mock setup");
        }
    }
}
