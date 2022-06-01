package com.example.moprouebung4;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import android.media.MediaPlayer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {
    String pfad;
    File files[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    double startTime = 0;
    double finalTime = 0;
    int oneTimeOnly = 0;
    Handler myHandler = new Handler();;
    MediaPlayer mp;
    SeekBar sb;
    Button stop;

    public void onSuchen(View v) throws InvalidDataException, UnsupportedTagException, IOException {
        EditText etPfad = (EditText)findViewById(R.id.idPfad);
        pfad = etPfad.getText().toString();
        File dir = new File(pfad);
        files = dir.listFiles();
        if(files == null){
            Toast toast = Toast.makeText(this, "Keine Dateien gefunden unter " + pfad, Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        ArrayList<ID3v2> tagList = new ArrayList<ID3v2>(files.length);
        for(int i=0; i<files.length; i++) {
            if(files[i].getName().endsWith("mp3")){
            Mp3File mp3file = new Mp3File(pfad + "/" + files[i].getName());
            if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                tagList.add(id3v2Tag);
            }
            }
        }

        musikadapter adapter = new musikadapter(this,tagList);
        ListView listView = ((ListView)findViewById(R.id.textListe));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        mp = new MediaPlayer();
                        mp.setDataSource(pfad + "/" + files[i].getName());
                        mp.prepare();
                        mp.start();
                        sb = (SeekBar) findViewById(R.id.seekBar);
                        sb.setVisibility(View.VISIBLE);
                        finalTime = mp.getDuration();
                        startTime = mp.getCurrentPosition();
                        stop= (Button)findViewById(R.id.buttonStop);
                        if (oneTimeOnly == 0) {
                            sb.setMax((int) finalTime);
                            oneTimeOnly = 1;
                        }
                        sb.setProgress((int)startTime);
                        myHandler.postDelayed(UpdateSongTime,100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        });

    }
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mp.getCurrentPosition();
            sb.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };
    public void stopmusic(View view){
        mp.stop();
    }
}
