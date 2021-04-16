package com.cf4j.demo.rest;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cf4j.demo.entity.Coordinates;

import com.cf4j.demo.entity.Board;

import com.cf4j.demo.service.UserKnnComparisonService;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/cf4j")
public class UserKnnComparisonResource {

	@Autowired
	UserKnnComparisonService userKnnComparisonService;

	@PostMapping("/userKnnComparison")
	@ResponseBody
	public ResponseEntity<List<Coordinates>> listMatrixFactorizationComparison(@RequestBody List<Board> series)
			throws IOException {
		List<Coordinates> result = userKnnComparisonService.listUserKnnComparison(series);
		return new ResponseEntity<List<Coordinates>>(result, HttpStatus.OK);

	}
}
