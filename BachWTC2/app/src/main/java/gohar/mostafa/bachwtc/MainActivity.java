package gohar.mostafa.bachwtc;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.transition.Visibility;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    public static MainActivity mainActivity;


    public static int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count = 1;


        mainActivity = this;
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        ViewPager mViewPager;
//        final SlidingTabLayout tabs;

        //tabs = findViewById(R.id.tabs);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            public void onPageScrollStateChanged(int state) {
//                tabs.setVisibility(View.VISIBLE);
//                hideTabs(tabs);
//            }
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                tabs.setVisibility(View.VISIBLE);
//                hideTabs(tabs);
//            }
//
//            public void onPageSelected(int position) {
//                // Check if this is the page you want.
//            }
//        });
//
//        hideTabs(tabs);

//        tabs.setDistributeEvenly(true);
//
//        // Setting the ViewPager For the SlidingTabsLayout
//        tabs.setViewPager(mViewPager);
    }

//    public void hideTabs(final View tabs){
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                }
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        tabs.setVisibility(View.GONE);
//                    }
//                });
//            }
//        };
//        thread.start(); //start the thread
//    }
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
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
            final View layout = inflater.inflate(R.layout.songs_page,container,false);
            count = 1;
            final SlidingTabLayout tabs = layout.findViewById(R.id.tabs);



//            GridLayout mGridLayout = layout.findViewById(R.id.gridLayout);
//            mGridLayout.setRowCount(6);
//            mGridLayout.setColumnCount(4);

            final LinearLayout containerLayout = layout.findViewById(R.id.container_layout);

            Bundle bundle = getArguments();
            final int bookNumber = bundle.getInt("position")+1;

            Drawable drawable;
            if(bookNumber == 1) {
                drawable = mainActivity.resizeImage(R.drawable.wallz1);
//               containerLayout.setPadding(150,70,50,70);
            }
            else {
                drawable = mainActivity.resizeImage(R.drawable.wallz2);
//                containerLayout.setPadding(80,70,150,70);

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

//
//            Drawable drawable = getResources().getDrawable(id);




            for(int i = 0;i<6;i++){
                for(int j = 0;j<4;j++) {
                    //Get actual count as if it were one loop

                    final LinearLayout linearLayout = new LinearLayout(getContext());
                    final LinearLayout l1 = new LinearLayout((getContext()));
                    final LinearLayout l2 = new LinearLayout((getContext()));
                    final LinearLayout PFLayout = new LinearLayout(getContext());

                    PFLayout.setTag("PFLayout_"+bookNumber+"_"+count);
                    l1.setTag("keyLayout_"+bookNumber+"_"+count);
                    Log.v("LMAO","PFLayout_"+bookNumber+"_"+count);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    final ImageView imageView = new ImageView(getContext());
                    AutoResizeTextView textView = new AutoResizeTextView(getContext());

                    Typeface type = Typeface.createFromAsset(getContext().getAssets(),"fonts/font_main_1.otf");
                    textView.setTypeface(type);


//                    imageView.setAlpha(0.7f);
//                    textView.setAlpha(0.7f);
//                    Typeface face= ResourcesCompat.getFont(getContext(), R.font.font_4);
//                    textView.setTypeface(face);
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setPadding((int)(imageView.getWidth()*0.09)
                                    ,(int)(imageView.getWidth()*0.09)
                                    ,(int)(imageView.getWidth()*0.09)
                                    ,(int)(imageView.getWidth()*0.09));
                        }
                    });
                    imageView.setImageResource(getKeySignatureDrawable(getContext(), count, bookNumber));

                    textView.setText(getSongName(count, bookNumber));
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.BLACK);
                    //textView.setTypeface(Typeface.DEFAULT_BOLD);

                    final TextView preludeLayout = new TextView(getContext());
                    final TextView fugueLayout = new TextView(getContext());

//                    preludeLayout.setTextColor(Color.WHITE);
//                    fugueLayout.setTextColor(Color.WHITE);
//                    preludeLayout.setBackgroundColor(Color.DKGRAY);
//                    fugueLayout.setBackgroundColor(Color.GRAY);
                    preludeLayout.setText("P");
                    preludeLayout.setTextSize(30);
                    fugueLayout.setTextSize(30);
                    preludeLayout.setTextColor(Color.BLACK);
                    fugueLayout.setTextColor(Color.BLACK);

                    fugueLayout.setText("F");
                    fugueLayout.setGravity(Gravity.CENTER);
                    preludeLayout.setGravity(Gravity.CENTER);
                    fugueLayout.setTypeface(type);
                    preludeLayout.setTypeface(type);


                    l1.addView(imageView);
                    l2.addView(textView);
                    PFLayout.setOrientation(LinearLayout.HORIZONTAL);
                    PFLayout.setVisibility(View.GONE);
                    PFLayout.addView(preludeLayout);
                    PFLayout.addView(fugueLayout);
                    PFLayout.setBackgroundResource(R.drawable.pf_back_1);
//                    PFLayout.setPadding(50,200,50,200);

//                    PFLayout.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.v("ZZZZZZZZ",PFLayout.getHeight()+"_"+PFLayout.getWidth());
//                            PFLayout.setPadding((int)(((View)PFLayout.getParent()).getHeight()*0.1)
//                                    ,(int)(((View)PFLayout.getParent()).getHeight()*0.25)
//                                    ,(int)(((View)PFLayout.getParent()).getHeight()*0.1)
//                                    ,(int)(((View)PFLayout.getParent()).getHeight()*0.2));
//                        }
//                    });

                    final LinearLayout masterLayout = new LinearLayout(getContext());

                    LinearLayout.LayoutParams PFparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                    preludeLayout.setLayoutParams(PFparams);
                    fugueLayout.setLayoutParams(PFparams);




                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                    linearLayout.setLayoutParams(lp);
                    LinearLayout.LayoutParams PFlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,0.8f);
                    masterLayout.setGravity(Gravity.CENTER);
                    PFLayout.setLayoutParams(PFlp);
//                    PFLayout.setPadding(100, 50, 50, 100);
                    //masterLayout.setLayoutParams(lp);
                    masterLayout.addView(linearLayout);
                    //masterLayout.addView(PFLayout);
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
                    //TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    //  GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f),      GridLayout.spec(GridLayout.UNDEFINED, 1f));

                    //masterLayout.setLayoutParams(params);
                    //masterLayout.setPadding(20,20,20,20);

                    //TypedValue typedValue = new TypedValue();
                    //getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
                    //linearLayout.setBackgroundResource(typedValue.resourceId);
//                    linearLayout.setTag(bookNumber + "_" + count);
                    linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            refreshLayout(getActivity());
                            l1.setVisibility(View.GONE);

                            PFLayout.setVisibility(View.VISIBLE);

                            ValueAnimator widthAnimator = ValueAnimator.ofInt(0,((View)PFLayout.getParent()).getMeasuredWidth());
                            widthAnimator.setDuration(300);
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
                    });
                    preludeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            refreshLayout(getActivity());

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
//                for(int j = 0;j<4;j++){
//                    TextView textView = new TextView(getContext());
//                    textView.setText(songNames.get(j));
//                    textView.setGravity(Gravity.CENTER);
//                    textView.setTextColor(Color.BLACK);
//
//                    GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 0.1f),      GridLayout.spec(GridLayout.UNDEFINED, 1f));
//                    textView.setLayoutParams(param);
//                    mGridLayout.addView(textView);
//                }

            }
            return layout;
        }

        public void refreshLayout(Activity activity){
            for(int b = 1;b<3;b++) {
                for (int i = 1; i < 25; i++) {


                    activity.findViewById(R.id.drawer_layout).findViewWithTag("PFLayout_"+b+"_"+i).setVisibility(View.GONE);
                    activity.findViewById(R.id.drawer_layout).findViewWithTag("keyLayout_"+b+"_"+i).setVisibility(View.VISIBLE);
                }
            }
        }


        private static ArrayList<View> getViewsByTag(ViewGroup root, String tag){
            ArrayList<View> views = new ArrayList<View>();
            final int childCount = root.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = root.getChildAt(i);
                if (child instanceof ViewGroup) {
                    views.addAll(getViewsByTag((ViewGroup) child, tag));
                }

                final Object tagObj = child.getTag();
                if (tagObj != null && tagObj.equals(tag)) {
                    views.add(child);
                }

            }
            return views;
        }
    }

    public static int getKeySignatureDrawable(Context context, int x, int book){

        int id;
        if(book == 0 && x == 8)
            id = context.getResources().getIdentifier("ic_"+13, "drawable", context.getPackageName());
        else
            id = context.getResources().getIdentifier("ic_"+x, "drawable", context.getPackageName());
        return id;
    }

    public static String getSongName(int song, int book){
        switch(song){

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

    public Drawable resizeImage(int imageResource) {// R.drawable.large_image
        // Get device dimensions
        Display display = getWindowManager().getDefaultDisplay();
        double deviceWidth = display.getWidth();

        BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(
                imageResource);
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
