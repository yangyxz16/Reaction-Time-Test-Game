package com.example.xxy180008asg5;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Class for the game activity which is called from the main activity,
 * which allows user to play the game and displays game data
 *
 * Written by Xizhen Yang for CS6326.001, Assignment 5, starting March 28, 2020
 * NetID: XXY180008
 */
public class GameActivity extends AppCompatActivity {
    // UI
    TextView timeTV;
    TextView clickTV;
    TextView missedTV;
    TextView gameRuleTV;
    GamingFieldView gamingFieldView;

    // Rules: circle/square, shape color
    int ruleShapeType;
    int ruleShapeColor;

    // x and y coordinates where the screen is touched
    float x;
    float y;

    // Variable for user's reaction time
    int reactionTime;
    // Number of times user clicks the correct shape
    int click;
    // Number of times user misses the correct shape
    int missed;

    // Handler
    Handler handler;
    Runnable r;
    // Variable for lifetime of a shape in milliseconds
    int frameRate;

    /**
     * Method that is called when the activity starts
     * @param savedInstanceState
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_game );

        // Initialize UI components
        timeTV = (TextView)findViewById( R.id.timeTV );
        clickTV = (TextView) findViewById( R.id.clickTV );
        missedTV = (TextView) findViewById( R.id.missedTV );
        gameRuleTV = (TextView) findViewById( R.id.gameRuleTV );
        gamingFieldView = (GamingFieldView) findViewById( R.id.gameView );

        // Generate shape type and color randomly
        int[] shapeRules = gamingFieldView.getShapeRules();
        ruleShapeType = shapeRules[0];
        ruleShapeColor = shapeRules[1];

        // Display the game rule to the user
        gameRuleTV.setText("Touch 10 " + shapeColor( ruleShapeColor ) + " " + shapeType( ruleShapeType ) + "s");

        // Default reaction time, click, and missed are 0
        reactionTime = 0;
        click = 0;
        missed = 0;

        gamingFieldView.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // When user press down and lift up the finger
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Store the touched x/y coordinates
                    x = event.getX();
                    y = event.getY();

                    // Touch the right shape
                    if (gamingFieldView.correctTouch(x, y)) {
                        // Update the reaction time and reaction time text view
                        reactionTime += gamingFieldView.getCorrectTouchReactionTime();
                        timeTV.setText( timeToString( reactionTime ) );

                        // Update the click and click text view
                        click += 1;
                        clickTV.setText( Integer.toString( click ) );
                        // When the user clicks 10 correct shape
                        if (click == 10) {endGame();}
                    }
                }
                return true;
            }
        } );

        this.frameRate = 500; // Initialize the frameRate: 0.5 second
        this.handler = new Handler(); // Initialize the handler
        r = new Runnable() {
            @Override
            public void run() { updatePenalty(); }};

        updatePenalty(); // update reaction time by adding penalty time
    }

    /**
     * Function to refresh every 0.5 seconds
     * that can get penalty time from Gaming Field View,
     * and update reaction time and text view;
     */
    private void updatePenalty() {
        int penaltyTime = gamingFieldView.getPenaltyTime();

        if (penaltyTime != 0) {
            // Update the reaction time by adding penalty time
            reactionTime += penaltyTime;
            timeTV.setText( timeToString( reactionTime ) );

            // Update the missed and missed text view
            missed += 1;
            missedTV.setText( Integer.toString( missed ) );
        }
        // The Canvas will be refreshed every "frameRate" second
        handler.postDelayed(r, frameRate);
    }

    /**
     * Function to end the game by directing to the AddScroeActivity
     */
    private void endGame() {
        // Create a Bundle object
        Bundle extras = new Bundle();
        extras.putInt( "time", reactionTime ); // Put reaction time
        extras.putInt( "missed", missed ); // Put missed count
        // Create and initialize an intent
        Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
        // Attach the bundle to the Intent object
        intent.putExtras( extras );
        // Finally start the activity
        startActivity(intent);
    }




    /**
     * Function to convert time from integer to string
     * @param time seconds integer
     * @return time string
     */
    private String timeToString(int time) {
        int minutes = (time % 3600) / 60;
        int seconds = time % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);

        return timeString;
    }

    /**
     * Function to generate shape type in string
     * @param ruleShapeType
     * @return shape type in string
     */
    private String shapeType(int ruleShapeType) {
        if (ruleShapeType == 0) return "circle";
        else return "square";
    }

    /**
     * Function to generate shape color in string
     * @param colorIndex between 1 - 7
     * @return shape color in string
     */
    private String shapeColor(int colorIndex) {
        switch (colorIndex) {
            case 1: return "red";
            case 2: return "orange";
            case 3: return "yellow";
            case 4: return "green";
            case 5: return "blue";
            case 6: return "purple";
            default: return "white";
        }
    }

}
