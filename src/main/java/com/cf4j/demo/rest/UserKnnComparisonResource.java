package com.cf4j.demo.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cf4j.demo.entity.Coordinates;
import com.cf4j.demo.entity.KeyValue;
import com.cf4j.demo.entity.Serie;
import com.cf4j.demo.service.MatrixFactorizationComparisonService;
import com.cf4j.demo.service.UserKnnComparisonService;

import es.upm.etsisi.cf4j.data.BenchmarkDataModels;
import es.upm.etsisi.cf4j.data.DataModel;
import es.upm.etsisi.cf4j.qualityMeasure.QualityMeasure;
import es.upm.etsisi.cf4j.qualityMeasure.prediction.Coverage;
import es.upm.etsisi.cf4j.qualityMeasure.prediction.MAE;
import es.upm.etsisi.cf4j.qualityMeasure.recommendation.Precision;
import es.upm.etsisi.cf4j.qualityMeasure.recommendation.Recall;
import es.upm.etsisi.cf4j.recommender.Recommender;
import es.upm.etsisi.cf4j.recommender.knn.UserKNN;
import es.upm.etsisi.cf4j.recommender.knn.userSimilarityMetric.UserSimilarityMetric;
import es.upm.etsisi.cf4j.util.Range;
import es.upm.etsisi.cf4j.util.plot.LinePlot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import es.upm.etsisi.cf4j.recommender.knn.userSimilarityMetric.*;

@RestController
@RequestMapping("/cf4j")
public class UserKnnComparisonResource {

	@Autowired
	UserKnnComparisonService userKnnComparisonService;

	@PostMapping("/userKnnComparison")
	@ResponseBody
	public ResponseEntity<List<Coordinates>> listMatrixFactorizationComparison(@RequestBody List<Serie> series)
			throws IOException {
		List<Coordinates> result = userKnnComparisonService.listUserKnnComparison(series);
		return new ResponseEntity<List<Coordinates>>(result, HttpStatus.OK);

	}
}
