package com.cf4j.demo.entity;

import java.util.List;

public class Board {
	private String dataset;
	private int [] param;
	private List<String> algorithms;
	
	public String getDataset() {
		return dataset;
	}
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public int[] getParam() {
		return param;
	}
	public void setParam(int[] param) {
		this.param = param;
	}
	public List<String> getAlgorithms() {
		return algorithms;
	}
	public void setAlgorithms(List<String> algorithms) {
		this.algorithms = algorithms;
	}


}
