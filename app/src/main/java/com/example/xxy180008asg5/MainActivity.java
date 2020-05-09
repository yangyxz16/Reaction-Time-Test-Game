package com.example.xxy180008asg5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Class for the main activity
 * which allows user to go play game and view high scores
 *
 * Written by Xizhen Yang for CS6326.001, Assignment 5, starting March 28, 2020
 * NetID: XXY180008
 */
public class MainActivity extends AppCompatActivity {
    Button startBT;
    Button highScoreBT;

    /**
     * Method that is called when the activity starts
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // Title that displays on the action bar
        setTitle( "Reaction Time Test Game");

        startBT = findViewById( R.id.startBT );
        highScoreBT = findViewById( R.id.highScoreBT );

        startBT.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Direct to game activity
                Intent intent = new Intent( getApplicationContext(), GameActivity.class );
                startActivity( intent );
            }
        } );

        highScoreBT.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Direct to score activity
                Intent intent = new Intent( getApplicationContext(), ScoreActivity.class );
                startActivity( intent );
            }
        } );
    }
}
