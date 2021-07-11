package com.example.docxter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Locale;

public class SpeakActivity extends AppCompatActivity {
    TextToSpeech mtts;
    SeekBar pitch;
    SeekBar speed;
    Button button;
    String obtained;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak);
        button=findViewById(R.id.play);
        Intent intent= getIntent();
        obtained=intent.getStringExtra("token");
        mtts= new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i==TextToSpeech.SUCCESS)
                {
                    int result=mtts.setLanguage(Locale.ENGLISH);
                    if(result==TextToSpeech.LANG_MISSING_DATA||result==TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Toast.makeText(getApplicationContext(),"Language not supported ",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        button.setEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Initialisation failed  ",Toast.LENGTH_LONG).show();
                }
            }
        });
        pitch=findViewById(R.id.Pitch);
        speed=findViewById(R.id.Speed);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                talk();
            }
        });
    }

    private void talk() {
        float progPitch= (float)pitch.getProgress()/50;
        float progSpeed= (float)speed.getProgress()/50;
        if(progPitch<0.1)
            progPitch=0.1f;
        if(progSpeed<0.1)
            progSpeed=0.1f;
        mtts.setPitch(progPitch);
        mtts.setSpeechRate(progSpeed);
        mtts.speak(obtained,TextToSpeech.QUEUE_FLUSH,null);

    }

    @Override
    protected void onDestroy() {
        if(mtts!=null)
        {
            mtts.stop();
            mtts.shutdown();
        }
        super.onDestroy();
    }
}
