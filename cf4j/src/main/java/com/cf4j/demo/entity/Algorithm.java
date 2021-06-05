package com.cf4j.demo.entity;

import java.util.List;

public class Algorithm {
	private String name;
	private List<KeyValue> params;
	

	public String getName() {
		return name;
	}


	public Algorithm(String name, List<KeyValue> params) {
		super();
		this.name = name;
		this.params = params;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<KeyValue> getParams() {
		return params;
	}


	public void setParams(List<KeyValue> params) {
		this.params = params;
	}


	public Algorithm() {
		super();
	}



}
