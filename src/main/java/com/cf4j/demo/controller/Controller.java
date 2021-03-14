package com.cf4j.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cf4j.demo.entity.coordinates;
import com.cf4j.demo.utils.utils;

import es.upm.etsisi.cf4j.data.BenchmarkDataModels;
import es.upm.etsisi.cf4j.util.plot.LinePlot;
import es.upm.etsisi.cf4j.qualityMeasure.QualityMeasure;
import es.upm.etsisi.cf4j.qualityMeasure.prediction.MSE;
import es.upm.etsisi.cf4j.recommender.matrixFactorization.PMF;
import es.upm.etsisi.cf4j.data.DataModel;

import es.upm.etsisi.cf4j.data.BenchmarkDataModels;
import es.upm.etsisi.cf4j.util.plot.LinePlot;
import es.upm.etsisi.cf4j.qualityMeasure.QualityMeasure;
import es.upm.etsisi.cf4j.recommender.Recommender;
import es.upm.etsisi.cf4j.util.Range;
import es.upm.etsisi.cf4j.data.DataModel;
import es.upm.etsisi.cf4j.qualityMeasure.prediction.RMSE;
import es.upm.etsisi.cf4j.recommender.matrixFactorization.*;

import java.io.IOException;


@RestController
@RequestMapping("/cf4j")
public class Controller {
	public utils util = new utils();
	
	  private static final int[] NUM_FACTORS = Range.ofIntegers(5, 5, 5);

	  private static final int NUM_ITERS = 50;

	  private static final long RANDOM_SEED = 43;

	// @RequestMapping(value="/matrixFactorizationComparison",
	// method=RequestMethod.GET)
	@GetMapping("/matrixFactorizationComparison")
	public ArrayList<coordinates> matrixFactorizationComparison() throws IOException {

//		DataModel datamodel = BenchmarkDataModels.MovieLens100K();
//
//		double[] regValues = { 0.000, 0.025, 0.05, 0.075, 0.100, 0.125, 0.150, 0.175, 0.200, 0.225, 0.250, 0.300 };
//		LinePlot plot = new LinePlot(regValues, "regularization", "MSE");
//
//		plot.addSeries("PMF");
//
//		for (double reg : regValues) {
//			PMF pmf = new PMF(datamodel, 6, 50, reg, 0.01, 43);
//			pmf.fit();
//
//			QualityMeasure mse = new MSE(pmf);
//			double mseScore = mse.getScore();
//
//			plot.setValue("PMF", reg, mseScore);
//		}
		



		    // DataModel load
		    DataModel datamodel = BenchmarkDataModels.MovieLens100K();

		    // To store results
		    LinePlot plot = new LinePlot(NUM_FACTORS, "Number of latent factors", "RMSE");

		    // Evaluate PMF Recommender
		    plot.addSeries("PMF");
		    for (int factors : NUM_FACTORS) {
		      Recommender pmf = new PMF(datamodel, factors, NUM_ITERS, RANDOM_SEED);
		      pmf.fit();

		      QualityMeasure rmse = new RMSE(pmf);
		      double rmseScore = rmse.getScore();
		      plot.setValue("PMF", factors, rmseScore);
		    }

		    // Evaluate BNMF Recommender
		    plot.addSeries("BNMF");
		    for (int factors : NUM_FACTORS) {
		      Recommender bnmf = new BNMF(datamodel, factors, NUM_ITERS, 0.2, 10, RANDOM_SEED);
		      bnmf.fit();

		      QualityMeasure rmse = new RMSE(bnmf);
		      double rmseScore = rmse.getScore();
		      plot.setValue("BNMF", factors, rmseScore);
		    }

		    // Evaluate BiasedMF Recommender
		    plot.addSeries("BiasedMF");
		    for (int factors : NUM_FACTORS) {
		      Recommender biasedmf = new BiasedMF(datamodel, factors, NUM_ITERS, RANDOM_SEED);
		      biasedmf.fit();

		      QualityMeasure rmse = new RMSE(biasedmf);
		      double rmseScore = rmse.getScore();
		      plot.setValue("BiasedMF", factors, rmseScore);
		    }

		    // Evaluate NMF Recommender
		    plot.addSeries("NMF");
		    for (int factors : NUM_FACTORS) {
		      Recommender nmf = new NMF(datamodel, factors, NUM_ITERS, RANDOM_SEED);
		      nmf.fit();

		      QualityMeasure rmse = new RMSE(nmf);
		      double rmseScore = rmse.getScore();
		      plot.setValue("NMF", factors, rmseScore);
		    }

		    // Evaluate CLiMF Recommender
		    plot.addSeries("CLiMF");
		    for (int factors : NUM_FACTORS) {
		      Recommender climf = new CLiMF(datamodel, factors, NUM_ITERS, RANDOM_SEED);
		      climf.fit();

		      QualityMeasure rmse = new RMSE(climf);
		      double rmseScore = rmse.getScore();
		      plot.setValue("CLiMF", factors, rmseScore);
		    }

		    // Evaluate SVDPlusPlus Recommender
		    plot.addSeries("SVDPlusPlus");
		    for (int factors : NUM_FACTORS) {
		      Recommender svdPlusPlus = new SVDPlusPlus(datamodel, factors, NUM_ITERS, RANDOM_SEED);
		      svdPlusPlus.fit();

		      QualityMeasure rmse = new RMSE(svdPlusPlus);
		      double rmseScore = rmse.getScore();
		      plot.setValue("SVDPlusPlus", factors, rmseScore);
		    }

		    // Evaluate HPF Recommender
		    plot.addSeries("HPF");
		    for (int factors : NUM_FACTORS) {
		      Recommender hpf = new HPF(datamodel, factors, NUM_ITERS, RANDOM_SEED);
		      hpf.fit();

		      QualityMeasure rmse = new RMSE(hpf);
		      double rmseScore = rmse.getScore();
		      plot.setValue("HPF", factors, rmseScore);
		    }

		    // Evaluate URP Recommender
		    plot.addSeries("URP");
		    for (int factors : NUM_FACTORS) {
		      Recommender urp =
		          new URP(
		              datamodel, factors, new double[] {1.0, 2.0, 3.0, 4.0, 5.0}, NUM_ITERS, RANDOM_SEED);
		      urp.fit();

		      QualityMeasure rmse = new RMSE(urp);
		      double rmseScore = rmse.getScore();
		      plot.setValue("URP", factors, rmseScore);
		    }


		plot.printData("0.000");

		String[] parts = plot.toString().split("\\r\\n");
		ArrayList<coordinates> resultadoCoordenadas = new ArrayList<coordinates>();
		for (int i = 0; i < parts.length; i++) {
			if (parts[i].indexOf("|") != -1) {
				String[] elements = parts[i].split("\\|");
				if(util.isNumeric(elements[1]) && util.isNumeric(elements[2])) {
					resultadoCoordenadas.add(new coordinates(Float.parseFloat(elements[1]), Float.parseFloat(elements[2])));
				}
			}
		}
		return resultadoCoordenadas;
	}
	

}
