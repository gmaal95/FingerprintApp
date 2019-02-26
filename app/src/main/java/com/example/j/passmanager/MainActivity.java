package com.example.j.passmanager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    private KeyStore keyStore;
    private static final String KEY_NAME= "jamaldeen";
    private Cipher cipher;
    private TextView textView;



    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager)getSystemService(FINGERPRINT_SERVICE);

        if(!fingerprintManager.isHardwareDetected()){
            Toast.makeText(this,"Fingerprint Authentication not enabled in this device",Toast.LENGTH_SHORT).show();}
            else{
                if(!fingerprintManager.hasEnrolledFingerprints()){
                   Toast.makeText(this,"Register atleast one fingerprint in settings",Toast.LENGTH_SHORT).show();}
                   else{
                       if(!keyguardManager.isKeyguardSecure()){
                           Toast.makeText(this,"Lockscreen not enabled in settings",Toast.LENGTH_SHORT).show();}

                           else {
                               genKey();
                           if(CipherInit()){

                               FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                               FingerprintHandler  helper = new FingerprintHandler(this);
                               helper.startAuthentication(fingerprintManager,cryptoObject);




                           }
                       }
                    }
                }
            }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void genKey() {

        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator = null;

        try {
            keyGenerator=KeyGenerator.getInstance("AndroidKeyStore");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            keyStore.load(null);

            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,KeyProperties.PURPOSE_ENCRYPT|KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build());

            keyGenerator.generateKey();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        catch (InvalidAlgorithmParameterException e){
            e.printStackTrace();
        }





    }



    private boolean CipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();

            try {
                keyStore.load(null);
                SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
                cipher.init(Cipher.ENCRYPT_MODE, key);
                return true;

            } catch (IOException e1) {
                e1.printStackTrace();
                return false;
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
                return false;
            } catch (CertificateException e1) {

                e1.printStackTrace();
                return false;
            } catch (UnrecoverableKeyException e1) {

                e1.printStackTrace();
                return false;
            } catch (KeyStoreException e1) {

                e1.printStackTrace();
                return false;
            } catch (InvalidKeyException e1) {
                return false;
            }

    }


        return false;
    }
}
