package movieReviewClassification;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 * File Name: ReviewClassifierGUI.java
 *
 * @author: Dylan W. Ray (dwr48)
 * Code supporters: Francis W, Mikey Rae
 * Code sources: https://www.tutorialspoint.com/how-to-create-vertical-button-column-with-gridlayout-in-java
 * https://stackoverflow.com/questions/21534515/jfilechooser-open-in-current-directory
 * https://stackoverflow.com/questions/1990817/how-to-make-a-jtable-non-editable
 * Date:11/18/2020
 * Assignment Title: Sentiment Analysis (Stage 3)
 * CS3354-Fall 2020
 * Instructor: Junye Wen
 *
 * This class uses javax Swing/AWT to implement a movie review classification
 * program based upon the MovieReview.java, ReviewHandler.java, and
 * AbstractMovieReviewHandler.java classes/interfaces. A basic use of
 * java Threads, implicitly accessible through the java.lang library, is added
 * to implement multithreading capabilities. The bulk of the GUI construction is
 * handled in this classes constructor or in subsequent methods that code for
 * component actions. All of the GUI components related ActionListeners
 * have the code needing to be executed, upon an action, separated in a
 * single method of this class. All Threads of this class are simple and
 * only execute one call of a ReviewHandler method.
 */
public class ReviewClassifierGUI {

    public static void main(String[] args) {
        //Creating an instance of my GUI application in the main method
        //Thread so all subsequent code will run.
        ReviewClassifierGUI primeGUI=new ReviewClassifierGUI();

    }

    //Data type fields:
    /**
     * An instance of a ReviewHandler object to manipulate
     * and work with MovieReview data objects.
     */
    public static ReviewHandler reviewsManager=new ReviewHandler();

    /**
     * An integer to store the reviews real sentiment.
     * This field will be used in conjunction with
     * Threads to simulate arguments/parameters/
     * returns for a Thread.
     */
    public static int realClass;

    /**
     * An integer variable to store MovieReview object
     * id's given by the user. This field will be used
     * in conjunction with Threads in order to simulate
     * arguments/parameters/returns for a Thread.
     */
    public static int id;

    /**
     * A String object to store a potential substring of
     * MovieReview text given by the user. This field
     * will be used in conjunction with Threads in order
     * to simulate arguments/parameters/returns for a
     * Thread.
     */
    public static String reviewText;

    /**
     * String to hold the review(s) file/folder path.
     * This field will be used in conjunction with
     * Threads in order to simulate arguments/parameters/
     * returns for a Thread.
     */

    public static String reviewPath;

    /**
     * A single instance of a MovieReview object.
     * This field will be used in conjunction with
     * Threads in order to simulate arguments/parameters/
     * returns for a Thread.
     */
    public static MovieReview reviewInst;

    /**
     * A List object of MovieReview objects. This field
     * will be used in conjunction with Threads in order
     * to simulate arguments/parameters/returns for a Thread.
     */
    public static List<MovieReview> movieReviewsList;

    /**
     * A 2D String array which will contain the raw data,
     * in String form, or multiple deconstructed MovieReview
     * objects. This field will be used in conjunction with
     * Threads in order to simulate arguments/parameters/returns
     * for a Thread.
     */
    public static String movieReviewData[][];

    //Thread object fields:
    //Each Thread corresponds to a single call to a ReviewHandler
    //method. Previously declared member fields are used to handle
    //arguments and returns for the method calls within their personal
    //Threads.(This may be a poor implementation of Threads, but it made
    //logical sense to me).

    /**
     * This Thread encapsulates the ReviewHandler.loadSerialDB()
     * method call within its own Thread.
     */
    private static Thread loadReviewsDBThread=new Thread() {
        public void run() {
            reviewsManager.loadSerialDB();
        }
    };

    /**
     * This Thread encapsulates the ReviewHandler.saveSerialDB()
     * method call within its own Thread.
     */
    private static Thread saveReviewsThread=new Thread() {
        public void run() {
            reviewsManager.saveSerialDB();
        }
    };

    /**
     * This Thread encapsulates the ReviewHandler.loadReviews(String,int)
     * method call within its own Thread.
     * Simulated arguments:reviewPath, realClass.
     */
    private static Thread loadReviewsThread=new Thread() {
        public void run() {
            reviewsManager.loadReviews(reviewPath, realClass);
        }
    };

    /**
     * This Thread encapsulates the ReviewHandler.searchBySubstring(String)
     * method call within its own Thread.
     * Simulated arguments: reviewText.
     * Simulated return: movieReviewsList.
     */
    private static Thread searchSubReviewThread=new Thread() {
        public void run() {
            movieReviewsList=reviewsManager.searchBySubstring(reviewText);
        }
    };

    /**
     * This Thread encapsulates the ReviewHandler.getJTableInput(List MovieReview)
     * method call within its own Thread.
     * Simulated arguments: movieReviewsList.
     * Simulated return: movieReviewData.
     */
    private static Thread multiReviewTableDataThread=new Thread() {
        public void run() {
            movieReviewData=reviewsManager.getJTableInput(movieReviewsList);
        }
    };

    /**
     * This Thead encapsulates the ReviewHandler.searchById(int)
     * method call within its own Thread.
     * Simulated arguments: id.
     * Simulated return: reviewInst.
     */
    private static Thread searchIdReviewThread=new Thread() {
        public void run() {
            reviewInst=reviewsManager.searchById(id);
        }
    };

    //All further member fields are java Swing/AWT components, or
    //required Data type objects closely related to a component,
    //used in the construction of the GUI interface.
    //JFrame components:
    public static JFrame primeFrame=new JFrame("Movie Review Classifier");
    public static JFrame exitFrame=new JFrame();

    //JPanel components:
    public static JPanel rightPanel=new JPanel(new BorderLayout());
    public static JPanel leftPanel=new JPanel(new BorderLayout());
    public static JPanel infoPanel=new JPanel(new BorderLayout());
    public static JPanel inputPanel=new JPanel();
    public static JPanel menuPanel=new JPanel(new GridLayout(4, 1, 10, 20));
    public static JPanel reviewClassPanel=new JPanel(new GridLayout(4, 1, 10, 20));
    public static JPanel exitPanel=new JPanel(new FlowLayout());

    //JText components:
    public static JTextField searchField=new JTextField(50);
    public static JTextArea textOutput=new JTextArea();

    //JTable components and related fields:
    public static JTable outputTable;
    //A String[] used as the header in JTable output of MovieReview
    //member field data.
    public static String reviewHeaders[]={"ID", "TEXT", "PREDICTED", "REAL"};
    //Locally overriding the isCellEditable() method of the TableModel
    //class inorder to make all my tables non-editable.
    public static DefaultTableModel reviewModel=new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    //JScrollPane components:
    public static JScrollPane reviewTablePane;

    //JButtons components:
    public static JButton loadButton=new JButton("Load new movie review collection (given a folder or a file path)");
    public static JButton deleteButton=new JButton("Delete movie review from database (given its id)");
    public static JButton searchReviewButton=new JButton("Search movie reviews in database by id or by matching a substring");
    public static JButton exitButton=new JButton("Exit/Save program");
    public static JButton searchDeleteButton=new JButton("Search");
    public static JButton searchButton=new JButton("Search");
    public static JButton confirmDeleteButton=new JButton("Confirm");
    public static JButton cancelDeleteButton=new JButton("Cancel");
    public static JButton negativeButton=new JButton("Negative");
    public static JButton positiveButton=new JButton("Positive");
    public static JButton unknownButton=new JButton("Unknown");
    public static JButton exitConfirmButton=new JButton("Exit");
    public static JButton exitCancelButton=new JButton("Cancel");

    //JLabel components:
    public static JLabel classLabel=new JLabel("Please choose a review sentiment classification:");
    public static JLabel deleteLabel=new JLabel();
    public static JLabel exitFrameLabel=new JLabel("Are you sure you want to exit without saving?");

    //JFileChooser components and related fields:
    public static JFileChooser fileFolderInput=new JFileChooser();
    FileNameExtensionFilter reviewFilter=new FileNameExtensionFilter(".txt", "txt");

    //JCombobox components:
    JComboBox<String> searchMethod=new JComboBox<String>();

    //Simple border used template used throughout the program.
    Border simpleLineBorder=BorderFactory.createLineBorder(Color.BLACK, 2);

    //The constructor of this class handles the majority of interconnecting
    //GUI components and adds all ActionListeners to components which require them.
    public ReviewClassifierGUI() {
        //Initializing the data and information to be displayed first
        //by the GUI interface.
        loadReviewsDBThread.run();
        textOutput.setEditable(false);
        textOutput.setText(reviewsManager.getTextContent());
        fillReviewTable();

        //Designing the primary frame of the GUI and adding a closing action:
        primeFrame.getContentPane().setBackground(Color.GRAY);
        primeFrame.setSize(1500, 800);
        primeFrame.getContentPane().add(leftPanel, BorderLayout.WEST);
        primeFrame.getContentPane().add(rightPanel, BorderLayout.CENTER);
        primeFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitAction();
            }
        });

        //Setting borders for the main GUI components the user
        //will interact with in the interface.
        updateReviewsBorder();
        constructBorders();

        //Constructing the left content panel:
        //1.Information panel:
        leftPanel.add(infoPanel, BorderLayout.CENTER);
        infoPanel.add(textOutput, BorderLayout.CENTER);

        //2.Menu panel and actions:
        leftPanel.add(menuPanel, BorderLayout.NORTH);
        menuPanel.add(loadButton);
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                loadButtonAction();
            }
        });
        menuPanel.add(deleteButton);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                deleteButtonAction();
            }
        });
        menuPanel.add(searchReviewButton);
        searchReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                searchReviewButtonAction();
            }
        });
        menuPanel.add(exitButton);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitButtonAction();
            }
        });

        //Setting up input panel look and action:
        //1.Combobox:
        searchMethod.addItem("id");
        searchMethod.addItem("substring");
        searchMethod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                searchMethodAction();
            }
        });

        //2.Input panel button action:
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchButtonAction();
            }
        });
        searchDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                searchDeleteButtonAction();
            }
        });
        confirmDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmDeleteButtonAction();
            }
        });
        cancelDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelDeleteButtonAction();
            }
        });

        //Constructing review classification panel:
        reviewClassPanel.add(classLabel);
        reviewClassPanel.add(negativeButton);
        negativeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                negativeButtonAction();
            }
        });
        reviewClassPanel.add(positiveButton);
        positiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                positiveButtonAction();
            }
        });
        reviewClassPanel.add(unknownButton);
        unknownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                unknownButtonAction();
            }
        });

        //Constructing the settings for the file chooser component and giving it action:
        fileFolderInput.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileFolderInput.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileFolderInput.setAcceptAllFileFilterUsed(false);
        fileFolderInput.setFileFilter(reviewFilter);
        fileFolderInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if(event.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)) {
                    fileFolderInputCancelAction();
                }
                if(event.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                    fileFolderInputOpenAction();
                }
            }
        });

        //Making the primary frame visible.
        primeFrame.setVisible(true);
    }

    /**
     * updateReviewsBorder() updates the boarder of the panel which
     * contains the table displaying review data with the
     * current number of reviews in the MovieReview database.
     */
    public void updateReviewsBorder() {
        TitledBorder tableBorder=BorderFactory.createTitledBorder(simpleLineBorder,
                "Movie Reviews ("+reviewsManager.getDatabaseSize()+")");
        rightPanel.setBorder(tableBorder);
    }

    /**
     * constructBorders() creates and sets titled borders, based upon a member
     * field border template (simpleBorder), for all GUI components the
     * user will interact directly with.
     */
    public void constructBorders() {
        //Information panel border.
        TitledBorder infoBorder=BorderFactory.createTitledBorder(simpleLineBorder, "Information");
        infoPanel.setBorder(infoBorder);
        //Input Panel border.
        TitledBorder inputBorder=BorderFactory.createTitledBorder(simpleLineBorder, "Input");
        inputPanel.setBorder(inputBorder);
        //Menu panel border.
        TitledBorder menuBorder=BorderFactory.createTitledBorder(simpleLineBorder, "Menu");
        menuPanel.setBorder(menuBorder);
        //Review classification panel border.
        TitledBorder reviewClassBorder=BorderFactory.createTitledBorder(simpleLineBorder, "Review Class");
        reviewClassPanel.setBorder(reviewClassBorder);
        //File chooser border.
        TitledBorder fileFolderInputBorder=BorderFactory.createTitledBorder(simpleLineBorder, "File Chooser");
        fileFolderInput.setBorder(fileFolderInputBorder);
    }

    /**
     * fillReviewsTable() fills the GUI JTable component with the
     * data from all MovieReview objects currently stored in the
     * HashMap database.
     */
    public void fillReviewTable() {
        //Making sure the database has data to display.
        if(reviewsManager.getDatabaseSize()>0) {
            //Clearing the previous JTable
            //from the right panel.
            rightPanel.removeAll();
            rightPanel.repaint();

            //Setting the field reviewText to an empty String
            //and calling searchSubReviewThread.run() to collect
            //all MovieReview data of the current database,
            //in the correct form List<MovieReview>,
            //and initialize the movieReviewList member field.
            reviewText="";
            searchSubReviewThread.run();

            //Calling multiReviewTableThread.run() to convert
            //movieReviewList, using the ReviewHandler.getTableInput(),
            //into a String[][]. Then adding that data to a JTable
            //Model.
            multiReviewTableDataThread.run();
            reviewModel.setDataVector(movieReviewData, reviewHeaders);

            //Constructing the a new JTable with all new data
            //and adding it to the correct panel. Then redrawing
            //that panel.
            outputTable=new JTable(reviewModel);
            outputTable.getTableHeader().setReorderingAllowed(false);
            reviewTablePane=new JScrollPane(outputTable);
            rightPanel.add(reviewTablePane, BorderLayout.CENTER);
            rightPanel.revalidate();
        }
        else {
            //Clearing the previous JTable
            //from the right panel if the
            //database is emptied.
            rightPanel.removeAll();
            rightPanel.repaint();
            rightPanel.revalidate();
        }
    }

    /**
     * fillReviewTableBySubstring() fills the GUI JTable component with the
     * data from all MovieReview objects found by a matching substring search
     * through theHashMap database.
     * @return boolean, a flag to indicate that matching reviews were found.
     */
    public boolean fillReviewTableBySubstring() {
        //calling searchSubReviewThread.run() to collect
        //all MovieReview data of the current database,
        //in the correct form List<MovieReview>,
        //and initialize the movieReviewList member field.
        //Current String stored in member reviewText is used
        //as the search String.
        searchSubReviewThread.run();

        //If matching reviews were found the previous JTable is
        //cleared, movieReviewList is converted to String[][],
        //the new JTable is constructed/placed in its parent panel,
        //and the parent panel redrawn. The current states of
        //movieReviewData is used for the data.
        if(movieReviewsList!=null) {
            rightPanel.removeAll();
            multiReviewTableDataThread.run();
            reviewModel.setDataVector(movieReviewData, reviewHeaders);
            outputTable=new JTable(reviewModel);
            outputTable.getTableHeader().setReorderingAllowed(false);
            reviewTablePane=new JScrollPane(outputTable);
            rightPanel.add(reviewTablePane, BorderLayout.CENTER);
            rightPanel.repaint();
            rightPanel.revalidate();

            //The number of matching reviews found is output to the
            //information panel. Setting flag to true.
            textOutput.setText("REVIEWS FOUND:"+movieReviewsList.size());
            return true;
        }
        else {
            //Clearing the previous JTable from the right panel if
            //no matching reviews were found. Setting flag to false.
            rightPanel.removeAll();
            rightPanel.repaint();
            rightPanel.revalidate();
            return false;
        }
    }

    /**
     * fillReviewTableById() fills the GUI JTable component with the
     * data from a MovieReview objects found by a matching id search
     * through theHashMap database.
     */
    public void fillReviewTableById() {
        //Clearing the previous JTable
        //from the right panel.
        rightPanel.removeAll();
        rightPanel.repaint();

        //Using .getJTableInput and the current state of member
        //field reviewInst to get the MovieReview Objects correctly
        //formatted data.
        movieReviewData=reviewsManager.getJTableInput(reviewInst);

        //Constructing a new JTable using the current state of
        //movieReviewData, placing it in ints parent panel,
        //and redrawing the parent panel.
        reviewModel.setDataVector(movieReviewData, reviewHeaders);
        outputTable=new JTable(reviewModel);
        outputTable.getTableHeader().setReorderingAllowed(false);
        reviewTablePane=new JScrollPane(outputTable);
        rightPanel.add(reviewTablePane, BorderLayout.CENTER);
        rightPanel.revalidate();

        //The id of the matching review found is output to the
        //information panel.
        textOutput.setText("REVIEW:"+reviewInst.getId());
    }

    /**
     * searchMethodAction() changes the text displayed
     * in the search field when the user switches
     * between searching by substring or id.
     */
    public void searchMethodAction() {

        if(searchMethod.getSelectedItem().equals("id")) {
            searchField.setText("Please provide a review id (positive integer)");
            searchField.repaint();
        }
        if(searchMethod.getSelectedItem().equals("substring")) {
            searchField.setText("Please provide a substring of review text "+
                    "to find matching reviews(blank for all reviews)");
            searchField.repaint();
        }
    }

    /**
     * loadButtonAction() removes the file chooser
     * from the GUI interface if present, and adds
     * the review classification pane in its place.
     */
    public void loadButtonAction() {
        leftPanel.remove(fileFolderInput);
        leftPanel.repaint();
        leftPanel.add(reviewClassPanel, BorderLayout.SOUTH);
        primeFrame.revalidate();
    }

    /**
     * deleteButtonAction() updates the input panel
     * from its current state into a construction
     * for searching reviews by id. Adds the updated
     * panel to the master frame and redraws the frame.
     */
    public void deleteButtonAction() {
        inputPanel.removeAll();
        inputPanel.repaint();
        searchField.setText("Please provide review id (positive integer)");
        inputPanel.add(searchDeleteButton);
        inputPanel.add(searchField);
        primeFrame.add(inputPanel, BorderLayout.SOUTH);
        primeFrame.revalidate();
    }

    public void searchReviewButtonAction() {
        inputPanel.removeAll();
        inputPanel.repaint();
        searchField.setText("Please provide review id (positive integer)");
        inputPanel.add(searchButton);
        inputPanel.add(searchField);
        inputPanel.add(searchMethod);
        primeFrame.add(inputPanel, BorderLayout.SOUTH);
        primeFrame.revalidate();
    }

    /**
     * exitButtonAction() saves the current state of the
     * MovieReview database, creates a final message/goodbye
     * dialog frame, and closes the master frame.
     */
    public void exitButtonAction() {
        //Calling this Thread saves the current state
        //of the MovieReviews database.
        saveReviewsThread.run();

        //Constructed the closing frame.
        exitFrame.remove(exitPanel);
        exitFrame.repaint();
        exitFrame.setTitle("Goodbye!");
        exitFrameLabel.setText(reviewsManager.getTextContent());
        exitFrame.setSize(250, 70);
        exitFrame.add(exitFrameLabel);
        exitFrame.setLocationRelativeTo(primeFrame);
        exitFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Making the closing frame visible
        //and the master frame go away.
        exitFrame.revalidate();
        exitFrame.setVisible(true);
        primeFrame.dispose();
    }

    /**
     * negativeButtonAction() sets the realClass member field to
     * 0, removes the file chooser from the GUI interface if present,
     * and adds the review classification pane in its place.
     */
    public void negativeButtonAction() {
        realClass=0;
        leftPanel.remove(reviewClassPanel);
        leftPanel.repaint();
        leftPanel.add(fileFolderInput, BorderLayout.SOUTH);
        leftPanel.revalidate();
    }

    /**
     * positiveButtonAction() sets the realClass member field to
     * 1, removes the file chooser from the GUI interface if present,
     * and adds the review classification pane in its place.
     */
    public void positiveButtonAction() {
        realClass=1;
        leftPanel.remove(reviewClassPanel);
        leftPanel.repaint();
        leftPanel.add(fileFolderInput, BorderLayout.SOUTH);
        leftPanel.revalidate();
    }
    /**
     * unknownButtonAction() sets the realClass member field to
     * 2, removes the file chooser from the GUI interface if present,
     * and adds the review classification pane in its place.
     */
    public void unknownButtonAction() {
        realClass=2;
        leftPanel.remove(reviewClassPanel);
        leftPanel.repaint();
        leftPanel.add(fileFolderInput, BorderLayout.SOUTH);
        leftPanel.revalidate();
    }

    /**
     * searchButtonAction() collects the proper input, depending
     * if the searchMethod is id or substring, call the appropriate
     * search Thread, based upon the current state of the member field
     * just populated with the collected input, then calls the appropriate
     * fillReviewTable method. An error message is displayed in the information
     * panel if no matching review(s) is/are found.
     */
    public void searchButtonAction() {
        //Search by id.
        if(searchMethod.getSelectedItem().equals("id")) {
            try {
                id=Integer.parseInt(searchField.getText());

                //Calling this Thread will call the ReviewHandler
                //searchById method using the current state of the
                //id member field.
                searchIdReviewThread.run();

                //Error message, no review was found, thus the member field
                //MovieReview object, reviewInst, is null.
                if(reviewInst==null) {
                    textOutput.setText("NO REVIEW WITH MATCHING ID:"+id);
                }
                else {
                    //Call to construct, populate, and make visible
                    //the JTable with the found review.
                    fillReviewTableById();
                }
            }
            //Error message for invalid id input.
            catch(Exception Ex) {
                textOutput.setText("ERROR:INVALID ID:\n");
                textOutput.append(searchField.getText());
            }
        }
        //Search by substring.
        if(searchMethod.getSelectedItem().equals("substring")) {
            reviewText=searchField.getText();

            //Calling this method will construct, populate, and make visible
            //the JTable with the found reviews, based upon the current state
            //of reviewText. If the returned boolean flag is false not reviews
            //were found and an error message is displayed in the information
            //panel.
            if(fillReviewTableBySubstring()==false) {
                textOutput.setText("NO REVIEW WITH MATCHING SUBSTRING:\n");
                textOutput.append("\""+reviewText+"\"");
            }
        }
    }

    /**
     * searchDeleteButtonAction() collects the given id from the
     * searchField component, validates the input, searches for
     * a MovieReview with a matching id, or puts an error message
     * in the information panel.
     */
    public void searchDeleteButtonAction() {
        try {
            id=Integer.parseInt(searchField.getText());

            //Calling this Thread will call the ReviewHandler
            //searchById method using the current state of the
            //id member field.
            searchIdReviewThread.run();

            //Member field reviewInst is null thus no
            //matching MovieReview was found by the id search.
            //Error message is displayed in the information panel.
            if(reviewInst==null) {
                textOutput.setText("NO REVIEW WITH MATCHING ID:"+id);
            }
            //The input panel is removed from the master frame,
            //the proper fillReviewTable method is called, and
            //the information panel displays the id of the
            //matching review.
            else {
                inputPanel.removeAll();
                inputPanel.repaint();
                fillReviewTableById();
                deleteLabel.setText("Delete review: "+id);
                inputPanel.add(deleteLabel);
                inputPanel.add(confirmDeleteButton);
                inputPanel.add(cancelDeleteButton);
                inputPanel.revalidate();
            }
        }
        //Error message for invalid id input.
        catch(Exception Ex) {
            textOutput.setText("ERROR:INVALID ID:\n");
            textOutput.append(searchField.getText());
        }
    }

    /**
     * confirmDeleteButtonAction() Using the current state of the
     * id member field the corresponding MovieReview is removed from
     * the database, the input panel is removed from the master frame,
     * the JTable parent panel border is updated to reflect the new size
     * of the database, the information panel is updated with the ReviewHandler
     * simulated console output, and the proper fillReviewTable method is called.
     */
    public void confirmDeleteButtonAction() {
        reviewsManager.deleteReview(id);
        primeFrame.remove(inputPanel);
        updateReviewsBorder();
        textOutput.setText(reviewsManager.getTextContent());
        fillReviewTable();
    }

    /**
     * cancelDeleteButtonAction() calls the fillReviewTable method
     * to fill the JTable with all current MovieReviews in the database,
     * removes the input panel from the master frame, and redraws the frame.
     */
    public void cancelDeleteButtonAction() {
        fillReviewTable();
        primeFrame.remove(inputPanel);
        primeFrame.revalidate();
    }

    /**
     * fileFolderInputCancelAction() removes the file chooser
     * from its parent panel, and redraws the master frame.
     */
    public void fileFolderInputCancelAction() {
        leftPanel.remove(fileFolderInput);
        leftPanel.repaint();
        leftPanel.revalidate();
    }

    /**
     * ileFolderInputOpenAction()
     */
    public void fileFolderInputOpenAction() {
        //Removing the file chooser from the
        //master frame.
        leftPanel.remove(fileFolderInput);
        leftPanel.repaint();
        leftPanel.revalidate();

        //Getting the String path of the selected file/folder.
        File reviewFileFolder=fileFolderInput.getSelectedFile();
        reviewPath=reviewFileFolder.getAbsolutePath();

        //Loading in the new Movie Review Objects using the
        //proper Thread and updating the GUI using
        // the fillReviewTable method. Also updated the JTable
        //border to reflect the new number of reviews.
        loadReviewsThread.run();
        textOutput.setText(reviewsManager.getTextContent());
        fillReviewTable();
        updateReviewsBorder();
    }

    /**
     * exitAction() overrides the normal close action of the
     * master frame causing a confirm exit frame to appear.
     */
    public void exitAction() {
        //Setting the look of the confirm exit frame.
        exitFrame.setTitle("Confirm Exit");
        exitFrame.setSize(300, 100);
        exitFrame.add(exitFrameLabel, BorderLayout.NORTH);
        exitFrame.add(exitPanel, BorderLayout.CENTER);

        //Adding buttons and actions.
        exitPanel.add(exitConfirmButton);
        //Closing the program when the confirm button is hit.
        exitConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        exitPanel.add(exitCancelButton);
        //Reopening the master frame and closing the
        //confirm exit frame when the cancel button is hit.
        exitCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                primeFrame.setVisible(true);
                exitFrame.dispose();
            }
        });

        //Making the confirm exit frame visible.
        exitFrame.setLocationRelativeTo(primeFrame);
        exitFrame.setVisible(true);
    }
}