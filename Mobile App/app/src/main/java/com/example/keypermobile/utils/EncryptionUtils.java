package com.example.keypermobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.keypermobile.models.Site;
import com.google.api.client.util.StringUtils;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {

    public static String pad(String raw)
    {
        int size = raw.length();
        int padding = 32 - (size % 32);

        for (int i = 0; i < padding; i++)
            raw = "x" + raw;

        return raw;
    }

    public static String unpad(String raw)
    {
        while (raw.charAt(0) == 'x')
            raw = raw.substring(1);

        return raw;
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

    public static String Decrypt(byte[] cipherText, SecretKey key,byte[] IV) throws Exception
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

    public static byte[] EncryptSite(Site site, Context context)
    {
        SharedPreferences pref = context.getSharedPreferences("keyper", 0);
        try
        {
            SecretKey key = new SecretKeySpec(Hex.decodeHex(pref.getString("key", "").toCharArray()), "AES");
            byte[] iv = site.getId().getBytes();
            return Encrypt(pad(site.toJson()).getBytes(), key, iv);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static Site DecryptSite(byte[] raw, String id, Context context)
    {
        SharedPreferences pref = context.getSharedPreferences("keyper", 0);
        try
        {
            SecretKey key = new SecretKeySpec(Hex.decodeHex(pref.getString("key", "").toCharArray()), "AES");
            byte[] iv = id.getBytes();
            return new Site(unpad(Decrypt(raw, key, iv)));
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
