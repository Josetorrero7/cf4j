package com.cf4j.demo.service;

import java.io.IOException;
import java.util.List;

import com.cf4j.demo.entity.Board;
import com.cf4j.demo.entity.Coordinates;

public interface MatrixFactorizationComparisonService {
	
	List<Coordinates> listMatrixFactorizationComparison(Board board)throws IOException;

}
