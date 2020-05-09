package com.example.xxy180008asg5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

/**
 * Class for the score activity which displays the high scores,
 * which also got directed from game activity
 * to determines whether the game score is the highest,
 * and directed to add score activity to display game results
 *
 * Written by Xizhen Yang for CS6326.001, Assignment 5, starting March 28, 2020
 * NetID: XXY180008
 */
public class ScoreActivity extends AppCompatActivity {
    // ArrayList to score the list of GameScores objects
    ArrayList<GameScores> scoreList;

    //File fileDir = this.getFilesDir();
    String fileName = "Scores";

    ListView lv;

    private int time;
    private int missed;

    // Create a variable to store current date
    final String curDate = setCurrentDate();

    /**
     * Method that is called when the activity starts
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_score );

        // Title that displays on the action bar
        setTitle( "Highest scores" );

        lv = findViewById( R.id.listView );

        // Initialize the list which reads data from a internal file
        scoreList = FileIO.fromFile(this.getFilesDir(), fileName);

        // Get the intent in the target activity from GameActivity
        Intent intent = getIntent();
        // Get the attached bundle from the intent
        Bundle extras = intent.getExtras();
        if (extras != null) { // which means it come from the game activity
            //Extracting the stored data from the bundle
            time = extras.getInt("time");
            missed = extras.getInt("missed");
            // Go to AddScoreActivity to display game result
            gameEnds( isHighest() );;
        }

        reorganizeList( scoreList );

        // Set up the adapter for the listView
        StableArrayAdapter adapter = new StableArrayAdapter( this, R.layout.adapter_view_layout, scoreList );
        lv.setAdapter( adapter );
    }

    /**
     * Function to check if a score is the highest
     * If so, add to the list
     */
    private boolean isHighest() {
        // Highest score
        if (scoreList.size() == 0 || time <= scoreList.get( scoreList.size()-1 ).getTime()) {
            // Add to the list
            scoreList.add( new GameScores( time, missed, curDate ) );

            // Update the ListView
            StableArrayAdapter adapter = new StableArrayAdapter( this, R.layout.adapter_view_layout, scoreList );
            lv.setAdapter( adapter );

            // Save to file
            FileIO.toFile(this.getFilesDir(), fileName, scoreList);

            return  true;
        }
        return false;
    }

    /**
     * Function to go to AddScoreActivity
     * @param highest
     */
    private void gameEnds(boolean highest) {
        // Create a Bundle object
        Bundle extras = new Bundle();
        extras.putInt( "time", time ); // Put reaction time
        extras.putInt( "missed", missed ); // Put missed count
        extras.putBoolean( "highest", highest );
        // Create and initialize an intent
        Intent intent = new Intent(getApplicationContext(), AddScroeActivity.class);
        // Attach the bundle to the Intent object
        intent.putExtras( extras );
        // Finally start the activity
        startActivity(intent);
    }




    /**
     * Method that return from the called activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        // When intent returns matches the called activity
        if (requestCode == 1) {
            Bundle bundle = data.getExtras();

            GameScores gs = (GameScores) bundle.getSerializable( "Score" );
            scoreList.add( gs );

            // When a new score is recorded, check to see if it needs to update
            reorganizeList( scoreList );

            // Update the ListView
            StableArrayAdapter adapter = new StableArrayAdapter( this, R.layout.adapter_view_layout, scoreList );
            lv.setAdapter( adapter );

            // Save to file
            FileIO.toFile(getFilesDir(), fileName, scoreList);
        }
    }

    /**
     * Method to create action bar menu on the top
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.add_high_score, menu );
        return true;
    }

    /**
     * Method to do operations for the item from action bar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_button) {
            Toast.makeText( getApplicationContext(), "Unable to add score", Toast.LENGTH_SHORT ).show();
            // Create an intent to start another activity
            //Intent intent = new Intent( this,  AddScroeActivity.class);
            //startActivityForResult( intent, 1, null);
            //startActivity( intent );
        }
        return true;
    }

    /**
     * Method to only select the 12 highest scores of the list
     * @param scoreList
     */
    public static void reorganizeList(ArrayList<GameScores> scoreList) {
        // Sort the list from large to small
        Collections.sort( scoreList);

        // Only keep the 12 highest scores
        for (int i = 12; i < scoreList.size(); i++) {
            scoreList.remove( 12 );
        }
    }


    /**
     * Method to set current date
     * @return current date in String
     */
    private String setCurrentDate() {
        Date date = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = df.format(date);

        return formattedDate;
    }

}
