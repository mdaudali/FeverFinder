package com.example.feverfinder;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

/**
 * THIS CLASS IS NO LONGER IN USE - WE ARE USING DIFFERENT SOLUTION TO ENCRYPT DATA
 */
@Deprecated
public class SurveyStoreEncrypt {
    private static final String KEY_ALIAS = "keyAlias";
    private static final String ALGORITHM = "RSA";
    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    private static KeyStore.Entry getKeys(Context context) throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException, NoSuchProviderException, InvalidAlgorithmParameterException, UnrecoverableEntryException {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            //Set the time restriction on the key
            Calendar notBefore = Calendar.getInstance();
            //TODO: deal with end dates properly
            Calendar notAfter = Calendar.getInstance();
            notAfter.add(Calendar.YEAR, 100);

            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(KEY_ALIAS)
                    .setKeyType(ALGORITHM)
                    .setKeySize(2048)
                    .setSubject(new X500Principal("CN=app"))
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(notBefore.getTime())
                    .setEndDate(notAfter.getTime())
                    .build();

            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
            generator.initialize(spec);
            generator.generateKeyPair();
            keyStore.store(null);
        }


        return keyStore.getEntry(KEY_ALIAS, null);
    }


    protected static void saveSurvey(String contents, Context context) throws EncryptionException, SaveException {
        try {
            //Create the cipher text
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            PublicKey publicKey = ((KeyStore.PrivateKeyEntry) getKeys(context)).getCertificate().getPublicKey();
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            //Find a new unique filename
            List<String> fileNames = Arrays.asList(context.getFilesDir().list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.startsWith("response");
                }
            }));

            int i = 0;
            String filename = "response" + String.valueOf(i);
            while (fileNames.contains(filename)) {
                i++;
                filename = "response" + String.valueOf(i);
            }

            //Write out the bytes
            CipherOutputStream cipherOutputStream =
                    new CipherOutputStream(
                            context.openFileOutput(filename, Context.MODE_PRIVATE), cipher);
            cipherOutputStream.write(contents.getBytes("UTF-8"));
            cipherOutputStream.close();
        } catch (Exception e) {
            throw new SaveException(e.getMessage());
        }
    }

    protected static void submitSavedSurveys(Context context) throws EncryptionException {
        List<File> files = Arrays.asList(context.getFilesDir().listFiles());
        for (File file : files) {
            if (file.getName().startsWith("response")) {
                try {
                    int size = (int) file.length();
                    byte[] bytes = new byte[size];
                    BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
                    in.read(bytes, 0, size);
                    in.close();

                    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
                    PrivateKey privateKey = ((KeyStore.PrivateKeyEntry) getKeys(context)).getPrivateKey();
                    cipher.init(Cipher.DECRYPT_MODE, privateKey);


                    CipherInputStream cipherInputStream =
                            new CipherInputStream(context.openFileInput(file.getName()),
                                    cipher);
                    List<Byte> outBytes = new ArrayList<>();

                    int nextByte;
                    while ((nextByte = cipherInputStream.read()) != -1) {
                        outBytes.add((byte) nextByte);
                    }
                    //String plainText = new String(outBytes, 0, outBytes.size(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new EncryptionException("Failed to decode responses");
                }
            }
        }
    }

    protected void sendString(String string, Context context) {


    }
}
