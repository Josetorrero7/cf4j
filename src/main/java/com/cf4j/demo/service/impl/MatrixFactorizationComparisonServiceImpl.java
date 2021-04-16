package com.cf4j.demo.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cf4j.demo.entity.Board;
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
	public List<Coordinates> listMatrixFactorizationComparison(Board board) throws IOException {

		// DataModel load
		DataModel datamodel = getDataset(board.getDataset());

		// To store results
		LinePlot plot = new LinePlot(board.getParam(), "Number of latent factors", "RMSE");

		for (int i = 0; i < board.getAlgorithms().size(); i++) {
			if (board.getAlgorithms().get(i).equals("PMF")) {
				// Evaluate PMF Recommender
				plot.addSeries("PMF");
				for (int factors : board.getParam()) {
					Recommender pmf = new PMF(datamodel, factors, NUM_ITERS, RANDOM_SEED);
					pmf.fit();

					QualityMeasure rmse = new RMSE(pmf);
					double rmseScore = rmse.getScore();
					plot.setValue("PMF", factors, rmseScore);
				}
			} else if (board.getAlgorithms().get(i).equals("BNMF")) {
				// Evaluate BNMF Recommender
				plot.addSeries("BNMF");
				for (int factors : board.getParam()) {
					Recommender bnmf = new BNMF(datamodel, factors, NUM_ITERS, 0.2, 10, RANDOM_SEED);
					bnmf.fit();

					QualityMeasure rmse = new RMSE(bnmf);
					double rmseScore = rmse.getScore();
					plot.setValue("BNMF", factors, rmseScore);
				}
			} else if (board.getAlgorithms().get(i).equals("BiasedMF")) {
				// Evaluate BiasedMF Recommender
				plot.addSeries("BiasedMF");
				for (int factors : board.getParam()) {
					Recommender biasedmf = new BiasedMF(datamodel, factors, NUM_ITERS, RANDOM_SEED);
					biasedmf.fit();

					QualityMeasure rmse = new RMSE(biasedmf);
					double rmseScore = rmse.getScore();
					plot.setValue("BiasedMF", factors, rmseScore);
				}
			} else if (board.getAlgorithms().get(i).equals("NMF")) {
				// Evaluate NMF Recommender
				plot.addSeries("NMF");
				for (int factors : board.getParam()) {
					Recommender nmf = new NMF(datamodel, factors, NUM_ITERS, RANDOM_SEED);
					nmf.fit();

					QualityMeasure rmse = new RMSE(nmf);
					double rmseScore = rmse.getScore();
					plot.setValue("NMF", factors, rmseScore);
				}
			} else if (board.getAlgorithms().get(i).equals("CLiMF")) {
				// Evaluate CLiMF Recommender
				plot.addSeries("CLiMF");
				for (int factors : board.getParam()) {
					Recommender climf = new CLiMF(datamodel, factors, NUM_ITERS, RANDOM_SEED);
					climf.fit();

					QualityMeasure rmse = new RMSE(climf);
					double rmseScore = rmse.getScore();
					plot.setValue("CLiMF", factors, rmseScore);
				}
			} else if (board.getAlgorithms().get(i).equals("SVDPlusPlus")) {
				// Evaluate SVDPlusPlus Recommender
				plot.addSeries("SVDPlusPlus");
				for (int factors : board.getParam()) {
					Recommender svdPlusPlus = new SVDPlusPlus(datamodel, factors, NUM_ITERS, RANDOM_SEED);
					svdPlusPlus.fit();

					QualityMeasure rmse = new RMSE(svdPlusPlus);
					double rmseScore = rmse.getScore();
					plot.setValue("SVDPlusPlus", factors, rmseScore);
				}
			} else if (board.getAlgorithms().get(i).equals("HPF")) {
				// Evaluate HPF Recommender
				plot.addSeries("HPF");
				for (int factors : board.getParam()) {
					Recommender hpf = new HPF(datamodel, factors, NUM_ITERS, RANDOM_SEED);
					hpf.fit();

					QualityMeasure rmse = new RMSE(hpf);
					double rmseScore = rmse.getScore();
					plot.setValue("HPF", factors, rmseScore);
				}
			} else if (board.getAlgorithms().get(i).equals("URP")) {
				// Evaluate URP Recommender
				plot.addSeries("URP");
				for (int factors : board.getParam()) {
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
//		plot.draw();
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

	public DataModel getDataset(String dataset) throws IOException {
		// DataModel load
		DataModel datamodel = null;
		switch (dataset) {
		case "TMovieLens100K":
			datamodel = BenchmarkDataModels.MovieLens100K();
			break;
		case "MovieLens1M":
			datamodel = BenchmarkDataModels.MovieLens1M();
			break;

		case "MovieLens10M":
			datamodel = BenchmarkDataModels.MovieLens10M();
			break;

		case "FilmTrust":
			datamodel = BenchmarkDataModels.FilmTrust();
			break;

		case "BookCrossing":
			datamodel = BenchmarkDataModels.BookCrossing();
			break;

		case "LibimSeTi":
			datamodel = BenchmarkDataModels.LibimSeTi();
			break;

		case "MyAnimeList":
			datamodel = BenchmarkDataModels.MyAnimeList();
			break;

		case "Jester":
			datamodel = BenchmarkDataModels.Jester();
			break;

		case "Netflix Prize":
			datamodel = BenchmarkDataModels.NetflixPrize();
			break;
		default:
			break;
		}

		return datamodel;
	}

}
