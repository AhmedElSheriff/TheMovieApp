package com.example.android.themovieapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class TrailersActivityFragment extends Fragment {
    
    ArrayList<String> trailersArrayList;
    ArrayAdapter adapter;
    ArrayList<String> trailersKeys;
    ArrayList<String> trailersNames;
    ListView listView;


    public TrailersActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_trailers, container, false);

        trailersArrayList = new ArrayList<>();
        Intent i = getActivity().getIntent();
        trailersArrayList = i.getStringArrayListExtra("trailersList");
        trailersKeys = i.getStringArrayListExtra("trailersKeys");
        trailersNames = i.getStringArrayListExtra("trailersNames");

        listView = (ListView) rootView.findViewById(R.id.trailersList);
        adapter = new ArrayAdapter<>(getActivity(),R.layout.trailersonerow,R.id.trailersonerowtext,trailersNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + trailersKeys.get(position)));
                        startActivity(intent);
            }
        });


        return rootView;

    }
}
