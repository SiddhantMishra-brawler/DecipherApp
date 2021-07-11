package com.example.docxter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;



public class HomeActivity extends AppCompatActivity {
    ImageView required;
    CardView read,translate,storage,work,scan,history;
    private static final int gallerycode=4;
    String important=null,tglang;
    String langCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        required=findViewById(R.id.sample);
        read=findViewById(R.id.readC);
        translate=findViewById(R.id.transC);
        storage=findViewById(R.id.storageC);
        work=findViewById(R.id.Speak);
        scan=findViewById(R.id.scanner);
        history=findViewById(R.id.History);
        storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,gallerycode);
            }
        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(important==null)
                {
                    Toast.makeText(HomeActivity.this,"First Add an image from storage",Toast.LENGTH_LONG).show();
                    return;
                }
                AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Text contained in the Image")
                        .setMessage(important)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                builder.setNegativeButton("Listen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(HomeActivity.this,SpeakActivity.class);
                        intent.putExtra("token",important);
                        startActivity(intent);
                    }
                });
                builder.create().show();
            }
        });

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(important==null)
                {
                    Toast.makeText(HomeActivity.this,"First Add an image from storage",Toast.LENGTH_LONG).show();
                    return;
                }
                final String list[]=new String[]{"Hindi","Kannada","Tamil","Marathi","Urdu"};
                final AlertDialog.Builder chooser=new AlertDialog.Builder(HomeActivity.this);
                chooser.setTitle("Select a language for translation ")
                        .setIcon(R.drawable.ic_translate_black_24dp);
                chooser.setSingleChoiceItems(list, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tglang=list[i];
                        Toast.makeText(HomeActivity.this, "You selected "+list[i], Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();
                        if(tglang.equals("Hindi"))
                        {
                            langCode=TranslateLanguage.HINDI;
                        }
                        else if(tglang.equals("Kannada"))
                        {
                            langCode=TranslateLanguage.KANNADA;
                        }
                        else if(tglang.equals("Tamil"))
                        {
                            langCode=TranslateLanguage.TAMIL;
                        }
                        else if(tglang.equals("Marathi"))
                        {
                            langCode=TranslateLanguage.MARATHI;
                        }
                        else if(tglang.equals("Urdu"))
                        {
                            langCode=TranslateLanguage.URDU;
                        }
                        TranslatorOptions options =
                                new TranslatorOptions.Builder()
                                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                                        .setTargetLanguage(langCode)
                                        .build();
                        final Translator englishGermanTranslator =
                                Translation.getClient(options);

                        DownloadConditions conditions = new DownloadConditions.Builder()
                                .build();
                        englishGermanTranslator.downloadModelIfNeeded(conditions)
                                .addOnSuccessListener(
                                        new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void v) {
                                                englishGermanTranslator.translate(important)
                                                        .addOnSuccessListener(
                                                                new OnSuccessListener<String>() {
                                                                    @Override
                                                                    public void onSuccess(@NonNull final String translatedText) {
                                                                        AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
                                                                        builder.setTitle("Translated Text in "+tglang+" ")
                                                                                .setMessage(translatedText)
                                                                                .setIcon(R.drawable.ic_translate_black_24dp)
                                                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                                                    }
                                                                                });
                                                                        builder.setNegativeButton("Create Pdf", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                                Intent pdfintent= new Intent(HomeActivity.this,createPdf.class);
                                                                                pdfintent.putExtra("pdftext",translatedText);
                                                                                pdfintent.putExtra("pdfhead","Translated Text in "+tglang+" ");
                                                                                startActivity(pdfintent);
                                                                            }
                                                                        });
                                                                        builder.create().show();
                                                                    }
                                                                });
                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(),"Model can't be downloaded",Toast.LENGTH_LONG).show();
                                            }
                                        });



                    }
                });

                chooser.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                chooser.create().show();

            }
        });
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,Camera.class);
                startActivity(intent);
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeActivity.this,ScanActivity.class);
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hisintent=new Intent(HomeActivity.this,Scanhistory.class);
                startActivity(hisintent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.feedback)
        {
            Intent intent=new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto: smishra.in99@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback for Decipher Android App");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==gallerycode && resultCode==RESULT_OK)
        {

            CropImage.activity(data.getData())
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                required.setImageURI(resultUri);

                BitmapDrawable drawable=(BitmapDrawable)required.getDrawable();
                Bitmap bitmap=drawable.getBitmap();
                TextRecognizer recognizer=new TextRecognizer.Builder(getApplicationContext()).build();
                if(!recognizer.isOperational())
                {
                    Toast.makeText(getApplicationContext(),"Failed to read text",Toast.LENGTH_LONG).show();
                }
                else {
                    Frame frame=new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock>items =recognizer.detect(frame);
                    StringBuilder sb=new StringBuilder();
                    for(int i=0;i<items.size();i++)
                    {
                        TextBlock my=items.valueAt(i);
                        sb.append(my.getValue());
                        sb.append("\n");
                    }
                    important=sb.toString();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
