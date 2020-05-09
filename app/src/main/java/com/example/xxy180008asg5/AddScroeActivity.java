package com.example.xxy180008asg5;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Class for the add score activity which calls from main activity,
 * which allows user to record new score with name, score and date
 *
 * Written by Xizhen Yang for CS6326.001, Assignment 5, starting March 28, 2020
 * NetID: XXY180008
 */
public class AddScroeActivity extends AppCompatActivity {
    // UI components
    private TextView titleTV;
    private EditText timeET;
    private EditText missedET;
    private EditText dateET;
    private Button saveBT;

    private int time;
    private int missed;
    private boolean highest;

    // Create a variable to store current date
    final String curDate = setCurrentDate();

    //File fileDir = this.getFilesDir();
    String fileName = "Scores";

    /**
     * Method that is called when the activity starts
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_scroe );

        // Get the intent in the target activity from ScoreAcitivity
        Intent intent = getIntent();
        // Get the attached bundle from the intent
        Bundle extras = intent.getExtras();
        if (extras != null) {
            //Extracting the stored data from the bundle
            time = extras.getInt("time");
            missed = extras.getInt("missed");
            highest = extras.getBoolean( "highest" );
        }

        // Initialize components, All edit texts are disabled
        titleTV = (TextView)findViewById( R.id.titleTV );
        if (highest) { // Highest score
            titleTV.setText( "New High Score" );
        } else {
            titleTV.setText( "Game Ends" );
        }

        timeET = findViewById( R.id.timeET );
        timeET.setEnabled(false);
        timeET.setHint( "Time: " + getTimeInString(time) );

        missedET = findViewById( R.id.missedET );
        missedET.setEnabled(false);
        missedET.setHint( "Missed: " + missed );

        dateET = findViewById( R.id.dateET );
        dateET.setEnabled(false);
        dateET.setHint( "Date: " + curDate ); // Current date

        saveBT = findViewById( R.id.saveBT );
        saveBT.setText( "Return to Home Page" );
        saveBT.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and initialize an intent
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                // Finally start the activity
                startActivity(intent);
//                if (validInput()) {
//                    // When all the inputs are valid, create a GameScore object
//                    GameScores gs = new GameScores( name, score, date);
//                    Toast.makeText(getApplicationContext(),"New score saved",Toast.LENGTH_SHORT).show();
//
//                    // Create a intent to send data back
//                    Intent resultIntent = new Intent();
//                    // Pass the GameScore object through bundle
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("Score", gs);
//                    // Send the bundle(GameScore object) back
//                    resultIntent.putExtras( bundle );
//                    setResult( RESULT_FIRST_USER, resultIntent);
//                    finish();
//                }
            }
        } );

    }


    /**
     * Function to convert time from integer to string
     * @return time string
     */
    public String getTimeInString(int time) {
        int minutes = (time % 3600) / 60;
        int seconds = time % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);

        return timeString;
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


    /**
     * Method to check each input
     * @return true if all are valid
     */
    /*
    private boolean validInput() {
        // Check if name is empty
        if(TextUtils.isEmpty( nameET.getText() )) {
            nameET.setError( "Player name is empty" );
            nameET.requestFocus();
            return false;
        } else {
            name = nameET.getText().toString();
        }

        // Check if score is empty or less than or equal to zero
        if(TextUtils.isEmpty( scoreET.getText() )) {
            scoreET.setError( "Score is empty" );
            scoreET.requestFocus();
            return false;
        } else if (Integer.parseInt(scoreET.getText().toString()) <= 0 ) {
            scoreET.setError( "Score must be greater than 0" );
            scoreET.requestFocus();
            return  false;
        } else {
            score = Integer.parseInt(scoreET.getText().toString());
        }

        // Check if data has correct format
        if (TextUtils.isEmpty( dateET.getText() )) {
            date = curDate;
        } else if (!isValidFormat( "MM/dd/yyyy", dateET.getText().toString(), Locale.ENGLISH )) {
            dateET.setError( "Invalid date format" );
            dateET.requestFocus();
            return false;
        } else {
            date = dateET.getText().toString();
        }
        return true;
    }*/

    /**
     * Method to check if dateET has the current format
     * @param format
     * @param value
     * @param locale
     * @return true if it is valid, false otherwise
     */
    /*private boolean isValidFormat(String format, String value, Locale locale) {
        LocalDateTime ldt = null;
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern( format, locale);

        // Check to see if the format matches
        try {
            ldt = LocalDateTime.parse(value, fomatter);
            String result = ldt.format(fomatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, fomatter);
                String result = ld.format(fomatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, fomatter);
                    String result = lt.format(fomatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                    // Debugging purposes
                    //e2.printStackTrace();
                }
            }
        }
        return false;
    }*/
}

