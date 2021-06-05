package com.cf4j.demo.entity;

import java.util.List;

public class RangeDynamic {
	private String name;
	private List<String> range;
	

	public String getName() {
		return name;
	}


	public RangeDynamic(String name, List<String> range) {
		super();
		this.name = name;
		this.range = range;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<String> getRange() {
		return range;
	}


	public void setRange(List<String> range) {
		this.range = range;
	}


	public RangeDynamic() {
		super();
	}



}
