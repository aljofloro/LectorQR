package com.desarrollo.lectorqr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configurarLector();
    }

    private void configurarLector(){
        ((ImageButton)findViewById(R.id.img_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    new IntentIntegrator(MainActivity.this).initiateScan();
                }else{
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults){
        switch (requestCode){
            case REQUEST_CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    new IntentIntegrator(MainActivity.this).initiateScan();
                    break;
                }
        }
    }

    private void actualizarTextViews(String scaneoResultado, String scaneoFormato){
        ((TextView)findViewById(R.id.tvResultado)).setText(scaneoResultado);
        ((TextView)findViewById(R.id.tvFormato)).setText(scaneoFormato);
    }

    private void recibirLectura(IntentResult intentResult){
        if(intentResult != null){
            actualizarTextViews(intentResult.getContents(),intentResult.getFormatName());
        }else{
            Toast.makeText(getApplicationContext(),"No hubo lectura",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        final IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(intentResult != null){
            recibirLectura(intentResult);
        }else{
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }
}
