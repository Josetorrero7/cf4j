package com.cf4j.demo.utils;

import java.util.List;

import com.cf4j.demo.entity.KeyValue;

public class utils {

	public static boolean isNumeric(String cadena) {
		try {
			Float.parseFloat(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	public static KeyValue getKeyValue(String key, List<KeyValue> params) {
		KeyValue result = new KeyValue();
		for (int i = 0; i < params.size(); i++) {
			if (params.get(i).getKey().equals(key)) {
				if (params.get(i).getValue() != null && params.get(i).getValue() != "") {
					result = params.get(i);
				}
				break;
			}
		}
		return result;
	}

}
