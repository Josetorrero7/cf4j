package com.cf4j.demo.entity;

import java.util.List;

public class Quality {
	private String name;
	private int numberOfRecommendations;
	private double relevantThreshold;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumberOfRecommendations() {
		return numberOfRecommendations;
	}
	public void setNumberOfRecommendations(int numberOfRecommendations) {
		this.numberOfRecommendations = numberOfRecommendations;
	}
	public double getRelevantThreshold() {
		return relevantThreshold;
	}
	public void setRelevantThreshold(double relevantThreshold) {
		this.relevantThreshold = relevantThreshold;
	}
	
}
