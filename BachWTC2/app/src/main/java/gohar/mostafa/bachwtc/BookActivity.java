package gohar.mostafa.bachwtc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {

    int bookNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        bookNumber = (int) b.get("BOOK_NUMBER");


        ArrayList<Song>songs = new ArrayList<Song>();
        for(int i = 0;i<24;i++)
        songs.add(new Song(bookNumber,i+1,i+1+""));

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new CustomAdapter(getApplicationContext(),songs));

    }
}
