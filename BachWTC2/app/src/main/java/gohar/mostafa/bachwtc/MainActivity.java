package gohar.mostafa.bachwtc;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);


        ViewPager mViewPager;
        SlidingTabLayout tabs;

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(mViewPager);
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
            return "Book "+position;
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

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
            View layout = inflater.inflate(R.layout.songs_page,container,false);

            GridLayout mGridLayout = layout.findViewById(R.id.gridLayout);
            mGridLayout.setRowCount(6);
            mGridLayout.setColumnCount(4);

            Bundle bundle = getArguments();
            int bookNumber = bundle.getInt("position");

            for(int i = 0;i<6;i++){
                for(int j = 0;j<4;j++){
                    //Get actual count as if it were one loop
                    int x = (j+1) + (4*i);

                    final LinearLayout linearLayout = new LinearLayout(getContext());
                    LinearLayout l1 = new LinearLayout((getContext()));
                    LinearLayout l2 = new LinearLayout((getContext()));
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    ImageView imageView = new ImageView(getContext());
                    TextView textView = new TextView(getContext());

                    imageView.setImageResource(getKeySignatureDrawable(getContext(),x,bookNumber));

                    textView.setText(getSongName(x, bookNumber));
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.BLACK);

                    l1.addView(imageView);
                    l2.addView(textView);

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    linearLayout.setLayoutParams(lp);
                    imageView.setLayoutParams(lp);
                    textView.setLayoutParams(lp);
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,0.8f);


                    l1.setLayoutParams(lp1);
                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,0.2f);

                    l2.setLayoutParams(lp2);


                    linearLayout.addView(l1);
                    linearLayout.addView(l2);
                    GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f),      GridLayout.spec(GridLayout.UNDEFINED, 1f));

                    linearLayout.setLayoutParams(param);
                    linearLayout.setPadding(20,20,20,20);

                    TypedValue typedValue = new TypedValue();
                    getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
                    linearLayout.setBackgroundResource(typedValue.resourceId);
                    linearLayout.setTag(bookNumber+"_"+x);
                    linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.v("hey","yes"+linearLayout.getTag());
                        }
                    });
                    mGridLayout.addView(linearLayout);
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
    }

    public static int getKeySignatureDrawable(Context context, int x, int book){

        int id;
        if(book == 0 && x == 8)
            id = context.getResources().getIdentifier("joined", "drawable", context.getPackageName());
        else
            id = context.getResources().getIdentifier("ic_"+x, "drawable", context.getPackageName());
        return id;
    }

    public static String getSongName(int x, int book){
        switch(x){

            case 1:return "C major";
            case 2:return "C minor";
            case 3:return "C# major";
            case 4:return "C# minor";
            case 5:return "D major";
            case 6:return "D minor";
            case 7:return "E♭ major";
            case 8: if(book == 0) return "E♭ minor \n/ D# minor"; else return "D# minor";
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
        return ""+x;
    }

}
