package gohar.mostafa.bachwtc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        int bookNumber = (int) b.get("BOOK_NUMBER");
        int songNumber = (int) b.get("SONG_NUMBER");
        boolean isPrelude = (boolean) b.get("PRELUDE");
        String Name = (String) b.get("NAME");


        TextView textView = findViewById(R.id.textView);
        textView.setText(bookNumber+"_"+songNumber+"_"+isPrelude+"_"+Name);

    }
}
