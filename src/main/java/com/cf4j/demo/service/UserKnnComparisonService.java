package com.cf4j.demo.service;

import java.io.IOException;
import java.util.List;

import com.cf4j.demo.entity.Serie;
import com.cf4j.demo.entity.Coordinates;

public interface UserKnnComparisonService {
	
	List<Coordinates> listUserKnnComparison(List<Serie> series)throws IOException;

}
