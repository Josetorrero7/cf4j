package com.cf4j.demo.entity;

import java.util.List;

public class RecomenderResponse {
	private Algorithm algorithm;
	private List<Double> results;
	private List<String> range;

	public RecomenderResponse() {
		super();
	}


	public RecomenderResponse(Algorithm algorithm, List<Double> results, List<String> range) {
		super();
		this.algorithm = algorithm;
		this.results = results;
		this.range = range;
	}


	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public List<Double> getResults() {
		return results;
	}

	public void setResults(List<Double> results) {
		this.results = results;
	}

	public List<String> getRange() {
		return range;
	}

	public void setRange(List<String> range) {
		this.range = range;
	}






}
