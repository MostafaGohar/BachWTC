package gohar.mostafa.bachwtc;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.pdf.PdfRenderer;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class SongActivity extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener{

    public int bookNumber;
    public int songNumber;
    public boolean isPrelude;
    public static SongActivity songActivity;

    private ImageButton btn_play_pause;
    private SeekBar seekBar;
//    private TextView textTimer;
    private MediaPlayer mediaPlayer;
    private int mediaFileLength;
    private int realtimeLength;
    final Handler handler = new Handler();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        bookNumber = (int) b.get("BOOK_NUMBER");
        songNumber = (int) b.get("SONG_NUMBER");
        isPrelude = (boolean) b.get("PRELUDE");

        songActivity = this;

        seekBar = (SeekBar)findViewById(R.id.seekbar);
        seekBar.setMax(99); // 100% (0~99)
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mediaPlayer.isPlaying())
                {
                    SeekBar seekBar = (SeekBar)v;
                    int playPosition = (mediaFileLength/100)*seekBar.getProgress();
                    mediaPlayer.seekTo(playPosition);
                    realtimeLength = mediaFileLength - (mediaFileLength/100)*seekBar.getProgress();
                }
                return false;
            }
        });
        //textTimer = (TextView)findViewById(R.id.textTimer);

        btn_play_pause = (ImageButton) findViewById(R.id.btn_play_pause);
        btn_play_pause.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(SongActivity.this);


                @SuppressLint("StaticFieldLeak") AsyncTask<String,String,String> mp3Play = new AsyncTask<String, String, String>() {

                    @Override
                    protected void onPreExecute() {
                        mDialog.setMessage("Loading");
                        mDialog.show();
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        try{

                            mediaPlayer.setDataSource(params[0]);
                            mediaPlayer.prepare();
                            mediaFileLength = mediaPlayer.getDuration();
                            realtimeLength = mediaFileLength;
                        }
                        catch (Exception ex)
                        {

                        }
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {

                        if(!mediaPlayer.isPlaying())
                        {
                            mediaPlayer.start();
                            btn_play_pause.setImageResource(R.mipmap.ic_pause);
                        }
                        else
                        {
                            mediaPlayer.pause();
                            btn_play_pause.setImageResource(R.mipmap.ic_play);
                        }

                        updateSeekBar();
                        mDialog.dismiss();
                    }
                };

                mp3Play.execute(getSongUrl(bookNumber,songNumber,isPrelude)); // direct link mp3 file
            }
        });

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);





//        TextView textView = findViewById(R.id.textView);
//        textView.setText(bookNumber+"_"+songNumber+"_"+isPrelude+"_"+MainActivity.getSongName(songNumber,bookNumber));
        ViewPager mViewPager;
        SlidingTabLayout tabs;

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.canScrollHorizontally(1);
        mViewPager.setAdapter(new SongActivity.MyPagerAdapter(getSupportFragmentManager()));

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(mViewPager);

        LinearLayout masterLayout = findViewById(R.id.masterLayout);








    }

    private String getSongUrl(int bookNumber, int songNumber, boolean isPrelude) {

        String songName = bookNumber+"_"+songNumber+"_";
        if(isPrelude)
            songName+="p";
        else
            songName+="f";
        switch(songName){

            case "1_1_f": return "https://od.lk/s/NjJfNTY1MDc3Ml8/1_1_f.mp3";
            case "1_2_f": return "https://od.lk/s/NjJfNTY1MDc3NV8/1_2_f.mp3";
            case "1_3_f": return "https://od.lk/s/NjJfNTY1MTU0OF8/1_3_f.mp3";
            case "1_4_f": return "https://od.lk/s/NjJfNTY1MTU1MF8/1_4_f.mp3";
            case "1_5_f": return "https://od.lk/s/NjJfNTY1MTU1Ml8/1_5_f.mp3";
            case "1_6_f": return "https://od.lk/s/NjJfNTY1MTU1NF8/1_6_f.mp3";
            case "1_7_f": return "https://od.lk/s/NjJfNTY1MTU1Nl8/1_7_f.mp3";
            case "1_8_f": return "https://od.lk/s/NjJfNTY1MTU1OF8/1_8_f.mp3";
            case "1_9_f": return "https://od.lk/s/NjJfNTY1MTU2MF8/1_9_f.mp3";
            case "1_10_f": return "https://od.lk/s/NjJfNTY1MTU2Ml8/1_10_f.mp3";
            case "1_11_f": return "https://od.lk/s/NjJfNTY1MTU2NF8/1_11_f.mp3";
            case "1_12_f": return "https://od.lk/s/NjJfNTY1MTU2Nl8/1_12_f.mp3";
            case "1_13_f": return "https://od.lk/s/NjJfNTY1MTU2OF8/1_13_f.mp3";
            case "1_14_f": return "https://od.lk/s/NjJfNTY1MTU3MF8/1_14_f.mp3";
            case "1_15_f": return "https://od.lk/s/NjJfNTY1MTU4MV8/1_15_f.mp3";
            case "1_16_f": return "https://od.lk/s/NjJfNTY1MTU4M18/1_16_f.mp3";
            case "1_17_f": return "https://od.lk/s/NjJfNTY1MTU4Nl8/1_17_f.mp3";
            case "1_18_f": return "https://od.lk/s/NjJfNTY1MTYwMV8/1_18_f.mp3";
            case "1_19_f": return "https://od.lk/s/NjJfNTY1MTYwM18/1_19_f.mp3";
            case "1_20_f": return "https://od.lk/s/NjJfNTY1MTYwNl8/1_20_f.mp3";
            case "1_21_f": return "https://od.lk/s/NjJfNTY1MTYwOV8/1_21_f.mp3";
            case "1_22_f": return "https://od.lk/s/NjJfNTY1MTYxMV8/1_22_f.mp3";
            case "1_23_f": return "https://od.lk/s/NjJfNTY1MTU0MV8/1_23_f.mp3";
            case "1_24_f": return "https://od.lk/s/NjJfNTY1MTU0NF8/1_24_f.mp3";
            //Prelude
            case "1_1_p": return "https://od.lk/s/NjJfNTY1MTU0Nl8/1_1_p.mp3";
            case "1_2_p": return "https://od.lk/s/NjJfNTY1MTU0N18/1_2_p.mp3";
            case "1_3_p": return "https://od.lk/s/NjJfNTY1MTU0OV8/1_3_p.mp3";
            case "1_4_p": return "https://od.lk/s/NjJfNTY1MTU1MV8/1_4_p.mp3";
            case "1_5_p": return "https://od.lk/s/NjJfNTY1MTU1M18/1_5_p.mp3";
            case "1_6_p": return "https://od.lk/s/NjJfNTY1MTU1NV8/1_6_p.mp3";
            case "1_7_p": return "https://od.lk/s/NjJfNTY1MTU1N18/1_7_p.mp3";
            case "1_8_p": return "https://od.lk/s/NjJfNTY1MTU1OV8/1_8_p.mp3";
            case "1_9_p": return "https://od.lk/s/NjJfNTY1MTU2MV8/1_9_p.mp3";
            case "1_10_p": return "https://od.lk/s/NjJfNTY1MTU2M18/1_10_p.mp3";
            case "1_11_p": return "https://od.lk/s/NjJfNTY1MTU2NV8/1_11_p.mp3";
            case "1_12_p": return "https://od.lk/s/NjJfNTY1MTU2N18/1_12_p.mp3";
            case "1_13_p": return "https://od.lk/s/NjJfNTY1MTU2OV8/1_13_p.mp3";
            case "1_14_p": return "https://od.lk/s/NjJfNTY1MTU3MV8/1_14_p.mp3";
            case "1_15_p": return "https://od.lk/s/NjJfNTY1MTU4Ml8/1_15_p.mp3";
            case "1_16_p": return "https://od.lk/s/NjJfNTY1MTU4NF8/1_16_p.mp3";
            case "1_17_p": return "https://od.lk/s/NjJfNTY1MTU5MF8/1_17_p.mp3";
            case "1_18_p": return "https://od.lk/s/NjJfNTY1MTYwMl8/1_18_p.mp3";
            case "1_19_p": return "https://od.lk/s/NjJfNTY1MTYwNF8/1_19_p.mp3";
            case "1_20_p": return "https://od.lk/s/NjJfNTY1MTYwN18/1_20_p.mp3";
            case "1_21_p": return "https://od.lk/s/NjJfNTY1MTYxMF8/1_21_p.mp3";
            case "1_22_p": return "https://od.lk/s/NjJfNTY1MTYxMl8/1_22_p.mp3";
            case "1_23_p": return "https://od.lk/s/NjJfNTY1MTU0Ml8/1_23_p.mp3";
            case "1_24_p": return "https://od.lk/s/NjJfNTY1MTU0NV8/1_24_p.mp3";


            case "2_1_f": return "https://od.lk/s/NjJfNTY1MTkxOF8/2_01_f.mp3";
            case "2_2_f": return "https://od.lk/s/NjJfNTY1MTkwOF8/2_02_f.mp3";
            case "2_3_f": return "https://od.lk/s/NjJfNTY1MTkxMF8/2_03_f.mp3";
            case "2_4_f": return "https://od.lk/s/NjJfNTY1MTkxMl8/2_04_f.mp3";
            case "2_5_f": return "https://od.lk/s/NjJfNTY1MTkxNF8/2_05_f.mp3";
            case "2_6_f": return "https://od.lk/s/NjJfNTY1MTkxNl8/2_06_f.mp3";
            case "2_7_f": return "https://od.lk/s/NjJfNTY1MTY0Nl8/2_07_f.mp3";
            case "2_8_f": return "https://od.lk/s/NjJfNTY1MTY0OF8/2_08_f.mp3";
            case "2_9_f": return "https://od.lk/s/NjJfNTY1MTY1MV8/2_09_f.mp3";
            case "2_10_f": return "https://od.lk/s/NjJfNTY1MTY1M18/2_10_f.mp3";
            case "2_11_f": return "https://od.lk/s/NjJfNTY1MTY1NV8/2_11_f.mp3";
            case "2_12_f": return "https://od.lk/s/NjJfNTY1MTY1N18/2_12_f.mp3";
            case "2_13_f": return "https://od.lk/s/NjJfNTY1MTY1OV8/2_13_f.mp3";
            case "2_14_f": return "https://od.lk/s/NjJfNTY1MTY2MV8/2_14_f.mp3";
            case "2_15_f": return "https://od.lk/s/NjJfNTY1MTY3M18/2_15_f.mp3";
            case "2_16_f": return "https://od.lk/s/NjJfNTY1MTY3NV8/2_16_f.mp3";
            case "2_17_f": return "https://od.lk/s/NjJfNTY1MTY3N18/2_17_f.mp3";
            case "2_18_f": return "https://od.lk/s/NjJfNTY1MTY3OV8/2_18_f.mp3";
            case "2_19_f": return "https://od.lk/s/NjJfNTY1MTY4MV8/2_19_f.mp3";
            case "2_20_f": return "https://od.lk/s/NjJfNTY1MTY4M18/2_20_f.mp3";
            case "2_21_f": return "https://od.lk/s/NjJfNTY1MTY4NV8/2_21_f.mp3";
            case "2_22_f": return "https://od.lk/s/NjJfNTY1MTY4OF8/2_22_f.mp3";
            case "2_23_f": return "https://od.lk/s/NjJfNTY1MTY5Ml8/2_23_f.mp3";
            case "2_24_f": return "https://od.lk/s/NjJfNTY1MTY3MF8/2_24_f.mp3";
            //Prelude
            case "2_1_p": return "https://od.lk/s/NjJfNTY1MTkwN18/2_01_p.mp3";
            case "2_2_p": return "https://od.lk/s/NjJfNTY1MTkwOV8/2_02_p.mp3";
            case "2_3_p": return "https://od.lk/s/NjJfNTY1MTkxMV8/2_03_p.mp3";
            case "2_4_p": return "https://od.lk/s/NjJfNTY1MTkxM18/2_04_p.mp3";
            case "2_5_p": return "https://od.lk/s/NjJfNTY1MTkxNV8/2_05_p.mp3";
            case "2_6_p": return "https://od.lk/s/NjJfNTY1MTkxN18/2_06_p.mp3";
            case "2_7_p": return "https://od.lk/s/NjJfNTY1MTY0N18/2_07_p.mp3";
            case "2_8_p": return "https://od.lk/s/NjJfNTY1MTY1MF8/2_08_p.mp3";
            case "2_9_p": return "https://od.lk/s/NjJfNTY1MTY1Ml8/2_09_p.mp3";
            case "2_10_p": return "https://od.lk/s/NjJfNTY1MTY1NF8/2_10_p.mp3";
            case "2_11_p": return "https://od.lk/s/NjJfNTY1MTY1Nl8/2_11_p.mp3";
            case "2_12_p": return "https://od.lk/s/NjJfNTY1MTY1OF8/2_12_p.mp3";
            case "2_13_p": return "https://od.lk/s/NjJfNTY1MTY2MF8/2_13_p.mp3";
            case "2_14_p": return "https://od.lk/s/NjJfNTY1MTY3Ml8/2_14_p.mp3";
            case "2_15_p": return "https://od.lk/s/NjJfNTY1MTY3NF8/2_15_p.mp3";
            case "2_16_p": return "https://od.lk/s/NjJfNTY1MTY3Nl8/2_16_p.mp3";
            case "2_17_p": return "https://od.lk/s/NjJfNTY1MTY3OF8/2_17_p.mp3";
            case "2_18_p": return "https://od.lk/s/NjJfNTY1MTY4MF8/2_18_p.mp3";
            case "2_19_p": return "https://od.lk/s/NjJfNTY1MTY4Ml8/2_19_p.mp3";
            case "2_20_p": return "https://od.lk/s/NjJfNTY1MTY4NF8/2_20_p.mp3";
            case "2_21_p": return "https://od.lk/s/NjJfNTY1MTY4N18/2_21_p.mp3";
            case "2_22_p": return "https://od.lk/s/NjJfNTY1MTY5MF8/2_22_p.mp3";
            case "2_23_p": return "https://od.lk/s/NjJfNTY1MTY5M18/2_23_p.mp3";
            case "2_24_p": return "https://od.lk/s/NjJfNTY1MTY3MV8/2_24_p.mp3";

        }

        return "https://od.lk/s/NjJfNTY1MDc3Ml8/1_1_f.mp3";
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();


        if(mediaPlayer != null && mediaPlayer.isPlaying())
        {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

            boolean screenOn;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                screenOn = pm.isInteractive();
            } else {
                screenOn = pm.isScreenOn();
            }

            if (screenOn) {
                btn_play_pause.performClick();
                // Screen is still on, so do your thing here
            }
            //mediaPlayer.pause();
        }

    }

    private void updateSeekBar() {
        seekBar.setProgress((int)(((float)mediaPlayer.getCurrentPosition() / mediaFileLength)*100));
        if(mediaPlayer.isPlaying())
        {
            Runnable updater = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                    realtimeLength-=1000; // declare 1 second
//                    Log.v("AAAAAAAAAAA",mediaFileLength+"_"+realtimeLength);
//                    long minutes = (mediaFileLength/(1000*60)) - (realtimeLength/(100*60));
//                    long seconds = (TimeUnit.MILLISECONDS.toSeconds(mediaFileLength) -
//                            (TimeUnit.MILLISECONDS.toSeconds(realtimeLength)))
//
//                            -
//                            minutes*60;
//                    String minutesString = minutes+"";
//                    String secondsString = seconds+"";
//                    if(minutes < 10)
//                        minutesString = "0"+minutes;
//                    if(seconds < 10)
//                        secondsString = "0"+seconds;
//                    textTimer.setText(minutesString+":"+secondsString);

                }

            };
            handler.postDelayed(updater,1000); // 1 second
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        btn_play_pause.setImageResource(R.mipmap.ic_play);
        realtimeLength = mediaFileLength;
        seekBar.setProgress(0);

        // musicView.stopNotesFall();

    }




    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            SongActivity.MyFragment myFragment = SongActivity.MyFragment.getInstance(position);
            return myFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ""+(position+1);
        }
    }

    // Instances of this class are fragments representing a single
    // object in our collection.
    public static class MyFragment extends Fragment {
        public static SongActivity.MyFragment getInstance(int position){
            SongActivity.MyFragment myFragment = new SongActivity.MyFragment();
            Bundle args = new Bundle();
            args.putInt("BOOK_NUMBER",SongActivity.getInstance().bookNumber);
            args.putInt("SONG_NUMBER",SongActivity.getInstance().songNumber);
            args.putBoolean("PRELUDE",SongActivity.getInstance().isPrelude);
            args.putInt("position",position);
            myFragment.setArguments(args);
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
            View layout = inflater.inflate(R.layout.pdf_page,container,false);

            Bundle bundle = getArguments();
            int bookNumber = bundle.getInt("BOOK_NUMBER");
            int songNumber = bundle.getInt("SONG_NUMBER");
            boolean isPrelude = bundle.getBoolean("PRELUDE");
            int position = bundle.getInt("position");
            final PDFView pdfView = layout.findViewById(R.id.pdfView);
            String song = songNumber+"";

            String path;
            if(position == 0)
                path = "A/";
            else
                path = "B/";
            if(songNumber < 10)
                song = 0+song;
            path +=bookNumber+"_"+song;
            if(isPrelude)
                path+="_p.pdf";
            else
                path+="_f.pdf";
            pdfView.fromAsset(path)
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .enableAntialiasing(true)
                    .onRender(new OnRenderListener() {
                        @Override
                        public void onInitiallyRendered(int pages, float pageWidth,
                                                        float pageHeight) {
                            pdfView.fitToWidth(); // optionally pass page number
                        }
                    })
                    .load();

            return layout;
        }
    }

    public static SongActivity getInstance(){
        return songActivity;
    }
}
