package com.zekikus.test;

import java.io.FileNotFoundException;

public class Test {
	public static void main(String[] args) throws FileNotFoundException {
		// Data File
		FileOperations operation = new FileOperations("iris_copy.data");
		double[][] inputs = operation.getReadDatas();
		double[] outputs = operation.getOutputs();
		
		MLP mlp = new MLP(inputs, outputs);
		mlp.start();
		
	}
}
