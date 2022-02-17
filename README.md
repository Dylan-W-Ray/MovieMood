#MovieMood

#Command-line Compilation Instructions: 
*Ensure you are in the top level directory 'MovieMood'
*Linux:
'''
Compile: '$ javac -d classes -cp "./src/movieReviewClassification/*.java:./lib/sentiment.jar:" ./src/movieReviewClassification/*.java'
'''
'''
Run:'$ java -cp "./classes/:./lib/sentiment.jar:" movieReviewClassification.ReviewClassifierGUI'
'''
*Windows:
'''
Compile: '> javac -d classes -cp "./src/movieReviewClassification/*.java;./lib/sentiment.jar;" ./src/movieReviewClassification/*.java'  
'''
'''
Run:'> java -cp "./classes/;./lib/sentiment.jar;" movieReviewClassification.ReviewClassifierGUI'
'''



