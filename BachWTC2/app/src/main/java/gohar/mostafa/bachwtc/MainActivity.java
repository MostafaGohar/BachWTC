package gohar.mostafa.bachwtc;

import android.app.Application;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b1 = findViewById(R.id.button1);
        Button b2 = findViewById(R.id.button2);

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), BookActivity.class);
                myIntent.putExtra("BOOK_NUMBER", 1);
                startActivity(myIntent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), BookActivity.class);
                myIntent.putExtra("BOOK_NUMBER", 2);
                startActivity(myIntent);
            }
        });
    }
}
