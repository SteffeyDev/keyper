package com.example.keypermobile;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    public void runEncryption(String plainText) throws Exception
    {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);

        // Generate Key
        SecretKey key = keyGenerator.generateKey();

        // Generating IV (random 16 character string nums and letters)
        byte[] IV = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);

        byte[] cipherText = Encrypt(plainText.getBytes(),key, IV);

        String decryptedText = Decrypt(cipherText,key, IV);
    }
    // key and IV should be 32 bytes
    public static byte[] Encrypt(byte[] plaintext, SecretKey key, byte[] IV)
            throws Exception
    {
        //Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        //Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        //Create IvParameterSpec
        IvParameterSpec ivSpec = new IvParameterSpec(IV);

        //Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        //Perform Encryption
        byte[] cipherText = cipher.doFinal(plaintext);

        return cipherText;
    }

    public String Decrypt(byte[] cipherText, SecretKey key,byte[] IV) throws Exception
    {
        //Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        //Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        //Create IvParameterSpec
        IvParameterSpec ivSpec = new IvParameterSpec(IV);

        //Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        //Perform Decryption
        byte[] decryptedText = cipher.doFinal(cipherText);

        return new String(decryptedText);
    }
}
