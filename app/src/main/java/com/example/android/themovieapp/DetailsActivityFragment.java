package com.example.android.themovieapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment{


    private String id;

    private ArrayList<String> reviewsDatas;

    private ArrayList<String> trailersDatas;
    private ArrayList<String> trailersKeys;
    private ArrayList<String> trailersNames;

    public DetailsActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateReviews();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        TextView titleText;
        titleText = (TextView) rootView.findViewById(R.id.movieTitle);
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
        id = movie.getId();


        Button reviewsButton = (Button) rootView.findViewById(R.id.movieReviews);
        reviewsDatas = new ArrayList<>();



        updateReviews();
        reviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ReviewsActivity.class);
                i.putStringArrayListExtra("reviewsList", reviewsDatas);
                startActivity(i);
            }
        });

        Button trailersButton = (Button) rootView.findViewById(R.id.movieTrailers);
        trailersDatas = new ArrayList<>();

        updateTrailers();
        trailersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TrailersActivity.class);
                i.putStringArrayListExtra("trailersList", trailersDatas);
                i.putStringArrayListExtra("trailersKeys", trailersKeys);
                i.putStringArrayListExtra("trailersNames",trailersNames);
                startActivity(i);
            }
        });
        return rootView;
    }



    private void updateReviews()
    {
        FetchReviews fetchReviews = new FetchReviews();
        fetchReviews.execute();
    }

    private void updateTrailers()
    {
        FetchTrailers fetchTrailers = new FetchTrailers();
        fetchTrailers.execute();
    }





    public class FetchReviews extends AsyncTask<String,Void,ArrayList<String>> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String reviewsJsonStr;


        private ArrayList<String> getReviewsFromJson(String reviewsJsonStr) throws JSONException {


            JSONObject reviewsJson = new JSONObject(reviewsJsonStr);
            JSONArray reviewsArray = reviewsJson.getJSONArray("results");

            String author, content;
            ArrayList<String> resultStrs = new ArrayList<>();
            for (int i = 0; i < reviewsArray.length(); i++) {
                JSONObject reviewsObject = reviewsArray.getJSONObject(i);
                author = reviewsObject.getString("author");
                content = reviewsObject.getString("content");
                resultStrs.add("Author: " + author + "\nContent: \n" + content);
            }

            return resultStrs;
        }


        @Override
        protected ArrayList<String> doInBackground(String... params) {

            try {

                URL url;
                url = new URL("http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=9a005dd380ec772cf6045b8c370f8ef7");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }

                reviewsJsonStr = buffer.toString();
                Log.v("Reviews JSON ", reviewsJsonStr);


            } catch (IOException e) {
                Log.e("Found ", "Error ", e);

                return null;
            } finally {

                // Log.e("TMDB AP Throws: ", trailerJsonStr);
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Found ", "Error closing stream", e);
                    }
                }
            }

            try {
                return getReviewsFromJson(reviewsJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<String> result) {

            reviewsDatas.clear();
            reviewsDatas.addAll(result);

        }
    }

    public class FetchTrailers extends AsyncTask<String,Void,ArrayList<String>>
    {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String trailerJsonStr;
        String trailerURL;
        String trailerKey;
        String trailerName;

        private ArrayList<String> getTrailersFromJson(String moviesJsonStr) throws
                JSONException
        {


            JSONObject trailersJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = trailersJson.getJSONArray("results");

            ArrayList<String> trailersContent = new ArrayList<>();
            trailersKeys = new ArrayList<>();
            trailersNames = new ArrayList<>();

            for(int i = 0; i <moviesArray.length(); i++)
            {


                JSONObject movieObject = moviesArray.getJSONObject(i);
                trailerURL = "https://www.youtube.com/watch?v=" + movieObject.getString("key");
                Log.v("Trailer URL ", trailerURL);
                trailerKey = movieObject.getString("key");
                trailerName = movieObject.getString("name");
                trailersContent.add(trailerURL);
                trailersKeys.add(trailerKey);
                trailersNames.add(trailerName);

            }

            return trailersContent;
        }





        @Override
        protected ArrayList<String> doInBackground(String... params) {

            try {

                URL url;
                url = new URL("http://api.themoviedb.org/3/movie/"+id+"/videos?api_key=9a005dd380ec772cf6045b8c370f8ef7");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }

                trailerJsonStr = buffer.toString();


            } catch (IOException e) {
                Log.e("Found ", "Error ", e);

                return null;
            } finally {

                // Log.e("TMDB AP Throws: ", trailerJsonStr);
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Found ", "Error closing stream", e);
                    }
                }
            }

            try {
                return getTrailersFromJson(trailerJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<String> trailersResult) {

            trailersDatas.clear();
            trailersDatas.addAll(trailersResult);
        }
    }
    }

