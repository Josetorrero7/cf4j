package com.cf4j.demo.utils;

import java.util.List;

import com.cf4j.demo.entity.KeyValue;

public class utils {

	/**
	 * Método que nos determina si una cadena de texto es un numero o no
	 * 
	 * @return boolean true en caso de tratarse de un numero y false en caso
	 *         contrario
	 */
	public static boolean isNumeric(String cadena) {
		try {
			Float.parseFloat(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	/**
	 * Método que nos busca apartir de una lista de KeyValue que recibe por
	 * parametro, la clave que le indicamos con su valor
	 * 
	 * @return KeyValue del parametro que buscamos 
	 */
	public static KeyValue getKeyValue(String key, List<KeyValue> params) {
		KeyValue result = new KeyValue();
		for (int i = 0; i < params.size(); i++) {
			if (params.get(i).getKey().equals(key)) {
				if (params.get(i).getValue() != null && params.get(i).getValue() != "") {
					result = params.get(i);
				}
				break;
			}}
		return result;
	}
}
