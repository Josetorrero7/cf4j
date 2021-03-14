package com.cf4j.demo.utils;

public class utils {

	public static boolean isNumeric(String cadena) {
		try {
			Float.parseFloat(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

}
