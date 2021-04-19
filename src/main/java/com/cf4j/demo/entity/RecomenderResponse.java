package com.cf4j.demo.entity;

import java.util.List;

public class RecomenderResponse {
	private String algorithm;
	private List<Integer> param;
	private List<Double> results;

	public RecomenderResponse() {
		super();
	}
	
	public RecomenderResponse(String algorithm, List<Integer> param, List<Double> results) {
		super();
		this.algorithm = algorithm;
		this.param = param;
		this.results = results;
	}

	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	public List<Double> getResults() {
		return results;
	}
	public void setResults(List<Double> results) {
		this.results = results;
	}
	public List<Integer> getParam() {
		return param;
	}
	public void setParam(List<Integer> param) {
		this.param = param;
	}



}
