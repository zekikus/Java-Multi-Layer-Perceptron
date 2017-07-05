package com.zekikus.test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MLP {
	
	private final int INPUT_COUNT = 4;
	private final int HIDDEN_COUNT = 4;
	private final int OUTPUT_COUNT = 3;
	private final double LEARNING_RATE = 0.01;
	
	private double[][] weightIH; // Input to Hidden Layer Weights
	private double[][] weightHO; // Hidden to Output Layer Weights;
	
	private double error,trainingError,testError,trainSetSize,testSetSize;
	private double totalError,desiredOutput;
	
	private double[][] inputs;
	private double[] hidden,outputs,desiredOutputs;
	private List<Double> outputNodeError;
	
	public MLP(double[][] inputs,double[] desiredOutputs) {
		this.inputs = inputs;
		this.desiredOutputs = desiredOutputs;
		this.hidden = new double[HIDDEN_COUNT];
		this.outputs = new double[OUTPUT_COUNT];
		weightIH = new double[HIDDEN_COUNT][INPUT_COUNT];
		weightHO = new double[HIDDEN_COUNT][OUTPUT_COUNT];
		outputNodeError = new ArrayList<Double>();
		initialisation();
	}
	
	public void start(){
		
		System.out.println("Starting..........\n");
		ArrayList<double[]> temp = new ArrayList<double[]>(); 
		for (int j = 0; j < 500; j++) {
			
			desiredOutput = desiredOutputs[0];
			double arraySize = inputs.length - 1;
			trainingError = 0.0;
			testError = 0.0;
			trainSetSize = 0.0;
			testSetSize = 0.0;
			
			for (int i = 0; i < inputs.length; i++) {
				if(desiredOutputs[i] == desiredOutput && (i < arraySize)){
					temp.add(inputs[i]);
				}else{
					Collections.shuffle(temp);
					training(temp);
					test(temp);
					temp.clear();
					desiredOutput = desiredOutputs[i];
				}
			}
			double mseTraining = Math.sqrt(trainingError / trainSetSize);
			double mseTest = Math.sqrt(testError / testSetSize); 
			System.out.println(" Iteration:" + (j+1) + ", Training Doðruluk Oraný: " + Math.abs((1 - mseTraining)) + ", Test Doðruluk Oraný: " + Math.abs((1 - mseTest)));
		}
	
	}
	
	public void training(ArrayList<double[]> inputs){
		
		double size = (inputs.size() * 80) / 100;
		
		for (int i = 0; i < size; i++) {
			trainingError += feedForward(inputs.get(i));
			backPropagation(inputs.get(i));
			outputNodeError.clear();
			trainSetSize++;
		}
	}
	
	//Aðýrlýklarýn Rastgele Belirlenmesi.
	public void initialisation(){
		
		for (int i = 0; i < INPUT_COUNT; i++) {
			for (int j = 0; j < HIDDEN_COUNT; j++) {
				weightIH[i][j] = Math.random() - 0.5;
			}
		}
		
		for (int j = 0; j < HIDDEN_COUNT; j++) {
			for (int k = 0; k < OUTPUT_COUNT; k++) {
				weightHO[j][k] = Math.random() - 0.5;
			}
		}
	}
	
	// Ýleri Besleme: Hidden ve output katmanýndaki düðümlerin deðerlerinin belirlenmesi.
	public Double feedForward(double[] inputs){
		
		totalError = 0.0;
		
		for (int j = 0; j < HIDDEN_COUNT; j++) {
			hidden[j] = 0.0;
			for (int i = 0; i < INPUT_COUNT; i++) {
				hidden[j] += inputs[i] * weightIH[i][j];
			}
			hidden[j] = sigmoidFunction(hidden[j]);
		}
		
		for (int k = 0; k < OUTPUT_COUNT; k++) {
			outputs[k] = 0.0;
			for (int j = 0; j < HIDDEN_COUNT; j++) {
				outputs[k] += hidden[j] * weightHO[j][k];
			}
			outputs[k] = sigmoidFunction(outputs[k]);
		}
		
		for (int k = 0; k < outputs.length; k++) {
			
			if(k == desiredOutput - 1)
				error = outputs[k] * ((1 - outputs[k]) * (1 - outputs[k]));
			else
				error = (1 - outputs[k]) * ((outputs[k]  * outputs[k]));
			
			outputNodeError.add(error);
			totalError += error;				
		}
		
		return totalError;
	}
	
	// Outputlar elde edildikten sonra oluþan hataya göre aðýrlýklarýn güncellenmesi.
	public void backPropagation(double[] inputs){
		
		
		double[] errorGradient = new double[HIDDEN_COUNT];
		
		for (int j = 0; j < hidden.length; j++) {
			double hiddenGradient = 0.0;
			for (int k = 0; k < outputs.length; k++) {
				weightHO[j][k] += (LEARNING_RATE * hidden[j] * outputNodeError.get(k));
				hiddenGradient += (outputNodeError.get(k) * weightHO[j][k]);
			}
			errorGradient[j] = hiddenGradient;
		}
		
		for (int i = 0; i < inputs.length; i++) {
			for (int j = 0; j < hidden.length; j++) {
				weightIH[i][j] += (LEARNING_RATE * inputs[i] * hidden[j] * (1-hidden[j]) * errorGradient[j]);
			}
		}
	}
	
	// Datanýn %80'i eðitim, %20'si test için kullanýldý.
	public void test(ArrayList<double[]> inputs){
		
		int startIndex = (inputs.size() * 80) / 100;
		int finishIndex = inputs.size();
		
		for (int i = startIndex; i < finishIndex; i++) {
			testError += feedForward(inputs.get(i));
			outputNodeError.clear();
			testSetSize++;
		}
		
	}
	
	public double sigmoidFunction(double actualOutput){
		return 1.0/(1.0+Math.exp(-actualOutput));
	}
	
	public double errorFormat(double error){
		DecimalFormat myFormat = new DecimalFormat("#.##");
		return Double.parseDouble(myFormat.format(error).replace(",", "."));
	}
	
	
}
