package com.example.passwordmanager;

import static android.content.Context.KEYGUARD_SERVICE;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Auth {
    private final String KEY_ALIAS = "password_key7";

    // check if the key is in the android keystore
    public boolean hasKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            return keyStore.containsAlias(KEY_ALIAS);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // generate a new key and override the previous key if it existed
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void generateKey() {
        try {
            final KeyGenerator keyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            final KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setUserAuthenticationRequired(true)
                    .setInvalidatedByBiometricEnrollment(false)
                    .setUserAuthenticationParameters(60, KeyProperties.AUTH_BIOMETRIC_STRONG | KeyProperties.AUTH_DEVICE_CREDENTIAL)
                    .build();

            keyGenerator.init(keyGenParameterSpec);
            final SecretKey secretKey = keyGenerator.generateKey();

            // key generated now
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    // get the key and return it (return null if not found)
    public SecretKey getKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_ALIAS, null);
            return key;
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    // encrypt the input plaintext, return null if error
    @RequiresApi(api = Build.VERSION_CODES.M)
    public String encrypt(String plaintext) throws UserNotAuthenticatedException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKey key = getKey();
            cipher.init(Cipher.ENCRYPT_MODE, key);
            while(plaintext.length() % 16 != 0) {
                plaintext += "\u0020";
            }
            Log.d("ENCRYPT", String.valueOf(plaintext.getBytes(StandardCharsets.ISO_8859_1).length));
            byte[] iv = cipher.getIV();
            Log.d("AUTH", String.valueOf(iv.length));
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.ISO_8859_1));
            ArrayList<Byte> cipherarray = new ArrayList<>();
            for(int i = 0; i < ciphertext.length; i++) cipherarray.add(ciphertext[i]);
            for(int i = 0; i < iv.length; i++) cipherarray.add(iv[i]);

            byte[] cipherIV = new byte[cipherarray.size()]; // IV is 16 bytes
            for(int i = 0; i < cipherarray.size(); i++) cipherIV[i] = cipherarray.get(i);

            return new String(cipherIV, StandardCharsets.ISO_8859_1);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            if(e instanceof UserNotAuthenticatedException) throw new UserNotAuthenticatedException();
        } catch (BadPaddingException e) {
            Log.e("AUTH", "Bad padding");
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            Log.e("AUTH", "illegal block size");
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public String decrypt(String ciphertext) throws UserNotAuthenticatedException {
        try {
            // IV is last 16 bytes of cipher
            byte[] ivBytes = ciphertext.substring(ciphertext.length() - 16).getBytes(StandardCharsets.ISO_8859_1);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKey key = getKey();
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] plaintextbytes = cipher.doFinal(ciphertext.getBytes(StandardCharsets.ISO_8859_1));
            String plaintext = new String(plaintextbytes, StandardCharsets.ISO_8859_1);
            return plaintext.substring(0, plaintext.length()-16).trim();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            if(e instanceof UserNotAuthenticatedException) throw new UserNotAuthenticatedException();
        } catch (BadPaddingException e) {
            Log.e("AUTH", "Bad padding");
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            Log.e("AUTH", "illegal block size");
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

}
