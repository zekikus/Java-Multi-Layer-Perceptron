package com.zekikus.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileOperations {
	
	private File file;
	private Scanner reader;
	private double[][] readDatas;
	private double[] outputs;
	private int dataRowCount = 0;
	private double maxVal;
	private double minVal;
	private int dataRows;
	private int dataCols;
	
	public FileOperations(String filePath) throws FileNotFoundException{
		file = new  File(filePath);
		reader = new Scanner(file);
		getDataInfo();
		readData();
		normalizeOperation();
	}
	
	public void getDataInfo() {
		Scanner s = new Scanner(System.in);
		System.out.print("The number of rows in your data:");
		dataRows = s.nextInt();
		System.out.print("The number of columns in your data:");
		dataCols = s.nextInt();
		readDatas = new double[dataRows][dataCols];
		outputs = new double[dataRows];
	}
	
	public void readData(){
		while(reader.hasNext()){
			
			String[] rowData = reader.next().split(",");
			outputs[dataRowCount] = Double.parseDouble(rowData[rowData.length-1]);
			
			if(dataRowCount == 0){
				maxVal = Double.parseDouble(rowData[0]);
				minVal = Double.parseDouble(rowData[0]);
			}
			
			for (int i = 0; i < rowData.length - 1; i++) {
				double data = Double.parseDouble(rowData[i]);
				
				if(data > maxVal) maxVal = data;
				if(data < minVal) minVal = data;
				
				readDatas[dataRowCount][i] = data;
			}
			
			dataRowCount++;
		}
		reader.close();
	}
	
	public void normalizeOperation(){
		for (int i = 0; i < readDatas.length; i++) {
			for (int j = 0; j < readDatas[i].length; j++) {
				readDatas[i][j] = normalizeData(readDatas[i][j]);
			}
		}
	}
	
	public Double normalizeData(Double data){
		return (data - minVal) / (maxVal - minVal);
	}
	
	public double[] getOutputs() {
		return outputs;
	}
	
	public double[][] getReadDatas() {
		return readDatas;
	}
	
}
