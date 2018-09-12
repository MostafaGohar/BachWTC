package gohar.mostafa.bachwtc;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.ParcelFileDescriptor;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SongActivity extends AppCompatActivity {

    public int bookNumber;
    public int songNumber;
    public boolean isPrelude;
    public static SongActivity songActivity;


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


//        TextView textView = findViewById(R.id.textView);
//        textView.setText(bookNumber+"_"+songNumber+"_"+isPrelude+"_"+MainActivity.getSongName(songNumber,bookNumber));
        ViewPager mViewPager;
        SlidingTabLayout tabs;

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new SongActivity.MyPagerAdapter(getSupportFragmentManager()));

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(mViewPager);

        LinearLayout masterLayout = findViewById(R.id.masterLayout);








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
            PDFView pdfView = (PDFView) layout.findViewById(R.id.pdfView);
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
                    .load();

            return layout;
        }
    }

    public static SongActivity getInstance(){
        return songActivity;
    }
}
