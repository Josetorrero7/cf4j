package com.cf4j.demo.service;

import java.io.IOException;
import java.util.List;

import com.cf4j.demo.entity.Board;
import com.cf4j.demo.entity.Coordinates;
import com.cf4j.demo.entity.RecomenderResponse;

import es.upm.etsisi.cf4j.util.plot.LinePlot;

public interface MatrixFactorizationComparisonService {
	
	List<RecomenderResponse> listMatrixFactorizationComparison(Board board)throws IOException;

}
