package com.example.myapplication;

import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageButton;
import android.media.MediaPlayer;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ImageButton playPauseButton;
    private boolean isPlaying = false;
    private final String RADIO_URL = "https://stream.zeno.fm/92kpcxvwegvvv"; // Cambia esta URL por la de tu radio

    private AudioManager audioManager;
    private SeekBar seekBarVolume;
    private ImageButton buttonVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playPauseButton = findViewById(R.id.buttonPlayPause);
        View facebook = findViewById(R.id.facebook);
        View whatsapp = findViewById(R.id.whatsapp);
        View youtube = findViewById(R.id.youtube);
        View pagina = findViewById(R.id.pagina);

        mediaPlayer = new MediaPlayer();

        playPauseButton.setOnClickListener(v -> togglePlayPause());
        facebook.setOnClickListener(v -> openLink("https://www.facebook.com/tu_perfil"));
        whatsapp.setOnClickListener(v -> openLink("https://twitter.com/tu_perfil"));
        youtube.setOnClickListener(v -> openLink("https://www.instagram.com/tu_perfil"));
        pagina.setOnClickListener(v -> openLink("https://www.linkedin.com/in/tu_perfil"));

        // Inicializar el AudioManager para controlar el volumen
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Obtener referencias al ImageButton y al SeekBar
        buttonVolume = findViewById(R.id.buttonVolume);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        // Configurar el SeekBar para que coincida con el volumen actual del dispositivo
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBarVolume.setMax(maxVolume);
        seekBarVolume.setProgress(currentVolume);

        // Mostrar u ocultar el SeekBar al hacer clic en el botón
        buttonVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBarVolume.getVisibility() == View.GONE) {
                    seekBarVolume.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Control de volumen visible", Toast.LENGTH_SHORT).show();
                } else {
                    seekBarVolume.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Control de volumen oculto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Controlar el volumen con el SeekBar
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // Ajustar el volumen del dispositivo según el progreso del SeekBar
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // No es necesario implementar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // No es necesario implementar
            }
        });
    }




    private void togglePlayPause() {
        if (isPlaying) {
            stopRadio();
            playPauseButton.setImageResource(R.drawable.play); // Cambiar a imagen de Play
        } else {
            playRadio();
            playPauseButton.setImageResource(R.drawable.pausa); // Cambiar a imagen de Pause
        }
        isPlaying = !isPlaying;
    }

    private void playRadio() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(RADIO_URL);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRadio() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}