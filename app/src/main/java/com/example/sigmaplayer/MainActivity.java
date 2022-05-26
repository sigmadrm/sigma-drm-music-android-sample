package com.example.sigmaplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import com.audioplay.audio.SigmaPlayer;
import com.audioplay.audio.SigmaPlayerEventsListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SigmaPlayerEventsListener {
    private String MEDIA_URI[] = {
            "https://pgdvqbnmsaobj.vcdn.cloud/mediahub/output/Kc4rW-SFUxl-C2Ddtwruh-khen-mo/mp3/128kbps.mp3",
            "https://pgdvqbnmsaobj.vcdn.cloud/mediahub/output/0dhE6y5f79TCDRJgmYaQG-loi-ru-ngan-xua-ho-quynh-huong/mp3/128kbps.mp3",
            "https://pgdvqbnmsaobj.vcdn.cloud/mediahub/output/RgrB0P9xMO51IxEmKYKuL-menh-mang-coi-thieu-anh-tho/mp3/128kbps.mp3",
            "https://pgdvqbnmsaobj.vcdn.cloud/mediahub/output/YIuCtGsv2c3thlX4TOYos-ninh-binh-ngay-ve-thuy-chi/mp3/128kbps.mp3",
            "https://pgdvqbnmsaobj.vcdn.cloud/mediahub/output/pIJy_6gG-DK_UK3EMIIGh-me-oi-con-muon-tro-ve-ngoc-anh/mp3/128kbps.mp3",
            "https://pgdvqbnmsaobj.vcdn.cloud/mediahub/output/w5RElq0Fr_JJBZA7eLDNd-hay-nhuom-gio-bien-khoi/mp3/128kbps.mp3",
            "/data/user/0/com.example.sigmaplayer/files/khen_mo.mp3",
            "/storage/self/primary/Download/other_file_test.mp3",
    };

    private Button _playBtn, _nextBtn, _saveThisFileBtn, _backBtn, _saveOtherFileBtn;
    private SeekBar _simpleSeekBar, _seekbarSlider;
    private TextView _durationLabel, _currentTimeLabel, _urlLabel, _saveOtherFileProgressLabel;
    private EditText _downloadUriInput;
    private String _downloadUriString;
    private int _currentIndex = -1;
    private ContextWrapper _context;

    SigmaPlayer _smPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        permissionsWrite();
        renderPlayer();
        renderBackBtn();
        renderNextBtn();
        renderNextBtn();
        renderSaveFileBtn();
        renderDownloadUriInput();
        renderSaveOtherFileBtn();
        renderLoopCheckBox();
        renderVolumeSlider();
        renderLabels();

        _context = new ContextWrapper(this);
        _smPlayer = SigmaPlayer.getInstance();
        // _smPlayer.setAppConfig("sctv", "RedTV", "123456797", "12346578");
        _smPlayer.setContext(_context);
        _smPlayer.setAppConfig("thudojsc", "MediaHub", "g-5F6QRRAEDK",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzZGkiOiJ7XCJ1c2VyXCI6XCJnLTVGNlFSUkFFREtcIixcIm1lcmNoYW50XCI6XCJ0aHVkb2pzY1wiLFwiYXNzZXRJZFwiOlwiNjE4OGVhMmUxNDBkYmM0YmE0NmQ2MWU2XCJ9IiwiaWF0IjoxNjM5NjQzMjE3LCJleHAiOjE2Mzk2NDY4MTd9._qFz6l_3e-6tBu_D38bPrY_B9sZNqpwq9vgiIZXENrU");
        _smPlayer.addEventsListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        long sizeHeap = Runtime.getRuntime().maxMemory();
        Toast.makeText(MainActivity.this, "Size Heap :" + sizeHeap,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void renderLabels() {
        _durationLabel = (TextView) findViewById(R.id.durationLabel);
        _durationLabel.setGravity(Gravity.CENTER_VERTICAL);
        _durationLabel.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

        _urlLabel = (TextView) findViewById(R.id.urlLabel);
        _currentTimeLabel = (TextView) findViewById(R.id.currentTimeLabel);
        _currentTimeLabel.setGravity(Gravity.CENTER_VERTICAL);
        _saveOtherFileProgressLabel = (TextView) findViewById(R.id.saveOtherFileProgress);

        _seekbarSlider = (SeekBar) findViewById(R.id.seekBar2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _seekbarSlider.setMin(0);
        }
        _seekbarSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int currentTime = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentTime = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("SigmaMusic DRM: ", "seek = " + currentTime);
                _smPlayer.setCurrentTime(currentTime);
            }
        });
    }

    private void getCurrentTineInterval() {
        final Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                double currentTime = _smPlayer.getCurrentTime();
                try {

                } catch (Exception e) {
                }
                handler.postDelayed(this, 200);
            }
        };
        handler.post(runnable);
    }

    private void renderVolumeSlider() {
        _simpleSeekBar = (SeekBar) findViewById(R.id.volumeSlider);
        _simpleSeekBar.setMax(10);
        _simpleSeekBar.setProgress(10);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _simpleSeekBar.setMin(0);
        }
        _simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                _smPlayer.setVolume((float) progressChangedValue / 10);
                Toast.makeText(MainActivity.this, "Volume :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void playWithIndex(int currentIndex) {
        String url = MEDIA_URI[currentIndex];
        _urlLabel.setText(url);
        _smPlayer.play(url);
    }

    private void renderPlayer() {
        _playBtn = (Button) findViewById(R.id.playBtn);
        _playBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (_smPlayer.isPlaying()) {
                    _smPlayer.pause();
                } else {
                    _smPlayer.resume();
                }
                // getCurrentTineInterval();
                _playBtn.setEnabled(false);
                _playBtn.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        _playBtn.setEnabled(true);
                    }
                }, 20);
            }
        });
    }

    private void permissionsWrite() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }

    private void renderLoopCheckBox() {
        CheckBox chk = (CheckBox) findViewById(R.id.loopCheckBox);
        chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if (checked) {
                    _smPlayer.setLoop(true);
                } else {
                    _smPlayer.setLoop(false);
                }
            }
        });
    }

    private void renderNextBtn() {
        _nextBtn = (Button) findViewById(R.id.nextBtn);
        _nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                _currentIndex++;
                if (_currentIndex >= MEDIA_URI.length) {
                    _currentIndex = 0;
                }
                playWithIndex(_currentIndex);
            }
        });
    }

    private void renderBackBtn() {
        _backBtn = (Button) findViewById(R.id.backBtn);
        _backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                _currentIndex--;
                if (_currentIndex < 0) {
                    _currentIndex = MEDIA_URI.length - 1;
                }
                playWithIndex(_currentIndex);
            }
        });
    }

    private void renderDownloadUriInput() {
        _downloadUriInput = (EditText) findViewById(R.id.downloadUriInput);
        String defaultUri = "https://pgdvqbnmsaobj.vcdn.cloud/mediahub/output/w5RElq0Fr_JJBZA7eLDNd-hay-nhuom-gio-bien-khoi/mp3/128kbps.mp3";
        _downloadUriInput.setText(defaultUri);
        _downloadUriString = defaultUri;
        _downloadUriInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                _downloadUriString = editable.toString();
            }
        });
    }

    String getFilesDirPath() {
        return _context.getFilesDir().getAbsolutePath();
    }

    private void renderSaveFileBtn() {
        _saveThisFileBtn = (Button) findViewById(R.id.saveThisFileBtn);
        _saveThisFileBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (_smPlayer != null) {
                    String filePath = getFilesDirPath() + "/khen_mo.mp3";
                    _smPlayer.saveFile(filePath);
                }
            }
        });
    }

    String getDownloadPath() {
        File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String downloadPath = downloadFolder.getPath();
        return downloadPath;
    }

    private void renderSaveOtherFileBtn() {
        _saveOtherFileBtn = (Button) findViewById(R.id.saveOtherFileBtn);
        _saveOtherFileBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (_smPlayer != null) {
                    String filePath = getDownloadPath() + "/other_file_test.mp3";
                    _smPlayer.saveOtherFile(_downloadUriString, filePath);
                }
            }
        });
    }

    @Override
    public void onLoadStart() {
        Log.e("SigmaMusic DRM", "onLoadStart");
    }

    @Override
    public void onProgress(double percent) {
        Log.e("SigmaMusic DRM", "onProgress " + " percent: " + percent + "%");
    }

    @Override
    public void onCanPlay() {
        Log.e("SigmaMusic DRM", "onCanPlay");
    }

    @Override
    public void onDurationChange(int duration) {
        this._seekbarSlider.setMax(duration);
        this._durationLabel.setText(duration + "s");
        Log.e("SigmaMusic DRM", "onDurationChange " + duration);
    }

    @Override
    public void onLoaded() {
        Log.e("SigmaMusic DRM", "onLoaded");
    }

    @Override
    public void onTimeUpdate(int currentTime) {
//        Log.e("SigmaMusic DRM", "onTimeUpdate " + currentTime);
        _currentTimeLabel.setText((int) currentTime + "s");
        _seekbarSlider.setProgress((int) currentTime);
    }

    @Override
    public void onStateChange(int state) {
        if (state == SigmaPlayer.PLAYER_STATE.PLS_INIT.ordinal()) {
            Log.e("SigmaMusic DRM", "onStateChange PLS_INIT");
        }
        if (state == SigmaPlayer.PLAYER_STATE.PLS_RUNNING.ordinal()) {
            Log.e("SigmaMusic DRM", "onStateChange PLS_RUNNING");
        }
        if (state == SigmaPlayer.PLAYER_STATE.PLS_SUSPENDED.ordinal()) {
            Log.e("SigmaMusic DRM", "onStateChange PLS_SUSPENDED");
        }
        if (state == SigmaPlayer.PLAYER_STATE.PLS_LOADING.ordinal()) {
            Log.e("SigmaMusic DRM", "onStateChange PLS_LOADING");
        }
        if (state == SigmaPlayer.PLAYER_STATE.PLS_STOPPED.ordinal()) {
            Log.e("SigmaMusic DRM", "onStateChange PLS_STOPPED");
        }
    }

    @Override
    public void onEnded() {
        Log.e("SigmaMusic DRM", "onEnded");
    }

    @Override
    public void onError(String error) {
        Log.e("SigmaMusic DRM", "onError " + error);
    }

    @Override
    public void onFileSavingProgress(String uri, String filePath, double percent){
        Log.e("SigmaMusic DRM", "onFileSavingProgress percent " + percent);
        _saveOtherFileProgressLabel.setText(Math.ceil(percent * 100) / 100 + " %");
    }

    @Override
    public void onFileSaved(String uri, String filePath) {
        Toast toast = Toast.makeText(_context,"onFileSaved: " + filePath,Toast.LENGTH_SHORT);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();

        Log.e("SigmaMusic DRM", "onFileSaved " + uri + "File path: " + filePath);
    }

}
