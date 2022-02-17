package movieReviewClassification;

import java.io.Serializable;

/**
 * File Name: MovieReview.java
 *
 * @author: Dylan W. Ray (dwr48)
 * Date:10/28/2020
 * Assignment Title: Sentiment Analysis (Stage 2)
 * CS3354-Fall 2020
 * Instructor: Junye Wen
 *
 * The MovieReview.java is a class which implements an object
 * representation of a written movie review. An object ot type
 * MovieReview contains member fields to store the review's
 * text, its human determined sentiment (polarity, 0=neg, 1=pos, 2=unknown),
 * its DNN determined sentiment (predicted polarity, 0==neg, 1=pos), and an
 * id for the MovieReview object. Getter and setter methods are implemented
 * for each member field.
 */

public class MovieReview implements Serializable {

    /**
     * The pure plain text of the review.
     */
    private String reviewText;

    /**
     * The real, human determined, sentiment classification of the review.
     */
    private int realPolarity;

    /**
     * The predicted, machine determined, sentiment classification of the
     *  review.
     */
    private int predictedPolarity;

    /**
     * The ID of the review in the "database".
     */
    private int id;

    /**
     * Sets the String member field, reviewText, as the text of a review.
     * @param text A String containing the movie reviews text.
     * @return void.
     */
    public void setText(String text) {
        reviewText=text;
    }

    /**
     * Gets the String member field, reviewText, and returns it.
     * @return String:reviewText The variable storing the MovieReviews text.
     */
    public String getText() {
        return reviewText;
    }

    /**
     * Sets the int predictedPolarity member field of the review.
     * @param predictedSentiment The DNN predicted polarity of the review.
     * @return void.
     */
    public void setPredictedPolarity(int predictedSentiment) {
        predictedPolarity=predictedSentiment;
    }

    /**
     * Gets the int predictedPolarity member field of a review and returns it.
     * @return The int field predictedPolarity.
     */
    public int getPredictedPolarity() { return predictedPolarity; }

    /**
     * Sets the int realPolarity member field of the review.
     * @param realSentiment The user input polarity of the review.
     * @return void.
     */
    public void setRealPolarity(int realSentiment) {
        realPolarity=realSentiment;
    }

    /**
     * Gets the int realPolarity member field of a review and returns it.
     * @return The int field realPolarity.
     */
    public int getRealPolarity() {
        return realPolarity;
    }

    /**
     * Sets the int id member field of the review.
     * @param identification the id integer of the review.
     */
    public void setId(int identification) {
        id=identification;
    }

    /**
     * Gets the int id member field of a review and returns it.
     * @return void.
     */
    public int getId() {
        return id;
    }
}