package common;

import java.util.ArrayList;
import common.Training;
public class Testing {
	
	private ArrayList<String> testData;
	private ArrayList<String> labelData;
	private static ArrayList<int[]> featureList;

	
	public Testing (){
		this.testData = new ArrayList<String>();
		this.labelData = new ArrayList<String>();
		this.setFeatureList(new ArrayList<int[]>());
	}


	public ArrayList<String> getTestData() {
		return testData;
	}


	public void setTestData(ArrayList<String> testData) {
		this.testData = testData;
	}


	public ArrayList<String> getLabelData() {
		return labelData;
	}


	public void setLabelData(ArrayList<String> labelData) {
		this.labelData = labelData;
	}
	
	public ArrayList<int[]> getFeatureList() {
		return featureList;
	}


	public void setFeatureList(ArrayList<int[]> featureList) {
		this.featureList = featureList;
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
	
	public static ArrayList<String> oneSentenceTest(int position){
		ArrayList<String> positions = new ArrayList<String>();
		int[]test = featureList.get(position);
		for (int i = 0 ; i<test.length; i++){
			if (test[i] == 1){
				positions.add(Integer.toString(i));
			}
		}
		return positions;
	}
	
		
	}

	

