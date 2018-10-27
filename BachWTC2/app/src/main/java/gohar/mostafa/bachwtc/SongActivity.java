package gohar.mostafa.bachwtc;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
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
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

import static android.app.Notification.DEFAULT_ALL;
import static android.app.Notification.DEFAULT_VIBRATE;
import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class SongActivity extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener{

    boolean isPlaying;
    public ArrayList<PDFView> pdfViews = new ArrayList<PDFView>();

    public static int deviceWidth;
    public static int deviceHeight;

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
    public Handler seekbarHandler =  new Handler();

    public Handler mHandler =  new Handler();
    public Runnable mRunnable;


    ViewPager mViewPager;
    SlidingTabLayout tabs;


    private LinearLayout player;
    private RelativeLayout masterLayout;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_song);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        deviceWidth = size.x;
        deviceHeight = size.y;


        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        bookNumber = (int) b.get("BOOK_NUMBER");
        songNumber = (int) b.get("SONG_NUMBER");
        isPrelude = (boolean) b.get("PRELUDE");
        player = (LinearLayout) findViewById(R.id.player);
        //SET PLAYER SIZE BASED ON PORTRAIT OR LANDSCAPE MODE
        player.post(new Runnable() {
            @Override
            public void run() {
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // In landscape
                    player.setMinimumHeight(((View)player.getParent()).getHeight()/8);
                } else {
                    // In portrait
                    player.setMinimumHeight(((View)player.getParent()).getHeight()/15);
                }
            }
        });
        masterLayout = (RelativeLayout) findViewById(R.id.masterLayout);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);


        songActivity = this;
        mediaPlayer = new MediaPlayer();

        seekBar = (SeekBar) findViewById(R.id.seekbar);
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
        mHandler =new Handler();

        mRunnable=new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                player.animate().translationY(player.getHeight());
                tabs.animate().translationY(-tabs.getHeight());
               // player.setVisibility(View.INVISIBLE); //This will remove the View. and free s the space occupied by the View
            }
        };
        mHandler.postDelayed(mRunnable,2*1000);






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

//                        Intent snoozeIntent = new Intent(getApplicationContext(), SongActivity.class);
//                        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
//                        PendingIntent snoozePendingIntent =
//                                PendingIntent.getBroadcast(getApplicationContext(), 0, snoozeIntent, 0);
//
//
//                                      //  .setContentIntent(); //Required on Gingerbread and below
//                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
//                                .setSmallIcon(R.mipmap.ic_pause)
//                                .setContentTitle("Book "+bookNumber+", ")
//                                .setContentText("Hello World!")
//                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                                .addAction(R.mipmap.ic_pause, "Pause",
//                                        snoozePendingIntent);
//
//                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                        assert notificationManager != null;
//                        notificationManager.notify(1, mBuilder.build());
//                                //.setContentIntent(pendingIntent)
//                                //.addAction(R.drawable.ic_snooze, getString(R.string.snooze),
//                                //        snoozePendingIntent);
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

        }}
        );



        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);





//        TextView textView = findViewById(R.id.textView);
//        textView.setText(bookNumber+"_"+songNumber+"_"+isPrelude+"_"+MainActivity.getSongName(songNumber,bookNumber));




        mViewPager.canScrollHorizontally(1);
        mViewPager.setAdapter(new SongActivity.MyPagerAdapter(getSupportFragmentManager()));

        // Assiging the Sliding Tab Layout View
        tabs.setDistributeEvenly(true);

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(mViewPager);









    }
//     player.setVisibility(View.VISIBLE);
//                mHandler.removeCallbacksAndMessages(null);
//                mHandler.postDelayed(mRunnable, 3 * 1000);
//                return false;

    public void onUserInteraction(){
        super.onUserInteraction();
        player.animate().translationY(0);
        tabs.animate().translationY(0);

        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(mRunnable, 2 * 1000);
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


        Log.v("RESSSSSSSSSSSS","SSSSSSSSSSSSSSSSSSS");
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
        }

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            for(int i = 0 ;i<pdfViews.size();i++){
                pdfViews.get(i).zoomTo((deviceHeight*1f)/(deviceWidth*1f)+0.3f);
            }
            Log.e("On Config Change","LANDSCAPE");
        }else{
            for(int i = 0 ;i<pdfViews.size();i++){
                pdfViews.get(i).resetZoom();
            }
            Log.e("On Config Change","PORTRAIT");
        }

    }


    private void updateSeekBar() {
        Log.v("XXXXXXXXXXXXXXXXXXXXXX",(int)(((float)mediaPlayer.getCurrentPosition() / mediaFileLength)*100)+"");
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
            seekbarHandler.postDelayed(updater,1000); // 1 second
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

//
//    @Override
//    protected void onSaveInstanceState(Bundle outState)
//    {
//        outState.putInt("position", mediaPlayer.getCurrentPosition());
//        mediaPlayer.pause();
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState)
//    {
//        int pos = savedInstanceState.getInt("position");
//        mediaPlayer.seekTo(pos);
//        seekBar.setProgress((int)(((float)pos / mediaFileLength)*100));
//        if(isPlaying){
//            btn_play_pause.setImageResource(R.mipmap.ic_pause);
//        }
//        super.onRestoreInstanceState(savedInstanceState);
//    }

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
            songActivity.pdfViews.add(pdfView);
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
