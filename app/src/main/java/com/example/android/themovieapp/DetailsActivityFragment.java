package com.example.android.themovieapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {


    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        TextView titleText = (TextView) rootView.findViewById(R.id.movieTitle);
        ImageView moviePoster = (ImageView) rootView.findViewById(R.id.detailsMoviePoster);
        TextView releaseDate = (TextView) rootView.findViewById(R.id.releaseyear);
        TextView Rate = (TextView) rootView.findViewById(R.id.movieRate);
        TextView overView = (TextView) rootView.findViewById(R.id.movieDescription);


        MoviesData movie = getActivity().getIntent().getExtras().getParcelable("parcelable_object");
        titleText.setText(movie.getTitle());
        Picasso.with(getContext()).load(movie.getPoster_path()).into(moviePoster);
        releaseDate.setText(movie.getRelease());
        Rate.setText(movie.getRate());
        overView.setText(movie.getOverview());

        return rootView;
    }
}
