package common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Training {

	private ArrayList<String> vocabulary;
	private ArrayList<String> trainingData;
	private ArrayList<String> stoplist;
	private ArrayList<String> labels;
	private ArrayList<int[]> featureList;
	private ArrayList<String> bagOfWords;
	private ArrayList<int[]> featuredWiseSentences;
	private ArrayList<int[]> featuredFutureSentences;
	private int totalNumberFutureWords;
	private int totalNumberWiseWords;

	public int getTotalNumberFutureWords() {
		return totalNumberFutureWords;
	}

	public void setTotalNumberFutureWords(int totalNumberFutureWords) {
		this.totalNumberFutureWords = totalNumberFutureWords;
	}

	public int getTotalNumberWiseWords() {
		return totalNumberWiseWords;
	}

	public void setTotalNumberWiseWords(int totalNumberWiseWords) {
		this.totalNumberWiseWords = totalNumberWiseWords;
	}

	public ArrayList<String> getBagOfWords() {
		return bagOfWords;
	}

	public ArrayList<int[]> getFeaturedWiseSentences() {
		return featuredWiseSentences;
	}

	public void setFeaturedWiseSentences(ArrayList<int[]> featuredWiseSentences) {
		this.featuredWiseSentences = featuredWiseSentences;
	}

	public ArrayList<int[]> getFeaturedFutureSentences() {
		return featuredFutureSentences;
	}

	public void setFeaturedFutureSentences(ArrayList<int[]> featuredFutureSentences) {
		this.featuredFutureSentences = featuredFutureSentences;
	}

	public void setBagOfWords(ArrayList<String> bagOfWords) {
		this.bagOfWords = bagOfWords;
	}

	public ArrayList<String> getVocabulary() {
		return vocabulary;
	}

	public void setVocabulary(ArrayList<String> vocabulary) {
		this.vocabulary = vocabulary;
	}

	public ArrayList<String> getTrainingData() {
		return trainingData;
	}

	public void setTrainingData(ArrayList<String> trainingData) {
		this.trainingData = trainingData;
	}

	public ArrayList<String> getStoplist() {
		return this.stoplist;
	}

	public void setStoplist(ArrayList<String> stoplist) {
		this.stoplist = stoplist;
	}

	public ArrayList<String> getLabels() {
		return this.labels;
	}

	public void setLabels(ArrayList<String> labels) {
		this.labels = labels;
	}

	public ArrayList<int[]> getFeatureList() {
		return this.featureList;
	}

	public void setFeatureList(ArrayList<int[]> featureList) {
		this.featureList = featureList;
	}

	public Training(ArrayList<String> vocabulary, ArrayList<String> trainingData) {

		this.vocabulary = new ArrayList<String>();
		this.trainingData = new ArrayList<String>();
		this.stoplist = new ArrayList<String>();
		this.labels = new ArrayList<String>();
		this.featureList = new ArrayList<>();
		this.bagOfWords = new ArrayList<String>();
		this.setFeaturedFutureSentences(new ArrayList<int[]>());
		this.setFeaturedWiseSentences(new ArrayList<int[]>());
	}

	public Training() {
		this.vocabulary = new ArrayList<String>();
		this.trainingData = new ArrayList<String>();
		this.stoplist = new ArrayList<String>();
		this.labels = new ArrayList<String>();
		this.featureList = new ArrayList<>();
		this.bagOfWords = new ArrayList<String>();
		this.setFeaturedFutureSentences(new ArrayList<int[]>());
		this.setFeaturedWiseSentences(new ArrayList<int[]>());
	}

	public List<String> removeStopListsAndSort(ArrayList<String> vocabulary, ArrayList<String> StopList) {
		List<String> deduped = vocabulary.stream().distinct().collect(Collectors.toList());
		deduped.removeAll(StopList);
		deduped.sort(String.CASE_INSENSITIVE_ORDER);
		return deduped;
	}

	public static int[] setOneFeature(ArrayList<String> vocabulary, String sentence) {

		int[] feature = new int[vocabulary.size() + 1];

		for (int j = 0; j < vocabulary.size(); j++) {
			if (sentence.contains(vocabulary.get(j))) {
				feature[j] = 1;
			} else {
				feature[j] = 0;
			}
		}

		return feature;
	}

	public static ArrayList<int[]> setAllFeatures(ArrayList<String> vocabulary, ArrayList<String> trainData) {
		ArrayList<int[]> features = new ArrayList<int[]>();
		for (int j = 0; j < trainData.size(); j++) {
			features.add(Training.setOneFeature(vocabulary, trainData.get(j)));

		}
		return features;

	}

	public static ArrayList<int[]> addLabels(ArrayList<String> vocabulary, ArrayList<int[]> features,
			ArrayList<String> labels) {
		for (int i = 0; i < features.size(); i++) {
			int[] temp = features.get(i);
			temp[temp.length - 1] = Integer.parseInt(labels.get(i));
		}
		return features;
	}

	public int NumberOfVocabularyWordsInSet(ArrayList<String> vocabulary, ArrayList<String> bagOfWords) {
		int count = 0;
		for (int i = 0; i < bagOfWords.size(); i++) {
			if (vocabulary.contains(bagOfWords.get(i)))
				count++;
		}
		return count;
	}

	public ArrayList<int[]> featuredFutureStrings(ArrayList<int[]> features) {
		ArrayList<int[]> futureStrings = new ArrayList<int[]>();
		for (int i = 0; i < features.size(); i++) {
			int[] temp = features.get(i);
			if (temp[temp.length - 1] == 1)
				futureStrings.add(features.get(i));

		}
		return futureStrings;
	}

	public ArrayList<int[]> featuredWiseStrings(ArrayList<int[]> features) {
		ArrayList<int[]> wiseStrings = new ArrayList<int[]>();
		for (int i = 0; i < features.size(); i++) {
			int[] temp = features.get(i);
			if (temp[temp.length - 1] == 0)
				wiseStrings.add(features.get(i));

		}
		return wiseStrings;
	}

	public int numberOfVocabularyWordsinList(ArrayList<int[]> features) {
		int count = 0;
		for (int i = 0; i < features.size(); i++) {
			int[] temp = features.get(i);
			for (int j = 0; j < temp.length - 1; j++) {
				if (temp[j] == 1)
					count++;
			}

		}
		return count;

	}

	public int frequencyOfWordinFuture(String word) {
		int count = 0;
		if (this.vocabulary.contains(word)) {
			int index = vocabulary.indexOf(word);
			for (int i = 0; i < this.featuredFutureSentences.size(); i++) {
				int[] temp = this.featuredFutureSentences.get(i);
				if (temp[index] == 1)
					count++;
			}

			return count;
		}
		return 0;
	}
	public int frequencyOfWordinWise(String word) {
		int count = 0;
		if (this.vocabulary.contains(word)) {
			int index = vocabulary.indexOf(word);
			for (int i = 0; i < this.featuredWiseSentences.size(); i++) {
				int[] temp = this.featuredWiseSentences.get(i);
				if (temp[index] == 1)
					count++;
			}

			return count;
		}
		return 0;
	}
	
	public double ProbabilityFuture(){
		double futureSize = this.getFeaturedFutureSentences().size();
		double totalSize = this.getFeatureList().size();
		return Math.log(futureSize/totalSize);
		}
	public double ProbabilityWise(){
		double wiseSize = this.getFeaturedWiseSentences().size();
		double totalSize = this.getFeatureList().size();
		return Math.log(wiseSize/totalSize);
	}
	
	public double ProbabilityWordFuture(double frequenceofWord){
		return Math.log((frequenceofWord + 1)/(this.totalNumberFutureWords + this.vocabulary.size()));
	}
	
	public double ProbabilityWordWise(double frequenceofWord){
		return Math.log((frequenceofWord +1)/(this.totalNumberWiseWords+ this.vocabulary.size()));
	}
	

	

	public static void printArrayList(ArrayList<String> arr) {
		for (int i = 0; i < arr.size(); i++) {
			System.out.println(arr.get(i));
		}
	}
	

	public static void printArrayListInts(ArrayList<int[]> arr) {
		for (int i = 0; i < arr.size(); i++) {
			int[] temp = arr.get(i);
			for (int j = 0; j < temp.length; j++) {
				System.out.print(temp[j]);
			}
			System.out.println();
		}
	}

}