package gohar.mostafa.bachwtc;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static MainActivity mainActivity;
    public static int _bookNumber = -1;
    public static int _keyNumber = -1;

    public static int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DISABLE AUTO ADJUST FONT ON ANDROID
        adjustFontScale(getResources().getConfiguration());
        //
        count = 1;

        mainActivity = this;
//        toolbar = (Toolbar) findViewById(R.id.app_bar);
//        toolbar.setTitle("");
//        setSupportActionBar(toolbar);


        ViewPager mViewPager;
//        final SlidingTabLayout tabs;

        //tabs = findViewById(R.id.tabs);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

    }


    class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            MyFragment myFragment = MyFragment.getInstance(position);
            return myFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return "Book "+(position+1);
        }
    }

    // Instances of this class are fragments representing a single
    // object in our collection.
    public static class MyFragment extends Fragment {
        public static MyFragment getInstance(int position){
            MyFragment myFragment = new MyFragment();
            Bundle args = new Bundle();
            args.putInt("position",position);
            myFragment.setArguments(args);
            return myFragment;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState){
            final View layout = inflater.inflate(R.layout.songs_page,container,false);
            count = 1;
            final LinearLayout containerLayout = layout.findViewById(R.id.container_layout);

            Bundle bundle = getArguments();
            final int bookNumber = bundle.getInt("position")+1;

            Drawable drawable;
            if(bookNumber == 1) {
                drawable = mainActivity.resizeImage(R.drawable.wallz11);
            }
            else {
                drawable = mainActivity.resizeImage(R.drawable.wallz22);
            }
            containerLayout.post(new Runnable() {
                @Override
                public void run() {
                    Log.v("CCCCCCCCCC",containerLayout.getHeight()+"_"+containerLayout.getWidth()); //height is ready
                    if(bookNumber == 1){
                        containerLayout.setPadding((int) (containerLayout.getWidth()*0.1),(int)(containerLayout.getHeight()*0.05),(int)(containerLayout.getWidth()*0.05),(int)(containerLayout.getHeight()*0.05));

                    }else{
                        containerLayout.setPadding((int) (containerLayout.getWidth()*0.05),(int)(containerLayout.getHeight()*0.05),(int)(containerLayout.getWidth()*0.1),(int)(containerLayout.getHeight()*0.05));

                    }

                }
            });
            containerLayout.setBackground(drawable);

            for(int i = 0;i<6;i++){
                for(int j = 0;j<4;j++) {
                    //Get actual count as if it were one loop

                    final LinearLayout linearLayout = new LinearLayout(getContext());
                    final LinearLayout l1 = new LinearLayout((getContext()));
                    final LinearLayout l2 = new LinearLayout((getContext()));
                    final LinearLayout PFLayout = new LinearLayout(getContext());

                    PFLayout.setTag("PFLayout_"+bookNumber+"_"+count);
                    l1.setTag("keyLayout_"+bookNumber+"_"+count);
                    Log.v("PFPFPF","PFLayout_"+bookNumber+"_"+count);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    final ImageView imageView = new ImageView(getContext());
                    AutoResizeTextView textView = new AutoResizeTextView(getContext());
                    Typeface type = Typeface.createFromAsset(getContext().getAssets(),"fonts/free_bi.ttf");
                    textView.setTypeface(type);


                    imageView.setAlpha(0.8f);
                    textView.setAlpha(0.8f);

                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setPadding((int)(imageView.getWidth()*0.09)
                                    ,(int)(imageView.getWidth()*0.09)
                                    ,(int)(imageView.getWidth()*0.09)
                                    ,(int)(imageView.getWidth()*0.09));
                        }
                    });
                    imageView.setImageResource(getKeySignatureDrawable(getContext(), count));

                    textView.setText(getSongName(count, bookNumber));
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.BLACK);


                    final TextView preludeLayout = new TextView(getContext());
                    final TextView fugueLayout = new TextView(getContext());
                    preludeLayout.setText("P");
                    fugueLayout.setText("F");
                    preludeLayout.setTextColor(Color.BLACK);
                    fugueLayout.setTextColor(Color.BLACK);
                    fugueLayout.setGravity(Gravity.CENTER);
                    preludeLayout.setGravity(Gravity.CENTER);
                    Typeface type1 = Typeface.createFromAsset(getContext().getAssets(),"fonts/corner.ttf");
                    fugueLayout.setTypeface(type1);
                    fugueLayout.setAlpha(0.7f);
                    preludeLayout.setTypeface(type1);
                    preludeLayout.setAlpha(0.7f);
                    //OLD IMAGE CODE
//                    preludeLayout.setImageResource(R.drawable.p_trans);
//                    fugueLayout.setImageResource(R.drawable.f_trans);
//                    preludeLayout.setPadding(6,80,6,80);
//                    fugueLayout.setPadding(6,80,6,80);

                    l1.addView(imageView);
                    l2.addView(textView);
                    PFLayout.setOrientation(LinearLayout.HORIZONTAL);
                    PFLayout.setVisibility(View.GONE);


                    PFLayout.addView(preludeLayout);
                    PFLayout.addView(fugueLayout);

                    //CHNAGE TEXT SIZE BASED ON DEVICE HEIGHT
                    Display display = getActivity().getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    final int height = size.y;
                    PFLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("ZZZZZZZZ",PFLayout.getHeight()+"_"+PFLayout.getWidth());
                            PFLayout.setPadding(0
                                    ,(int)(((View)PFLayout.getParent()).getHeight()*0.08)
                                    , 0
                                    ,0);
                            preludeLayout.setTextSize(height/60f);
                            fugueLayout.setTextSize(height/60f);
                        }
                    });

                    final LinearLayout masterLayout = new LinearLayout(getContext());


                    LinearLayout.LayoutParams PFparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);

                    preludeLayout.setLayoutParams(PFparams);
                    fugueLayout.setLayoutParams(PFparams);

//                    preludeLayout.setScaleType(ImageView.ScaleType.FIT_XY);
//                    fugueLayout.setScaleType(ImageView.ScaleType.FIT_XY);

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                    linearLayout.setLayoutParams(lp);
                    LinearLayout.LayoutParams PFlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,0.8f);
                    masterLayout.setGravity(Gravity.CENTER);
                    PFLayout.setLayoutParams(PFlp);
                    masterLayout.addView(linearLayout);
                    masterLayout.setLayoutParams(lp);
                    imageView.setLayoutParams(lp);
                    textView.setLayoutParams(lp);
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.8f);
                    l1.setLayoutParams(lp1);
                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.2f);
                    l2.setLayoutParams(lp2);
                    linearLayout.setGravity(Gravity.CENTER);
                    linearLayout.addView(l1);
                    linearLayout.addView(PFLayout);
                    linearLayout.addView(l2);
                    final int countFinal = count;

                    linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            refreshLayout(getActivity());
                            Log.v("VIZZZ",bookNumber+"|"+countFinal);




                            if(PFLayout.getVisibility() != View.VISIBLE || !(_bookNumber == bookNumber && _keyNumber == countFinal)) {
                                Log.v("VIZZZ",PFLayout.getVisibility()+"");
                                l1.setVisibility(View.GONE);

                                PFLayout.setVisibility(View.VISIBLE);

                                ValueAnimator widthAnimator = ValueAnimator.ofInt(0, ((View) PFLayout.getParent()).getMeasuredWidth());
                                widthAnimator.setDuration(200);
                                widthAnimator.setInterpolator(new DecelerateInterpolator());
                                widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        PFLayout.getLayoutParams().width = (int) animation.getAnimatedValue();
                                        PFLayout.requestLayout();
                                    }
                                });
                                widthAnimator.start();

                            }
                            if(_bookNumber == bookNumber && _keyNumber == countFinal){
                                refreshLayoutVariables();
                            }else {
                                _bookNumber = bookNumber;
                                _keyNumber = countFinal;
                            }

                        }
                    });
                    preludeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            refreshLayout(getActivity());
                            refreshLayoutVariables();

                            int bookNumber = Integer.parseInt(l1.getTag().toString().split("_")[1]);
                            int songNumber = Integer.parseInt(l1.getTag().toString().split("_")[2]);
                            Log.v("hey", "yes" + bookNumber + "_" + songNumber);
                            Intent intent = new Intent(getContext(), SongActivity.class);
                            intent.putExtra("BOOK_NUMBER", bookNumber);
                            intent.putExtra("SONG_NUMBER", songNumber);
                            intent.putExtra("PRELUDE", true);

                            startActivity(intent);

                        }
                    });
                    fugueLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            refreshLayout(getActivity());
                            refreshLayoutVariables();
                            int bookNumber = Integer.parseInt(l1.getTag().toString().split("_")[1]);
                            int songNumber = Integer.parseInt(l1.getTag().toString().split("_")[2]);
                            Log.v("hey", "yes" + bookNumber + "_" + songNumber);
                            Intent intent = new Intent(getContext(), SongActivity.class);
                            intent.putExtra("BOOK_NUMBER", bookNumber);
                            intent.putExtra("SONG_NUMBER", songNumber);
                            intent.putExtra("PRELUDE", false);

                            startActivity(intent);

                        }
                    });
                    int layoutInt = getResources().getIdentifier("layout"+count, "id", getContext().getPackageName());
                    LinearLayout dumpingLayout = layout.findViewById(layoutInt);
                    Log.v("ccc",count+"");
                    dumpingLayout.addView(masterLayout);
                    count++;
                }
            }
            return layout;
        }
        public void refreshLayoutVariables(){
            _bookNumber = -1;
            _keyNumber = -1;
        }


        public void refreshLayout(final Activity activity){
            if(_bookNumber != -1){
            View drawer = activity.findViewById(R.id.drawer_layout);
            final View PFLayout = drawer.findViewWithTag("PFLayout_"+_bookNumber+"_"+_keyNumber);
            final View keyLayout = drawer.findViewWithTag("keyLayout_"+_bookNumber+"_"+_keyNumber);



                ValueAnimator widthAnimator = ValueAnimator.ofInt(((View)PFLayout.getParent()).getMeasuredWidth(),0);
                final ValueAnimator widthAnimator2 = ValueAnimator.ofInt(0,10);
                widthAnimator.setDuration(100);
                widthAnimator.setInterpolator(new DecelerateInterpolator());
                widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        PFLayout.getLayoutParams().width = (int) animation.getAnimatedValue();
                        PFLayout.requestLayout();
                    }
                });
                widthAnimator.addListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        widthAnimator2.setDuration(400);
                        widthAnimator2.setInterpolator(new DecelerateInterpolator());
                        widthAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                keyLayout.setAlpha(((int)animation.getAnimatedValue()/10f));
                                keyLayout.requestLayout();
                            }
                        });
                        keyLayout.setVisibility(View.VISIBLE);
                        PFLayout.setVisibility(View.GONE);
                        widthAnimator2.start();

                    }
                });
                widthAnimator.start();
            }
//            for(int b = 1;b<3;b++) {
//                for (int i = 1; i < 25; i++) {
//                    View drawer = activity.findViewById(R.id.drawer_layout);
//                    final View PFLayout = drawer.findViewWithTag("PFLayout_"+b+"_"+i);
//                    final View keyLayout = drawer.findViewWithTag("keyLayout_"+b+"_"+i);
//                    if(PFLayout.getVisibility() == View.VISIBLE){
//
//                        ValueAnimator widthAnimator = ValueAnimator.ofInt(((View)PFLayout.getParent()).getMeasuredWidth(),0);
//                        final ValueAnimator widthAnimator2 = ValueAnimator.ofInt(0,10);
//                        widthAnimator.setDuration(100);
//                        widthAnimator.setInterpolator(new DecelerateInterpolator());
//                        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                            @Override
//                            public void onAnimationUpdate(ValueAnimator animation) {
//                                PFLayout.getLayoutParams().width = (int) animation.getAnimatedValue();
//                                PFLayout.requestLayout();
//                            }
//                        });
//                        widthAnimator.addListener(new AnimatorListenerAdapter()
//                        {
//                            @Override
//                            public void onAnimationEnd(Animator animation)
//                            {
//                                widthAnimator2.setDuration(400);
//                                widthAnimator2.setInterpolator(new DecelerateInterpolator());
//                                widthAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                                    @Override
//                                    public void onAnimationUpdate(ValueAnimator animation) {
//                                        keyLayout.setAlpha(((int)animation.getAnimatedValue()/10f));
//                                        keyLayout.requestLayout();
//                                    }
//                                });
//                                keyLayout.setVisibility(View.VISIBLE);
//                                PFLayout.setVisibility(View.GONE);
//                                widthAnimator2.start();
//
//                            }
//                        });
//                        widthAnimator.start();
//                    }
//
//                }
//            }
        }
    }
    //DISABLE AUTO ADJUST FONT IN ANDROID
    public void adjustFontScale(Configuration configuration) {
        Log.v("zappy", configuration.fontScale+"");
            configuration.fontScale = (float) 1.0;
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            getApplicationContext().getResources().updateConfiguration(configuration, metrics);
    }

    //GET IMAGE ID
    public static int getKeySignatureDrawable(Context context, int x){

        return context.getResources().getIdentifier("ic_"+x, "drawable", context.getPackageName());

    }

    //GET SONG NAME BASED ON BOOK/COUNT
    public static String getSongName(int song, int book){
        switch(song){
//FF03＃
            case 1:return "C major";
            case 2:return "C minor";
            case 3:return "C# major";
            case 4:return "C# minor";
            case 5:return "D major";
            case 6:return "D minor";
            case 7:return "E♭ major";
            case 8: if(book == 1) return "E♭/D# minor"; else return "D# minor";
            case 9:return "E major";
            case 10:return "E minor";
            case 11:return "F major";
            case 12:return "F minor";
            case 13:return "F# major";
            case 14:return "F# minor";
            case 15:return "G major";
            case 16:return "G minor";
            case 17:return "A♭ major";
            case 18:return "G# minor";
            case 19:return "A major";
            case 20:return "A minor";
            case 21:return "B♭ major";
            case 22:return "B♭ minor";
            case 23:return "B major";
            case 24:return "B minor";

        }
        return ""+song;
    }

    //RESIZE IMAGE TO PREVENT LOADING ENTIRE IMAGE TO MEMORY
    public Drawable resizeImage(int imageResource) {// R.drawable.large_image
        // Get device dimensions
        Display display = getWindowManager().getDefaultDisplay();
        double deviceWidth = display.getWidth();

        BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(imageResource);
        double imageHeight = bd.getBitmap().getHeight();
        double imageWidth = bd.getBitmap().getWidth();

        double ratio = deviceWidth / imageWidth;
        int newImageHeight = (int) (imageHeight * ratio);

        Bitmap bMap = BitmapFactory.decodeResource(getResources(), imageResource);
        Drawable drawable = new BitmapDrawable(this.getResources(), getResizedBitmap(bMap, newImageHeight, (int) deviceWidth));

        return drawable;
    }

    /************************ Resize Bitmap *********************************/
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

}
