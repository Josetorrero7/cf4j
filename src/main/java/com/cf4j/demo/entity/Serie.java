package com.cf4j.demo.entity;

import java.util.List;

public class Serie {
	private String name;
	private List<KeyValue> param;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<KeyValue> getParam() {
		return param;
	}
	public void setParam(List<KeyValue> param) {
		this.param = param;
	}

}
