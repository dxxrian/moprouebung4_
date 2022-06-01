package com.example.moprouebung4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpatric.mp3agic.ID3v2;

import java.util.ArrayList;

public class musikadapter extends BaseAdapter {
    ArrayList<ID3v2> infos;

    Context context;

    public musikadapter (Context c, ArrayList<ID3v2> list) {
        context = c;
        infos = list;
    }
    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int i) {
        return infos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.musikadapter,viewGroup,false);
        }
        TextView textViewTitle = (TextView)view.findViewById(R.id.textViewTitle);
        TextView textViewArtist = (TextView)view.findViewById(R.id.textViewArtist);
        TextView textViewGenre = (TextView)view.findViewById(R.id.textViewGenre);
        TextView textViewYear = (TextView)view.findViewById(R.id.textViewYear);
        TextView textViewData = (TextView)view.findViewById(R.id.textViewData);

        textViewTitle.setText(infos.get(i).getTitle());
        textViewArtist.setText(infos.get(i).getArtist());
        textViewData.setText(" - " + infos.get(i).getAlbum());
        textViewGenre.setText(infos.get(i).getGenre()+" ("+infos.get(i).getGenreDescription()+")");
        textViewYear.setText(infos.get(i).getYear());

        byte imageData[] = infos.get(i).getAlbumImage();
        if(imageData!=null){
            Bitmap bmp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            ImageView imageViewCover = view.findViewById(R.id.imageViewCover);
            imageViewCover.setImageBitmap(bmp);
        }
        return view;
    }
}
