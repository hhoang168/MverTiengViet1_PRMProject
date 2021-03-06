package com.example.tiengviet1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tiengviet1.R;
import com.example.tiengviet1.dto.AlphabetDTO;
import com.example.tiengviet1.dto.ImageDTO;

import java.util.ArrayList;
import java.util.List;

public class AlphabetDetailActivity extends AppCompatActivity {
    private TextView txtChuThuong, txtChuHoa, txtLetter;
    private ImageView imgChuThuong, imgChuHoa;
    private ImageButton imgSound;
    private AlphabetDTO alphabetDTO;
    private String urlChuThuong = "";
    private String urlChuHoa = "";
    private RecyclerView listView;
    private String[] listLetter =
            {"a", "ă", "â", "b", "c", "d", "đ", "e",
                    "ê", "g", "h", "i", "k", "l", "m", "n",
                    "o", "ô", "ơ", "p", "q", "r", "s", "t",
                    "u", "ư", "v", "x", "y",
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alphabet_detail);
        createView();

        Intent intent = this.getIntent();
        alphabetDTO = (AlphabetDTO) intent.getSerializableExtra("dto");
        List<ImageDTO> tmpList = alphabetDTO.getListImages();
        List<ImageDTO> listTuLienQuan = new ArrayList<>();
        for (ImageDTO dto : tmpList) {
            if (dto.getDescription().contains("viet_hoa")) {
                urlChuHoa = dto.getImgPath();
            } else if (dto.getDescription().contains("viet_thuong")) {
                urlChuThuong = dto.getImgPath();
            } else {
                listTuLienQuan.add(dto);
            }
        }

        setUpView();

        MyCustomAdapter adapter = new
                MyCustomAdapter(AlphabetDetailActivity.this,listTuLienQuan,alphabetDTO.getLetter());
        listView.setLayoutManager(new LinearLayoutManager(AlphabetDetailActivity.this,LinearLayoutManager.HORIZONTAL,false));
        listView.setAdapter(adapter);
    }

    private void createView() {
        txtChuThuong = findViewById(R.id.txtChuthuong);
        txtChuHoa = findViewById(R.id.txtChuhoa);
        txtLetter = findViewById(R.id.txtLetter);
        imgChuThuong = findViewById(R.id.imgVietthuong);
        imgChuHoa = findViewById(R.id.imgViethoa);
        imgSound = findViewById(R.id.imgBtnDanhvan);
        listView = findViewById(R.id.listTuLienQuan);
    }

    private void setUpView() {
        txtChuHoa.setText(alphabetDTO.getLetter());
        txtChuThuong.setText(alphabetDTO.getLetter());
        txtLetter.setText(alphabetDTO.getLetter());
        Glide.with(this).load(urlChuHoa).into(imgChuHoa);
        Glide.with(this).load(urlChuThuong).into(imgChuThuong);
    }

    private void playAudio() {
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, Uri.parse(alphabetDTO.getAudioPath()));
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mediaPlayer.start();
    }

    public void clickToPlayAudio(View view) {
        playAudio();
    }

    public void getBack(View view) {
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}

class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.ViewHolder> {
    Context context;
    List<ImageDTO> list;
    String letter;

    public MyCustomAdapter(Context context, List<ImageDTO> list, String letter) {
        this.context = context;
        this.list = list;
        this.letter = letter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alphabet_relation_word, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = list.get(position).getImgPath();
        Glide.with(context).load(url).into(holder.imgHinh);
        String tmpText = list.get(position).getDescription();
        tmpText = tmpText.substring(tmpText.indexOf("_")+1,tmpText.length());
        int letterIndex = tmpText.indexOf(letter);
        SpannableString ss = new SpannableString(tmpText);
        ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);

        ss.setSpan(fcsRed,letterIndex,letterIndex+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txtMota.setText(ss);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinh;
        TextView txtMota;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinh = itemView.findViewById(R.id.imgHinhLienQuan);
            txtMota = itemView.findViewById(R.id.txtMoTa);
        }
    }
}