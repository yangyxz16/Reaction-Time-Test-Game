package com.example.xxy180008asg5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.xxy180008asg5.GameScores;

import java.util.ArrayList;

/**
 * Class used to display high score list with adapter
 *
 * Written by John Cole
 * Modified by Xizhen Yang for CS6326.001, Assignment 5, starting March 28, 2020
 * NetID: XXY180008
 */
public class StableArrayAdapter extends ArrayAdapter<GameScores> {

    ArrayList<GameScores> scoreList;

    /**
     * Default constructor for the StableArrayAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public StableArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<GameScores> objects) {
        super( context, resource, objects );

        scoreList = objects;
    }

    /**
     * Method to fill data in for each rol
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Context cx = this.getContext();

        // Create thr inflater to match the layout with the View
        LayoutInflater inflater = (LayoutInflater) cx.getSystemService(cx.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_view_layout, parent, false);

        // Fill out each time column
        TextView tvName = (TextView) rowView.findViewById(R.id.time);
        tvName.setText(scoreList.get( position ).getTimeInString());

        // Fill out each missed column
        TextView tvScore = (TextView) rowView.findViewById(R.id.missed);
        tvScore.setText( Integer.toString( scoreList.get( position ).getMissed() ) );

        // Fill out each date column
        TextView tvDate = (TextView) rowView.findViewById(R.id.date);
        tvDate.setText( scoreList.get( position ).getDate() );

        return rowView;
    }
}
