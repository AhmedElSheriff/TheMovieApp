package com.example.android.themovieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Abshafi on 7/26/2016.
 */
public class GridViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<MoviesData> array;
    LayoutInflater inflater;
    ImageView image;




    public GridViewAdapter(Context context,ArrayList<MoviesData> array)
    {
        this.context = context;
        this.array = array;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }
    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        //ImageView image;

        if(convertView == null)
        {
//            image = (ImageView)row.findViewById(R.id.oneMovieBlockImageView);
            //image = new ImageView(context);
////            //image.setLayoutParams(new GridView.LayoutParams(185, 370));
////            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
////
////            image.setPadding(8, 8, 8, 8);

             row = inflater.inflate(R.layout.movie_block,null);


        }


            image = (ImageView)row.findViewById(R.id.oneMovieBlockImageView);
////            //image.setLayoutParams(new GridView.LayoutParams(185, 370));
////            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
////            image.setPadding(8, 8, 8, 8);
//            image = (ImageView)row.findViewById(R.id.oneMovieBlockImageView);


        //image = (ImageView)row.findViewById(R.id.oneMovieBlockImageView);

        Picasso.with(context).load(array.get(position).getPoster_path()).into(image);

        return row;
    }

}
