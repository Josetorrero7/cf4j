package com.cf4j.demo.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cf4j.demo.entity.Serie;
import com.cf4j.demo.entity.Coordinates;
import com.cf4j.demo.service.MatrixFactorizationComparisonService;
import com.cf4j.demo.utils.utils;

import es.upm.etsisi.cf4j.data.BenchmarkDataModels;
import es.upm.etsisi.cf4j.data.DataModel;
import es.upm.etsisi.cf4j.qualityMeasure.QualityMeasure;
import es.upm.etsisi.cf4j.qualityMeasure.prediction.RMSE;
import es.upm.etsisi.cf4j.recommender.Recommender;
import es.upm.etsisi.cf4j.recommender.matrixFactorization.BNMF;
import es.upm.etsisi.cf4j.recommender.matrixFactorization.BiasedMF;
import es.upm.etsisi.cf4j.recommender.matrixFactorization.CLiMF;
import es.upm.etsisi.cf4j.recommender.matrixFactorization.HPF;
import es.upm.etsisi.cf4j.recommender.matrixFactorization.NMF;
import es.upm.etsisi.cf4j.recommender.matrixFactorization.PMF;
import es.upm.etsisi.cf4j.recommender.matrixFactorization.SVDPlusPlus;
import es.upm.etsisi.cf4j.recommender.matrixFactorization.URP;
import es.upm.etsisi.cf4j.util.Range;
import es.upm.etsisi.cf4j.util.plot.LinePlot;

@Service
public class MatrixFactorizationComparisonServiceImpl implements MatrixFactorizationComparisonService {
	public utils util = new utils();

	private static final int[] NUM_FACTORS = Range.ofIntegers(5, 5, 5);

	private static final int NUM_ITERS = 50;

	private static final long RANDOM_SEED = 43;

	@Override
	public List<Coordinates> listMatrixFactorizationComparison(List<Serie> series) throws IOException {

		// DataModel load
		DataModel datamodel = BenchmarkDataModels.MovieLens100K();

		// To store results
		LinePlot plot = new LinePlot(NUM_FACTORS, "Number of latent factors", "RMSE");

		for (int i = 0; i < series.size(); i++) {
			if (series.get(i).getName().equals("PMF")) {
				// Evaluate PMF Recommender
				plot.addSeries("PMF");
				for (int factors : NUM_FACTORS) {
					Recommender pmf = new PMF(datamodel, factors, NUM_ITERS, RANDOM_SEED);
					pmf.fit();

					QualityMeasure rmse = new RMSE(pmf);
					double rmseScore = rmse.getScore();
					plot.setValue("PMF", factors, rmseScore);
				}
			} else if (series.get(i).getName().equals("BNMF")) {
				// Evaluate BNMF Recommender
				plot.addSeries("BNMF");
				for (int factors : NUM_FACTORS) {
					Recommender bnmf = new BNMF(datamodel, factors, NUM_ITERS, 0.2, 10, RANDOM_SEED);
					bnmf.fit();

					QualityMeasure rmse = new RMSE(bnmf);
					double rmseScore = rmse.getScore();
					plot.setValue("BNMF", factors, rmseScore);
				}
			} else if (series.get(i).getName().equals("BiasedMF")) {
				// Evaluate BiasedMF Recommender
				plot.addSeries("BiasedMF");
				for (int factors : NUM_FACTORS) {
					Recommender biasedmf = new BiasedMF(datamodel, factors, NUM_ITERS, RANDOM_SEED);
					biasedmf.fit();

					QualityMeasure rmse = new RMSE(biasedmf);
					double rmseScore = rmse.getScore();
					plot.setValue("BiasedMF", factors, rmseScore);
				}
			} else if (series.get(i).getName().equals("NMF")) {
				// Evaluate NMF Recommender
				plot.addSeries("NMF");
				for (int factors : NUM_FACTORS) {
					Recommender nmf = new NMF(datamodel, factors, NUM_ITERS, RANDOM_SEED);
					nmf.fit();

					QualityMeasure rmse = new RMSE(nmf);
					double rmseScore = rmse.getScore();
					plot.setValue("NMF", factors, rmseScore);
				}
			} else if (series.get(i).getName().equals("CLiMF")) {
				// Evaluate CLiMF Recommender
				plot.addSeries("CLiMF");
				for (int factors : NUM_FACTORS) {
					Recommender climf = new CLiMF(datamodel, factors, NUM_ITERS, RANDOM_SEED);
					climf.fit();

					QualityMeasure rmse = new RMSE(climf);
					double rmseScore = rmse.getScore();
					plot.setValue("CLiMF", factors, rmseScore);
				}
			} else if (series.get(i).getName().equals("SVDPlusPlus")) {
				// Evaluate SVDPlusPlus Recommender
				plot.addSeries("SVDPlusPlus");
				for (int factors : NUM_FACTORS) {
					Recommender svdPlusPlus = new SVDPlusPlus(datamodel, factors, NUM_ITERS, RANDOM_SEED);
					svdPlusPlus.fit();

					QualityMeasure rmse = new RMSE(svdPlusPlus);
					double rmseScore = rmse.getScore();
					plot.setValue("SVDPlusPlus", factors, rmseScore);
				}
			} else if (series.get(i).getName().equals("HPF")) {
				// Evaluate HPF Recommender
				plot.addSeries("HPF");
				for (int factors : NUM_FACTORS) {
					Recommender hpf = new HPF(datamodel, factors, NUM_ITERS, RANDOM_SEED);
					hpf.fit();

					QualityMeasure rmse = new RMSE(hpf);
					double rmseScore = rmse.getScore();
					plot.setValue("HPF", factors, rmseScore);
				}
			} else if (series.get(i).getName().equals("URP")) {
				// Evaluate URP Recommender
				plot.addSeries("URP");
				for (int factors : NUM_FACTORS) {
					Recommender urp = new URP(datamodel, factors, new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 }, NUM_ITERS,
							RANDOM_SEED);
					urp.fit();

					QualityMeasure rmse = new RMSE(urp);
					double rmseScore = rmse.getScore();
					plot.setValue("URP", factors, rmseScore);
				}
			}
		}

		plot.printData("0.000");

		String[] parts = plot.toString().split("\\r\\n");
		List<Coordinates> resultadoCoordenadas = new ArrayList<Coordinates>();
		for (int i = 0; i < parts.length; i++) {
			if (parts[i].indexOf("|") != -1) {
				String[] elements = parts[i].split("\\|");
				if (util.isNumeric(elements[1]) && util.isNumeric(elements[2])) {
					resultadoCoordenadas
							.add(new Coordinates(Float.parseFloat(elements[1]), Float.parseFloat(elements[2])));
				}
			}
		}
		return resultadoCoordenadas;
	}

}