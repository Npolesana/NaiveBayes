package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		Training training = new Training() {};
		Testing test = new Testing();

		/*
		 * 
		 * Scanners to read files
		 * 
		 */
		Scanner s1 = new Scanner(new BufferedReader(new FileReader("traindata.txt")));

		while (s1.hasNext()) {
			training.getVocabulary().add(s1.next());

		}

		s1.close();

		Scanner s2 = new Scanner(new BufferedReader(new FileReader("traindata.txt")));

		s2.useDelimiter(System.getProperty("line.separator"));

		while (s2.hasNextLine()) {
			training.getTrainingData().add(s2.nextLine());
		}
		s2.close();

		Scanner s3 = new Scanner(new BufferedReader(new FileReader("stoplist.txt")));

		while (s3.hasNext()) {
			training.getStoplist().add(s3.next());

		}

		s3.close();

		Scanner s4 = new Scanner(new BufferedReader(new FileReader("trainlabels.txt")));

		while (s4.hasNext()) {
			training.getLabels().add(s4.next());

		}

		Scanner s7 = new Scanner(new BufferedReader(new FileReader("testlabels.txt")));

		while (s7.hasNext()) {
			test.getLabelData().add(s7.next());

		}
		s4.close();

		Scanner s5 = new Scanner(new BufferedReader(new FileReader("traindata.txt")));

		while (s5.hasNext()) {
			training.getBagOfWords().add(s5.next());

		}

		s5.close();

		/*
		 * Remove stoplists, duplicates and sort vocabulary a-z
		 */
		training.setVocabulary(
				(ArrayList<String>) training.removeStopListsAndSort(training.getVocabulary(), training.getStoplist()));
		/*
		 * Generates featured lists and add labels
		 * 
		 */
		training.setFeatureList(training.setAllFeatures(training.getVocabulary(), training.getTrainingData()));
		training.setFeatureList(
				training.addLabels(training.getVocabulary(), training.getFeatureList(), training.getLabels()));
		training.setFeaturedFutureSentences((training.featuredFutureStrings(training.getFeatureList())));
		training.setFeaturedWiseSentences((training.featuredWiseStrings(training.getFeatureList())));

		/*
		 * Count number of vocabulary words for every set
		 * 
		 */

		training.setTotalNumberFutureWords(
				training.numberOfVocabularyWordsinList(training.getFeaturedFutureSentences()));
		training.setTotalNumberWiseWords(training.numberOfVocabularyWordsinList(training.getFeaturedWiseSentences()));

		/*
		 * Testing frequency of a specific word in sets
		 * 
		 */

		System.out.println(training.ProbabilityWise());
		System.out.println(training.ProbabilityFuture());
		System.out.println(training.ProbabilityWordWise(234));

		/*
		 * Print training file
		 * 
		 */

		File outFile = new File("output.txt");
		FileWriter fWriter = new FileWriter(outFile);
		PrintWriter pWriter = new PrintWriter(fWriter);
		int[] temp = new int[training.getVocabulary().size()];

		for (int j = 0; j < training.getVocabulary().size(); j++) {
			pWriter.print(training.getVocabulary().get(j) + ", ");
		}
		pWriter.println();
		for (int i = 0; i < training.getFeatureList().size(); i++) {
			temp = training.getFeatureList().get(i);
			for (int j = 0; j < temp.length; j++) {
				pWriter.print(temp[j]);
			}
			pWriter.println();

		}
		pWriter.close();

		/*
		 * Read test file
		 * 
		 */

		Scanner s6 = new Scanner(new BufferedReader(new FileReader("testdata.txt")));

		s6.useDelimiter(System.getProperty("line.separator"));

		while (s6.hasNextLine()) {
			test.getTestData().add(s6.nextLine());

		}

		s6.close();

		/*
		 * 
		 * Testing data: converting set into feature vector
		 * 
		 */

		test.setFeatureList(test.setAllFeatures(training.getVocabulary(), test.getTestData()));

		/*
		 * 
		 * Calculating probability using the feature vector and training data.
		 * 
		 * 
		 */		
		
				Double WiseScore = (double) 0;
				Double FutureScore =(double) 0;
		        ArrayList<String> finaResults = new ArrayList<String>();
		for (int i = 0; i < test.getFeatureList().size(); i++) {
			int[] t = test.getFeatureList().get(i);

			for (int j = 0; j < t.length; j++) {
				
				if (t[j] == 1){
					 FutureScore = training.ProbabilityFuture();
			         WiseScore = training.ProbabilityWise();
					String word = training.getVocabulary().get(j);
					double freq = training.frequencyOfWordinFuture(word);
					double freq2 = training.frequencyOfWordinWise(word);
					 FutureScore =+ training.ProbabilityWordFuture(freq);
					 WiseScore =+ training.ProbabilityWordWise(freq2);				
				}}
				if (FutureScore > WiseScore) 
				{
                  finaResults.add("1");
                  
				}
				else
				{
                   finaResults.add("0");}

			}

		
		
		/*
		 * Sanity check. Comparing labels with actual results
		 * 
		 * 
		 */
	double counterz =0;
	for(int i = 0; i < test.getLabelData().size(); i++) {
    if(finaResults.get((i)).equals(test.getLabelData().get(i))) {
    	counterz++;
      }

  }
	
	/*
	 * Print result
	 * 
	 */

	File outFile1 = new File("result.txt");
	FileWriter fWriter1 = new FileWriter(outFile1);
	PrintWriter pWriter1 = new PrintWriter(fWriter1);

	pWriter1.print(" Equal elements are " + counterz + ". Accuracy is " + counterz/test.getFeatureList().size()*100);
	pWriter1.println();
	pWriter1.print("Accuracy needs to be improved. I believe something went wrong when comparing vocabulary and datasets");

	
	pWriter1.close();
}}