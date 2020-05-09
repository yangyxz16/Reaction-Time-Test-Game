package com.example.xxy180008asg5;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *  Class of file input/output, which store data to a file and read data from a file
 *
 *  Written by John Cole
 *  Modified by Xizhen Yang for CS6326.001, Assignment 5, starting March 28, 2020
 *  NetID: XXY180008
 */
public class FileIO {

    /**
     * Method to save the list of score objects to an internal file
     * @param fileDir the current program's directory
     * @param fileName the file's name
     * @param scoreList
     */
    public static void toFile(File fileDir, String fileName, ArrayList<GameScores> scoreList) {
        File dir;
        PrintWriter writer = null;

        try {
            // Initialize the file that will be used for output
            dir = new File(fileDir, fileName);
            // Writer used to write to a file
            writer = new PrintWriter(dir);
        } catch(Exception e) {
            e.printStackTrace();
        }

        // Write each score objects
        for (GameScores gs : scoreList) {
            writer.println(gs.toString());
        }

        writer.close();
    }

    /**
     * Method to read the from an internal file and create a list of score objects
     * @param fileDir the current program's directory
     * @param fileName the file's name
     * @return list of score objects
     */
    public static ArrayList<GameScores> fromFile(File fileDir, String fileName) {
        ArrayList<GameScores> scoreList = new ArrayList<>();

        try {
            // Initialize the file that will be used for input
            File dir = new File(fileDir, fileName);
            Scanner scanner = new Scanner( dir );

            while(scanner.hasNextLine()) {
                String[] fields = scanner.nextLine().split( "," );
                //Create an object for each line
                GameScores gs = new GameScores( Integer.parseInt(fields[0]),  Integer.parseInt(fields[1]), fields[2]);

                // Add to the list
                scoreList.add(gs);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return scoreList;
    }
}
