package com.example.docxter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanActivity extends AppCompatActivity {
    Button scan,search;
    TextView textView;
    SharedPreferences mshare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        mshare=getSharedPreferences("mypref",MODE_PRIVATE);
        textView=findViewById(R.id.output);
        scan=findViewById(R.id.scbtn);
        search =findViewById(R.id.webS);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScanning();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri=Uri.parse("https://www.google.com/search?q="+textView.getText().toString());
                Intent webIntent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(webIntent);
            }
        });

    }

    private void startScanning() {
        IntentIntegrator integrator=new IntentIntegrator(ScanActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setBeepEnabled(true);
        integrator.setPrompt("Scanning Code");
        integrator.setCameraId(0);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null && result.getContents()!=null)
        {
            textView.setText(result.getContents());
            SharedPreferences.Editor editor=mshare.edit();
            editor.putString("result",result.getContents());
            editor.apply();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
