package com.cf4j.demo.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cf4j.demo.entity.Coordinates;
import com.cf4j.demo.entity.RecomenderResponse;
import com.cf4j.demo.entity.Board;
import com.cf4j.demo.service.MatrixFactorizationComparisonService;


import java.io.IOException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/cf4j")
public class MatrixFactorizationComparisonResource {

	@Autowired
	MatrixFactorizationComparisonService matrixFactorizationComparisonService;

	@PostMapping("/matrixFactorizationComparison")
	@ResponseBody
	public ResponseEntity<List<RecomenderResponse>> listMatrixFactorizationComparison(@RequestBody Board board)
			throws IOException {
		List<RecomenderResponse> result = matrixFactorizationComparisonService.listMatrixFactorizationComparison(board);
		return new ResponseEntity<List<RecomenderResponse>>(result, HttpStatus.OK);

	}

}
