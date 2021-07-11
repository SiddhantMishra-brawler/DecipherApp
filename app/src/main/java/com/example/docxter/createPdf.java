package com.example.docxter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class createPdf extends AppCompatActivity {
    WebView webView;
    Button mbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pdf);
        webView=findViewById(R.id.pdfView);
        Intent intent= getIntent();
        mbutton=findViewById(R.id.createPdf);
        String res=intent.getStringExtra("pdftext");
        String h3=intent.getStringExtra("pdfhead");
        String html="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "<h3 id=\"header\">"+h3+"</h3>\n" +
                "<p id=\"para\">"+res+"</p>\n" +
                "</body>\n" +
                "</html>";
        webView.loadDataWithBaseURL(null,html,"text/html","utf-8",null);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePdf();
            }
        });
    }

    private void makePdf() {
        Context context=createPdf.this;
        PrintManager printManager=(PrintManager)createPdf.this.getSystemService(context.PRINT_SERVICE);
        PrintDocumentAdapter adapter=null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
        {
            adapter=webView.createPrintDocumentAdapter();
        }
        String job=getString(R.string.app_name)+"Document";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
        {
            PrintJob printJob=printManager.print(job,adapter,new PrintAttributes.Builder().build());
        }
    }

}
