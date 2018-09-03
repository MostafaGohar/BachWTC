package gohar.mostafa.bachwtc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private ArrayList<Song> listData;
    public LayoutInflater layoutInflater;
    Context aContext;

    public CustomAdapter(Context aContext, ArrayList<Song> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
        this.aContext = aContext;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.songNumber = (TextView) convertView.findViewById(R.id.numberText);
            holder.preludeButton = (Button) convertView.findViewById(R.id.preludeButton);
            holder.fugueButton = (Button) convertView.findViewById(R.id.fugueButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Song song = (Song) listData.get(position);
        holder.songNumber.setText(song.getName());

        holder.preludeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(aContext, SongActivity.class);
                myIntent.putExtra("BOOK_NUMBER", song.getBookNumber());
                myIntent.putExtra("SONG_NUMBER", song.getSongNumber());
                myIntent.putExtra("PRELUDE", true);
                myIntent.putExtra("NAME", song.getName());
                aContext.startActivity(myIntent);
            }
        });
        holder.fugueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(aContext, SongActivity.class);
                myIntent.putExtra("BOOK_NUMBER", song.getBookNumber());
                myIntent.putExtra("SONG_NUMBER", song.getSongNumber());
                myIntent.putExtra("PRELUDE", false);
                myIntent.putExtra("NAME", song.getName());
                aContext.startActivity(myIntent);
            }
        });


        return convertView;
    }

    static class ViewHolder {
        TextView songNumber;
        Button preludeButton;
        Button fugueButton;
    }
}