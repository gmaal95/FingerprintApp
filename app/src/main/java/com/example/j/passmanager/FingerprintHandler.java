package com.example.j.passmanager;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.M)
class FingerprintHandler extends FingerprintManager.AuthenticationCallback{

    private Context context;
    public FingerprintHandler(Context context){
        this.context=context;
    }
    @TargetApi(Build.VERSION_CODES.M)

    public void startAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT)!= PackageManager.PERMISSION_GRANTED){
            fingerprintManager.authenticate(cryptoObject,cancellationSignal,0,this,null);
            return;

        }
    }
    @Override
    public void onAuthenticationFailed(){
        super.onAuthenticationFailed();
        Toast.makeText(context,"authentication failed",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result){
        super.onAuthenticationSucceeded(result);
        context.startActivity(new Intent(context,Homepage.class));
    }

}
