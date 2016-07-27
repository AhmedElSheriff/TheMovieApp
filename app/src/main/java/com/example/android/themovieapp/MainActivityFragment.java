package com.example.android.themovieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

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
public class MainActivityFragment extends Fragment {

    GridView gridView;
    String sortByStr;
    ImageView imageView;



    ArrayList<MoviesData> moviesDatas;


    @Override
    public void onResume() {
        super.onResume();

    }

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();


    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    public void updateMovies()
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortByStr = prefs.getString(getString(R.string.pref_sortby_key),getString(R.string.pref_sortby_default));
        FetchMovies movies = new FetchMovies();
        movies.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refreshmovies,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_refresh)
        {
           updateMovies();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        updateMovies();


        View rootView = inflater.inflate(R.layout.fragment_main,container,false);



        gridView = (GridView) rootView.findViewById(R.id.moviesListGridView);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MoviesData movie = moviesDatas.get(position);

                Intent i = new Intent(getActivity(),DetailsActivity.class);
                i.putExtra("parcelable_object",movie);
                startActivity(i);
            }
        });



        return rootView;

    }

    public class FetchMovies extends AsyncTask<String, Void, ArrayList<MoviesData>>
    {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr;

        private ArrayList<MoviesData> getMoviesDataFromJson(String moviesJsonStr) throws JSONException
        {


            JSONObject forecastJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = forecastJson.getJSONArray("results");

            moviesDatas = new ArrayList<>();

            for(int i = 0; i <moviesArray.length(); i++)
            {


               JSONObject movieObject = moviesArray.getJSONObject(i);


                MoviesData movie = new MoviesData();

                movie.setPoster_path(movieObject.getString("poster_path"));
                movie.setTitle(movieObject.getString("title"));
                movie.setOverview(movieObject.getString("overview"));
                movie.setRate(movieObject.getString("vote_average"));
                movie.setRelease(movieObject.getString("release_date"));
                moviesDatas.add(movie);

            }



            return moviesDatas;


        }



        @Override
        protected ArrayList<MoviesData> doInBackground(String... params) {

            try {

                URL url = null;

                if(sortByStr.equals("popular"))
                {
                    url = new URL("http://api.themoviedb.org/3/movie/popular?api_key=9a005dd380ec772cf6045b8c370f8ef7");
                }

                else if(sortByStr.equals("top_rated"))
                {
                    url = new URL("http://api.themoviedb.org/3/movie/top_rated?api_key=9a005dd380ec772cf6045b8c370f8ef7");
                }

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

                moviesJsonStr = buffer.toString();


            } catch (IOException e) {
                Log.e("Found ", "Error ", e);

                return null;
            } finally {

                Log.e("TMDB AP Throws: ", moviesJsonStr);
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
                return getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();

            }






            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MoviesData> result) {
            if (result != null) {

                imageView = (ImageView) getActivity().findViewById(R.id.oneMovieBlockImageView);
                GridViewAdapter adapter = new GridViewAdapter(getActivity(),moviesDatas);
                gridView.setAdapter(adapter);



            }
        }
    }



}
