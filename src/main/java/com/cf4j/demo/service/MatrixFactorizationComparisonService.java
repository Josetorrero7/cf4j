package com.cf4j.demo.service;

import java.io.IOException;
import java.util.List;

import com.cf4j.demo.entity.Serie;
import com.cf4j.demo.entity.Coordinates;

public interface MatrixFactorizationComparisonService {
	
	List<Coordinates> listMatrixFactorizationComparison(List<Serie> series)throws IOException;

}
