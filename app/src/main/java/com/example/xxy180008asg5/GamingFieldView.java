package com.example.xxy180008asg5;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Class for the gaming field custom view which is called from the game activity,
 * which contains the gaming components and logic
 *
 * Written by Xizhen Yang for CS6326.001, Assignment 5, starting March 28, 2020
 * NetID: XXY180008
 */
public class GamingFieldView extends View {
    ArrayList<Shape> shapeList; // An ArrayList to store the origin x and y coordinates and radius for each shape

    // Variables for the height and width of the gaming field
    int height;
    int width;

    Paint p; // Initialize a new paint object

    android.os.Handler h; // Handler
    Runnable r;

    // Variable for lifetime of a shape in milliseconds
    // Initialize the frameRate: 0.1 second
    final int frameRate = 100;

    long startTime; // Variable for start time in millisecond

    int penaltyTime = 0; // Variable for the penalty time that needs to be add in Game Activity

    Random random = new Random();
    // Since there are 7 colors, randomly generate a number between 1 to 7
    final int colorIndex = random.nextInt((7 - 1) + 1) + 1;
    // Generate shape type and color randomly as the game rule
    final int ruleShapeType = randomCircleSquare();
    final int ruleShapeColor = randomGenerateColor(colorIndex);

    // Variable for the time that is taken for the correct shape to be touched
    int correctTouchReactionTime = 0;

    /**
     * Original method for custom view
     * @param context
     */
    public GamingFieldView(Context context) {
        super( context );

        init(null);
    }
    /**
     * Original method for custom view
     * @param context
     */
    public GamingFieldView(Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
        init(attrs);
    }
    /**
     * Original method for custom view
     * @param context
     */
    public GamingFieldView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        init(attrs);
    }
    /**
     * Original method for custom view
     * @param context
     */
    public GamingFieldView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super( context, attrs, defStyleAttr, defStyleRes );
        init(attrs);
    }

    /**
     * Method the initialize the view
     */
    private void init(@Nullable AttributeSet set) {
        // Initialize the ArrayList
        shapeList = new ArrayList<>();

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        //int width = metrics.widthPixels;
        //int height = metrics.heightPixels;
        height = (int) Math.round( metrics.heightPixels * 2 / 3 );
        width = metrics.widthPixels;

        // Initialize the Paint project
        p = new Paint();

        // Call the function to create shapes
        createShapesss();

        h = new Handler();

        // Initialize the start time to current time
        startTime = SystemClock.elapsedRealtime();

        // Initialize the runnable object
        r = new Runnable() {
            @Override
            public void run()
            {
                invalidate();
            }
        };

    }

    /**
     * Method to fill in contents
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // When the current time minus the last recorded time in second is 1,
        // which means one second has passed
        if ( TimeUnit.MILLISECONDS.toSeconds(SystemClock.elapsedRealtime() - startTime ) == 1) {
            updateShapeLifeTime();
            startTime = SystemClock.elapsedRealtime(); // Reset to 0
        }

        for (Shape shape: shapeList) {
            p.setColor( shape.getColor() );

            if (shape.getType() == 0) { // A circle
                // Draw the circle on the canvas
                canvas.drawCircle( shape.getX(), shape.getY(), shape.getLength(), p );

            } else { // A square
                // Initialize a new Rect object
                Rect square = new Rect();
                // Describe square's location
                square.left = (int)shape.getX();
                square.top = (int)shape.getY();
                square.right = (int)(shape.getX() + shape.getLength());
                square.bottom = (int)(shape.getY() + shape.getLength());
                // Draw the circle on the canvas
                canvas.drawRect( square, p );
            }
        }

        // The Canvas will be refreshed every "frameRate" second
        h.postDelayed(r, frameRate);
    }


    /**
     * Method to determine if a user touches the correct shape
     * @param x coordinate
     * @param y coordinate
     * @return true if touches correctly and remove the shape from the view and list
     */
    public boolean correctTouch(float x, float y) {
        Boolean correct = false;

        for (int i = 0; i < shapeList.size(); i++) {
            Shape shape = shapeList.get( i );

            if (shape.getType() == 0) { // A circle
                // The distance of between the touch and the origin of the circle
                float distance =  (float)Math.sqrt((y - shape.getY()) * (y - shape.getY()) + (x - shape.getX()) * (x - shape.getX()));
                // Distance is less than the radius,
                // which means the shape is touched
                if (distance <= shape.getLength()) {
                    if (shape.getType() == ruleShapeType && shape.getColor() == ruleShapeColor) { // Correct shape and color
                        // Time that takes for user to click this shape
                        correctTouchReactionTime = shape.gerLife() - shape.getLifeTime();
                        correct = true;
                    }
                    // Remove the shape from the list
                    shapeList.remove( i );
                    // Create a new shape
                    createAShape();
                }
            } else { // A square
                // The touch point in the range of the square
                if (x >= shape.getX()
                        && y >= shape.getY()
                        && x <= shape.getX()+shape.getLength()
                        && y <= shape.getY()+shape.getLength()) {
                    if (shape.getType() == ruleShapeType && shape.getColor() == ruleShapeColor) { // Correct shape and color
                        // Time that takes for user to click this shape
                        correctTouchReactionTime = shape.gerLife() - shape.getLifeTime();
                        correct = true;
                    }
                    // Remove the shape from the list
                    shapeList.remove( i );
                    // Create a new shape
                    createAShape();
                }
            }
        }
        return correct;
    }

    /**
     * Method to reduce life time for each shape by 1
     * If the life time becomes 0, remove from the list,
     * and create a new shape to the list
     */
    private void updateShapeLifeTime() {
        for (int i = 0; i < shapeList.size(); i++) {
            Shape shape = shapeList.get( i );

            // Each shapes' lifetime reduce by one
            int lifeTime = shape.getLifeTime() - 1;
            if (lifeTime == 0) { // Life time ends

                if (shape.getType() == ruleShapeType && shape.getColor() == ruleShapeColor) { // Correct shape and color
                    // Since it is the correct shape,
                    // Its lifetime will be added to the total time in Game Activity
                    penaltyTime += shape.gerLife();

                }
                shapeList.remove( i );
                createAShape();
            } else  {
                shape.setLifeTime(lifeTime);
            }
        }
    }

    /**
     * Method that return the time that is taken for the correct shape to be touched
     * @return reaction time
     */
    public int getCorrectTouchReactionTime() {
        return correctTouchReactionTime;
    }

    /**
     * Method that return the penalty time that needs to be add in Game Activity
     * @return penalty time
     */
    public int getPenaltyTime() {
        int pt = penaltyTime;
        penaltyTime = 0; // Reset to zero
        return pt;
    }


    /**
     * Method to create a random number of shapes
     */
    private void createShapesss() {
        // A variable for a random number of circles or squares to be created
        int numOfShapes = randomNumShapes();
        // Create a random number of shapes
        for (int i = 0; i < numOfShapes; i++) {
            createAShape();
        }
    }

    /**
     * Method to create a shape and add to the list
     */
    private void createAShape() {
        // Randomly generate the shape - circle / square
        int circleOrSquare = randomCircleSquare();

        // Randomly generate the color of the shape
        int colorIndex = random.nextInt((7 - 1) + 1) + 1;
        int color = randomGenerateColor(colorIndex);

        // Randomly generate the lifetime of the shape
        int lifeTime = randomLifetime();


        float[] cord = generateShapeCord(circleOrSquare);

        // Check whether the circle touches border
        // Keep generating new circles until it does not touch
        while(overlap(cord, circleOrSquare)) cord = generateShapeCord(circleOrSquare);

        // Add the circle coordinates to the list
        // When a new shape is created, its life is equal to lifeTime
        shapeList.add( new Shape( circleOrSquare, color, lifeTime, lifeTime, cord[0], cord[1], cord[2]) );
    }

    /**
     * Method to generate shape coordinates
     * If the shape is a circle, generate x and y coordinates of the origin and origin
     * If the shape is a square, generate x and y coordinates of the top left corner and length
     * @param type of the shape
     * @return x, y, length
     */
    private float[] generateShapeCord(int type) {
        float[] coordinates = new float[3];

        Random random = new Random();
        coordinates[0] = random.nextInt(width - 10 + 1) + 10;
        coordinates[1] = random.nextInt(height - 10 + 1) + 10;

        // Random assign the length of the square
        float length = randomShapeLength();

        if (type == 0) { // Circle: generate the radius
            coordinates[2] = length / 2;
        } else { // Square: generate the length
            coordinates[2] = length;
        }

        return coordinates;
    }

    /**
     * Method to convert square coordinates to circle coordinates
     * @param x of the square
     * @param y of the square
     * @param length of the square
     * @return coordinates as a circle
     */
    private float[] squareToCircle(float x, float y, float length) {
        float[] circleCord = new float[3];

        float halfLength = length / 2; // Half of the length of the square

        circleCord[0] = x + halfLength; // x coordinate of the origin
        circleCord[1] = y + halfLength; // y coordinate of the origin
        circleCord[2] = (float) (halfLength * Math.sqrt( 2 )); // radius = length / 2 * sqr(2)

        return circleCord;
    }

    /**
     * Method to determine:
     * if the shape touches or overlaps with the border,
     * or overlap with other shapes
     * @param cord x, y, and length of the shape
     * @param type type of the shape
     * @return true if touches or overlaps; false if not
     */
    private boolean overlap(float[] cord, int type) {
        if (type == 1) { // The shape is a square
            cord = squareToCircle(cord[0], cord[1], cord[2]); // Convert square coordinates to circle coordinates
        }

        float x = cord[0]; // x coordinate of the origin
        float y = cord[1]; // y coordinate of the origin
        float r = cord[2]; // radius

        // Check if the shape touches or overlaps with the border
        if (x - r < 20 || y - r < 20 || x + r > width - 20 || y + r > height - 20) {
            return true;
        }

        if (shapeList.size() > 0) {
            // CHeck if the shape overlap with any other shapes
            for (Shape shape : shapeList) {
                // The shape that is comparing to
                float x1 = shape.getX();
                float y1 = shape.getY();
                float r1 = shape.getLength();

                // The shape is a square
                if (shape.getType() == 1) {
                    float[] cord1 = squareToCircle( shape.getX(), shape.getY(), shape.getLength());
                    x1 = cord1[0];
                    y1 = cord1[1];
                    r1 = cord1[2];
                }

                // The distance of between two origins of the circles
                float originDistance =  (float)Math.sqrt((y1 - y) * (y1 - y) + (x1 - x) * (x1 - x));

                // If the sun of radius of two circles is greater than the distance of two origins
                // Two must overlap!
                if (originDistance < r + r1) return true;
            }
        }

        return false;
    }




    /**
     * Method to generate random shape type and color as shape rules
     * @return type and color
     */
    public int[] getShapeRules() {
        int[] typeAndColor = new int[2];
        typeAndColor[0] = ruleShapeType;
        typeAndColor[1] = colorIndex;
        return typeAndColor;
    }

    /**
     * Method to randomly generate a color for the paint of the shape
     * Possible colors: Red, Orange, Yellow, Green, Blue, Purple, and White
     * @return the Paint with a randomly generated color
     */
    private int randomGenerateColor(int randomNum) {
        switch (randomNum) {
            case 1: return Color.RED ;
            case 2: return Color.parseColor("#FF7133"); // orange
            case 3: return Color.YELLOW;
            case 4: return Color.GREEN;
            case 5: return Color.BLUE;
            case 6: return Color.parseColor("#E433FF"); // purple
            default: return Color.WHITE;
        }
    }

    /**
     * Method to generate a random number of shapes
     * @return a number between 6 - 12
     */
    private int randomNumShapes() {
        Random random = new Random();
        return random.nextInt(12 - 6 + 1) + 6;
    }

    /**
     * Method to generate a random number to determine either a circle or a square
     * @return 0 as circle, 1 as square
     */
    private int randomCircleSquare() {
        Random random = new Random();
        return random.nextInt(1 + 1);
    }

    /**
     * Method to generate the length of diameter/side of the shape
     * @return a number between 32 - 64
     */
    private float randomShapeLength() {
        Random random = new Random();
        int length = random.nextInt((64 - 32) + 1) + 32;
        float lengthDP = length * Resources.getSystem().getDisplayMetrics().density;
        return lengthDP;
    }

    /**
     * Method to generate the lifetime of a shape
     * @return a number between 3 - 7 seconds
     */
    private int randomLifetime() {
        Random random = new Random();
        int seconds = random.nextInt((7 - 3) + 1) + 3;
        return seconds;
    }
}
