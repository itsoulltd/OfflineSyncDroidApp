package lab.infoworks.libshared.util.crypto.impl;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import lab.infoworks.libshared.BuildConfig;
import lab.infoworks.libshared.util.crypto.definition.Cryptor;
import lab.infoworks.libshared.util.crypto.definition.IVectorIO;
import lab.infoworks.libshared.util.crypto.models.CryptoAlgorithm;
import lab.infoworks.libshared.util.crypto.models.HashKey;
import lab.infoworks.libshared.util.crypto.models.Transformation;

public class DroidAESCryptor implements Cryptor {

    public static final String TAG = DroidAESCryptor.class.getSimpleName();
    private Cipher cipher;
    private Cipher decipher;
    private KeyStore keyStore;
    private final boolean isDebugMode;
    private IVectorIO iVectorIO;

    private final Transformation transformation;

    public DroidAESCryptor(KeyStore keyStore, IVectorIO iVectorIO) {
        this.keyStore = keyStore;
        this.transformation = Transformation.AES_CBC_PKCS7Padding;
        this.isDebugMode = BuildConfig.DEBUG;
        this.iVectorIO = iVectorIO;
    }

    public CryptoAlgorithm getAlgorithm() {return null;}
    public HashKey getHashKey() {
        return null;
    }
    public Transformation getTransformation() {return transformation;}
    private KeyStore getKeyStore(){return keyStore;}
    private IVectorIO getIV() {return iVectorIO;}

    @Override
    public Key getKey(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        try {
            KeyStore.Entry entry = getKeyStore().getEntry(key, null);
            return ((KeyStore.SecretKeyEntry) entry).getSecretKey();
        } catch (KeyStoreException | UnrecoverableEntryException e) {
            throw new UnsupportedEncodingException(e.getMessage());
        }
    }

    private Cipher getCipher(String alias) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        if (this.cipher == null){
            Cipher cipher = Cipher.getInstance(getTransformation().value());
            cipher.init(Cipher.ENCRYPT_MODE, getKey(alias));
            this.cipher = cipher;
            /*if (!getIV().isSaved(alias))*/
            getIV().write(alias, this.cipher.getIV());
        }
        return cipher;
    }

    @Override
    public String encrypt(String alias, String strToEncrypt) {
        try {
            byte[] encryptedBytes = getCipher(alias).doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));
            String encrypted = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
            if(isDebugMode) Log.d(TAG, "encryptUsingAesSecretKey: " + encrypted);
            return encrypted;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Cipher getDecipher(String alias) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        if (this.decipher == null){
            //byte[] ivs = getCipher(alias).getIV();
            byte[] ivs = getIV().read(alias);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivs);
            Cipher cipher = Cipher.getInstance(getTransformation().value());
            cipher.init(Cipher.DECRYPT_MODE, getKey(alias), ivParameterSpec);
            this.decipher = cipher;
        }
        return decipher;
    }

    @Override
    public String decrypt(String alias, String encrypted) {
        try {
            if(isDebugMode) Log.d(TAG, "decryptUsingAesSecretKey: " + encrypted);
            byte[] encryptedBytes = Base64.decode(encrypted.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
            byte[] readBytes = getDecipher(alias).doFinal(encryptedBytes);
            //
            String decryptedText = new String(readBytes, StandardCharsets.UTF_8);
            return decryptedText;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | BadPaddingException | InvalidAlgorithmParameterException
                | IllegalBlockSizeException | UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
