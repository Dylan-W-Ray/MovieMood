package movieReviewClassification;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * File Name: ReviewHandler.java
 *
 * @author: Dylan W. Ray (dwr48)
 * Date:11/18/2020
 * Assignment Title: Sentiment Analysis (Stage 3)
 * CS3354-Fall 2020
 * Instructor: Junye Wen
 *
 * The ReviewHandler.java class is an extension of the AbstractReviewHandler.java
 * class. This classes overrides and implements all abstract methods from its super
 * class in order to create, load into a HashMap, and save a HashMap of MovieReview
 * objects. This class simulates its console text output for GUI integration using
 * a field JTextArea component and two getters to send a String of text or the
 * entire JTextArea object.
 */
public class ReviewHandler extends AbstractReviewHandler {

    /**
     * An Integer object to that is used to mark
     * the number at which new MovieReview ids
     * should take their value.
     */
    private Integer idCounter;

    /**
     * A JTextArea component used to simulate output
     * to the console. Using this Jcomponent and two
     * two simple getters text output originally
     * meant for the console can be re-directed and
     * brought to the GUI level either as the JTextArea
     * itself or as simple text.
     */
    private JTextArea textContent=new JTextArea();


    /**
     * Loads reviews from a given path. If the given path is a .txt file, then
     * a single review is loaded. Otherwise, if the path is a folder, all reviews
     * in it are loaded.
     *
     * @param filePath  The path to the file (or folder) containing the review(sentimentModel).
     * @param realClass The real class of the review (0 = Negative, 1 = Positive
     *                  2 = Unknown).
     */
    @Override
    public void loadReviews(String filePath, int realClass) {

        int originalDBSize=database.size();//An integer to capture the database size at this moment.
        int posPolarity=0;//The number of newly created MovieReview object with a predicted positive sentiment.
        int negPolarity=0;//The number of newly created MovieReview objects with a predicted negative sentiment.
        boolean loaded=false;//A boolean flag used for the logical progression of this method.


        //The given path leads to a single text file.
        if(filePath.endsWith(".txt")) {
            //Creating a single new MovieReview object, counting its number
            //of respective predicted sentiment, adding it to the HashMap
            //database, then calling AccuracyReport() to compute and
            //display relevant information. An error message is displayed
            //if file path is incorrect or does not exits.
            try {
                MovieReview review=readReview(filePath, realClass);

                if(review.getPredictedPolarity()==1) {
                    posPolarity++;
                }
                else {
                    negPolarity++;
                }

                database.put(review.getId(), review);
                textContent.setText("REVIEW(S) LOADED:");
                textContent.append(""+(database.size()-originalDBSize));

                if(realClass!=2&&database.size()!=0) {
                    textContent.setText("REVIEW(S) LOADED.\n");
                    AccuracyReport(originalDBSize, posPolarity, negPolarity, realClass);
                }
            }
            catch(IOException e) {
                textContent.setText("ERROR:FILE NOT FOUND:\n");
                textContent.append(filePath+"\n");
                textContent.append("CHECK IF PATH IS CORRECT OR FILE EXISTS\n");
            }
        }

        //The given path leads to a folder of text files.
        else {
            //Initializing the proper File object, array of review file names,
            //and finding the OS's file path separator character.
            File movieReviews=new File(filePath);
            String[] reviewsList=movieReviews.list();
            String fileSeparatorChar=System.getProperty("file.separator");

            //Error message if the folder path is bad.
            if(reviewsList==null) {
                textContent.setText("ERROR:FOLDER NOT FOUND:\n");
                textContent.append(filePath+"\n");
                textContent.append("CHECK IF PATH IS CORRECT OR FOLDER EXISTS\n");
            }
            else {

                //Looping through the given folder and creating
                //all file paths within.
                for(int i=0; i<reviewsList.length; i++) {

                    //Skipping non txt files, throwing error
                    //if no txt files exist.
                    try {
                        while(!reviewsList[i].endsWith(".txt")) {
                            i++;
                        }
                    }
                    catch(Exception e) {
                        if(originalDBSize!=database.size())
                            textContent.setText("ERROR:FILE(S) NOT FOUND:\n");
                        textContent.append(filePath+"\n");
                        textContent.append("CHECK IF PATH IS CORRECT OR FILE(S) EXISTS\n");
                    }

                    //Creating a specific reviews file path, with in the current folder.
                    String reviewFile="";
                    try {
                        reviewFile=filePath+fileSeparatorChar+reviewsList[i];
                    }
                    catch(Exception e) {
                        textContent.setText("ERROR:FILE NOT FOUND:\n");
                        textContent.append(filePath+"\n");
                        textContent.append("CHECK IF PATH IS CORRECT OR FILES ");
                        textContent.append("EXISTS\n");
                    }
                    //Creating a single new MovieReview object, counting its number
                    //of respective predicted sentiment, adding it to the HashMap
                    //database. Setting the loaded flag to ture. An error message
                    // is displayed if file path is incorrect or does not exits.
                    try {
                        MovieReview review=readReview(reviewFile, realClass);
                        if(review.getPredictedPolarity()==1) {
                            posPolarity++;
                        }
                        else {
                            negPolarity++;
                        }
                        database.put(review.getId(), review);
                        loaded=true;
                    }
                    catch(IOException e) {
                        textContent.setText("ERROR:FILE NOT FOUND:\n");
                        textContent.append(filePath+"\n");
                        textContent.append("CHECK IF PATH IS CORRECT OR FILES ");
                        textContent.append("EXISTS\n");
                    }
                }
                //Displaying a commandline message indicating that
                //the reviews have been loaded into the HashMap
                //database, and calling AccuracyReport() to compute and
                //display relevant information
                if(loaded) {
                    textContent.setText("REVIEW(S) LOADED:");
                    textContent.append(""+(database.size()-originalDBSize));
                }
                if(realClass!=2&&database.size()!=0) {
                    AccuracyReport(originalDBSize, posPolarity, negPolarity, realClass);
                }
            }
        }
    }

    /**
     * Reads a single review file and returns it as a MovieReview object.
     * This method also calls the method classifyReview to predict the polarity
     * of the review.
     *
     * @param reviewFilePath A path to a .txt file containing a review.
     * @param realClass      The real class entered by the user.
     * @return a MovieReview object.
     * @throws IOException if specified file cannot be opened.
     */
    @Override
    public MovieReview readReview(String reviewFilePath, int realClass)
            throws IOException {

        //Object instance of the MovieReview bing created.
        MovieReview review=new MovieReview();

        //Creating the File object of the reviews file path.
        File movieReview=new File(reviewFilePath);


        //Collecting and setting the MovieReview's text.
        FileReader reviewReader=new FileReader(movieReview);
        Scanner inFile=new Scanner(reviewReader);
        String reviewText="";

        while(inFile.hasNextLine()) {
            reviewText+=inFile.nextLine();
        }
        review.setText(reviewText);

        //Setting the MovieReviews real sentiment.
        review.setRealPolarity(realClass);

        //Setting the MovieReviews machine predicted sentiment.
        this.classifyReview(review);

        //Setting the MovieReview id.
        while(database.containsKey(idCounter)) {
            idCounter++;
        }
        review.setId(idCounter);

        return review;
    }


    /**
     * Deletes a review from the database, given its id.
     *
     * @param id The id value of the review.
     */
    @Override
    public void deleteReview(int id) {
        //Pulling the MovieReview object out of the HasMap Database
        //if it exists.
        MovieReview review=(MovieReview) database.remove(id);

        //Displaying the proper message depending on if
        //a MovieReview Object was actually removed from
        //the HashMap database.
        if(review!=null) {
            textContent.setText("REVIEW "+id+" REMOVED.");
        }
        else {
            textContent.setText("NO REVIEW WITH MATCHING ID:"+id);
        }
    }

    /**
     * Saves the database in the working directory as a serialized object,
     * along with saving the Integer idCounter.
     */
    @Override
    public void saveSerialDB() {
        textContent.setText("Saving database...");
        //serialize the database and idCounter.
        OutputStream file=null;
        OutputStream buffer=null;
        ObjectOutput output=null;
        try {
            file=new FileOutputStream(DATA_FILE_NAME);
            buffer=new BufferedOutputStream(file);
            output=new ObjectOutputStream(buffer);

            output.writeObject(database);
            output.writeInt(idCounter);

            output.close();
        }
        catch(IOException ex) {
            System.err.println(ex.toString());
            ex.printStackTrace();
        }
        finally {
            close(file);
        }
        textContent.append("Done.");
    }


    /**
     * Loads review database, along with the idCounter. This code
     * is a copypasta of the saveSerialDB() method with all of the
     * output objects changed to input objects.
     */
    @Override
    public void loadSerialDB() {

        textContent.setText("LOADING DATABASE...");

        //deserializing the database and idCounter.
        //Proper messages are displayed to the
        //commandline if a database.ser file was
        //found and de-serialized.
        InputStream file=null;
        InputStream buffer=null;
        ObjectInputStream input=null;
        try {
            file=new FileInputStream(DATA_FILE_NAME);
            buffer=new BufferedInputStream(file);
            input=new ObjectInputStream(buffer);

            database=(HashMap) input.readObject();
            idCounter=input.readInt();
            textContent.append("DONE ("+database.size()+" REVIEWS)");

            input.close();
        }
        catch(IOException|ClassNotFoundException e) {
            textContent.append("DATABASE INACCESSIBLE");
            idCounter=1;
            return;
        }
        finally {
            close(file);
        }
    }

    /**
     * Searches the review database by id.
     *
     * @param id The id to search for.
     * @return The review that matches the given id or null if the id does not
     * exist in the database.
     */
    @Override
    public MovieReview searchById(int id) {
        //Pulling a copy of a MovieReview object out
        // of the HasMap Database if it exists.
        MovieReview review=(MovieReview) database.get(id);

        //Determining the proper return value.
        if(review!=null) {
            return review;
        }
        else {
            return null;
        }
    }

    /**
     * Searches the review database for reviews matching a given substring.
     *
     * @param substring The substring to search for.
     * @return A list of review objects matching the search criterion.
     */
    @Override
    public List<MovieReview> searchBySubstring(String substring) {

        //Pulling a copy of all MovieReview objects out of the HashMap
        //database in the form of a Java Collection.
        Collection<MovieReview> reviews=database.values();

        //Creating an empty List of MovieReview objects ready to be
        //filled with review that have matching substrings.
        List<MovieReview> reviewsFound=new ArrayList<MovieReview>();

        //Iterating through the Collection of MovieReview object
        //and adding ones that have a matching substring to the
        //List.
        for(MovieReview review : reviews) {
            if(review.getText().contains(substring)) {
                reviewsFound.add(review);
            }
        }

        //Determining the proper return value.
        if(reviewsFound.isEmpty()) {

            return null;
        }
        else {
            return reviewsFound;
        }
    }

    /**
     * This method, given a single MovieReview instance,
     * generates a 2D String array from the MovieReviews
     * data for later use in a GUI level interface.
     *
     * @param review A given MovieReview object which
     *               will have its field data converted
     *               to a 2D String array.
     * @return String[][]
     */
    public String[][] getJTableInput(MovieReview review) {

        //Creating strings for each member field of the
        //MovieReview object.
        String id=""+review.getId();
        String txt=review.getText();
        String predictedPolarity;
        String realPolarity;

        //Determining and setting the proper text based output
        //for the MovieReviews corresponding predicted and real
        //sentiments.
        if(review.getPredictedPolarity()==1) {
            predictedPolarity="Positive";
        }
        else {
            predictedPolarity="Negative";
        }
        if(review.getRealPolarity()==1) {
            realPolarity="Positive";
        }
        else if(review.getRealPolarity()==0) {
            realPolarity="Negative";
        }
        else {
            realPolarity="Unknown";
        }

        //Outputting all pieces of the field data in a 2D String data type.
        String reviewData[][]={{id, txt, predictedPolarity, realPolarity}};
        return reviewData;
    }

    /**
     * An overloaded version of the getJTableInput(MovieReview review) method
     * that, given a List object of MovieReview instances,
     * generates a 2D String array from the MovieReviews
     * data for later use in a GUI level interface.
     *
     * @param reviewList A given List of MovieReview objects
     *                   which will have their field data
     *                   converted to a 2D String array.
     * @return String[][]
     */
    public String[][] getJTableInput(List<MovieReview> reviewList) {

        //Initializing the 2D String array with the proper size.
        String reviewsData[][]=new String[reviewList.size()][4];

        //Iterating through the given List of MovieReview
        //objects and populating each row index with the
        //proper MovieReview data. A manually implemented mixed
        //for each and normal for loop is used.
        int i=0;
        for(MovieReview review : reviewList) {
            String singleReviewData[][]=this.getJTableInput(review);
            for(int j=0; j<singleReviewData[0].length; j++) {
                reviewsData[i][j]=singleReviewData[0][j];
            }
            i++;
        }
        return reviewsData;
    }

    /**
     * A getter method to return the current size
     * of the HashMap database.
     *
     * @return The current size of the HashMap database.
     */
    public int getDatabaseSize() {
        return database.size();
    }

    /**
     * This method, given the current size of database, the number
     * of newly added MovieReview objects with positive sentiment,
     * the number of newly added MovieReview objects with negative
     * sentiment, and the real sentiment of all the newly added
     * MovieReview objects to the database, will set the text of
     * the textContent GUI field component to a summary
     * accuracy report of the Sentiment Analysis DNN classification.
     *
     * @param currentSize The current size of database before new MovieReview
     *                    objects have been added.
     * @param posNum      The number of newly added MovieReview objects to database
     *                    with a predicted positive sentiment.
     * @param negNum      The number of newly added MovieReview objects to database
     *                    with a predicated negative sentiment.
     * @param realClass   The real sentiment of all the newly added MovieReview
     *                    objects to database.
     * @return void.
     */
    private void AccuracyReport(int currentSize, int posNum, int negNum, int realClass) {

        //Initializing and declaring proper variables to
        //store the relevant information to be computed
        //and printed.
        int totalEntries=database.size()-currentSize;//Total number of new MovieReview objects.
        int correctNum=0;//Number of MovieReview objects with correctly predicted sentiment.
        int incorrectNum=0;//Number of MovieReview objects with incorrectly predicted sentiment.
        double accuracy=0;//Percentage of how many MovieReview objects were correctly classified.

        //Computing proper information if the real sentiment
        //of the new MovieReview objects is positive.
        if(realClass==1) {
            correctNum=totalEntries-negNum;
            incorrectNum=totalEntries-posNum;
            accuracy=((float) correctNum/totalEntries)*100;
        }
        //Computing proper information if the real sentiment
        //of the new MovieReview objects is negative.
        else {
            correctNum=totalEntries-posNum;
            incorrectNum=totalEntries-negNum;
            accuracy=((float) correctNum/totalEntries)*100;

        }

        //Displaying the information to the commandline.
        textContent.append("Number of entries: "+totalEntries+"\n");
        textContent.append("Correctly classified: "+correctNum+"\n");
        textContent.append("Misclassified: "+incorrectNum+"\n");
        textContent.append("Accuracy: "+String.format("%.1f", accuracy)+"%\n");
        textContent.append("Database size: "+database.size()+"\n");
    }

    /**
     * This method returns the text, in String form, from
     * the JTextArea textContent member field instance.
     *
     * @return String.
     */
    public String getTextContent() {
        return textContent.getText();
    }

    /**
     * This method returns the JTextArea textContent member field
     * to be integrated into a GUI interface.
     *
     * @return JTextArea object.
     */
    public JTextArea getTextArea() {
        return textContent;
    }
}
