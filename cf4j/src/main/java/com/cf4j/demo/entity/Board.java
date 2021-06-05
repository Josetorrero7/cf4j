package com.cf4j.demo.entity;

import java.util.List;

public class Board {
	private String dataset;
	private List<Algorithm> algorithms;
	private RangeDynamic rangeDynamic;
	private Quality qualityMeasure;
	
	public String getDataset() {
		return dataset;
	}
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public List<Algorithm> getAlgorithms() {
		return algorithms;
	}
	public void setAlgorithms(List<Algorithm> algorithms) {
		this.algorithms = algorithms;
	}
	public RangeDynamic getRangeDynamic() {
		return rangeDynamic;
	}
	public void setRangeDynamic(RangeDynamic rangeDynamic) {
		this.rangeDynamic = rangeDynamic;
	}
	public Quality getQualityMeasure() {
		return qualityMeasure;
	}
	public void setQualityMeasure(Quality qualityMeasure) {
		this.qualityMeasure = qualityMeasure;
	}
	

}
