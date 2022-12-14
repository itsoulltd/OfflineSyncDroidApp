package lab.infoworks.libshared.util.crypto.definition;

import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;

public interface iKeyStore {

    KeyStore getKeyStore() throws RuntimeException;
    Key createKey(String alias) throws RuntimeException;
    String encrypt(String alias, String text) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException;
    String decrypt(String alias, String encrypted) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException;

}
