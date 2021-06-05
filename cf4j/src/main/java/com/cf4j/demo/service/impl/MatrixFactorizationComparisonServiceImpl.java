package com.cf4j.demo.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import com.cf4j.demo.entity.Board;
import com.cf4j.demo.entity.KeyValue;
import com.cf4j.demo.entity.Algorithm;
import com.cf4j.demo.entity.RecomenderResponse;
import com.cf4j.demo.service.MatrixFactorizationComparisonService;
import com.cf4j.demo.utils.utils;
import com.cf4j.demo.entity.Quality;
import es.upm.etsisi.cf4j.data.BenchmarkDataModels;
import es.upm.etsisi.cf4j.data.DataModel;
import es.upm.etsisi.cf4j.qualityMeasure.QualityMeasure;
import es.upm.etsisi.cf4j.qualityMeasure.prediction.Coverage;
import es.upm.etsisi.cf4j.qualityMeasure.prediction.MAE;
import es.upm.etsisi.cf4j.qualityMeasure.prediction.MSE;
import es.upm.etsisi.cf4j.qualityMeasure.prediction.MSLE;
import es.upm.etsisi.cf4j.qualityMeasure.prediction.Max;
import es.upm.etsisi.cf4j.qualityMeasure.prediction.Perfect;
import es.upm.etsisi.cf4j.qualityMeasure.prediction.R2;
import es.upm.etsisi.cf4j.qualityMeasure.prediction.RMSE;
import es.upm.etsisi.cf4j.qualityMeasure.recommendation.Discovery;
import es.upm.etsisi.cf4j.qualityMeasure.recommendation.Diversity;
import es.upm.etsisi.cf4j.qualityMeasure.recommendation.F1;
import es.upm.etsisi.cf4j.qualityMeasure.recommendation.NDCG;
import es.upm.etsisi.cf4j.qualityMeasure.recommendation.Novelty;
import es.upm.etsisi.cf4j.qualityMeasure.recommendation.Precision;
import es.upm.etsisi.cf4j.qualityMeasure.recommendation.Recall;
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

	public static <T, U> List<U> convertStringList(List<T> listOfString, Function<T, U> function) {
		return listOfString.stream().map(function).collect(Collectors.toList());
	}

	@Override
	public List<RecomenderResponse> listMatrixFactorizationComparison(Board board) throws IOException {

		// DataModel load
		DataModel datamodel = getDataset(board.getDataset());

		// To store results

		String[] strings = board.getRangeDynamic().getRange().stream().toArray(String[]::new);
		LinePlot plot = null;
		if (board.getRangeDynamic().getName().equals("lambda") || board.getRangeDynamic().getName().equals("gamma")) {
			double[] intArray = Stream.of(strings).mapToDouble(Double::parseDouble).toArray();
			plot = new LinePlot(intArray, "Number of latent factors", board.getQualityMeasure().getName());

		} else {
			int[] intArray = Stream.of(strings).mapToInt(Integer::parseInt).toArray();
			plot = new LinePlot(intArray, "Number of latent factors", board.getQualityMeasure().getName());
		}

		List<RecomenderResponse> result = new ArrayList<RecomenderResponse>();

		for (int i = 0; i < board.getAlgorithms().size(); i++) {
			if (board.getAlgorithms().get(i).getName().equals("PMF")) {

				// Evaluate PMF Recommender
				plot.addSeries("PMF");
				RecomenderResponse recomender = new RecomenderResponse(
						new Algorithm(board.getAlgorithms().get(i).getName(), board.getAlgorithms().get(i).getParams()),
						new ArrayList(), board.getRangeDynamic().getRange());

				// Parametros
				ArrayList<String> params = this.pushParams(board.getAlgorithms().get(i).getName(),
						board.getRangeDynamic().getName());

				// Creamos Map de los parametros con su tipo correcto
				Map<String, Object> map = this.getMap(params, board.getAlgorithms().get(i).getParams());

				// Iteramos la ejecucion en funcion del tipo de dato
				if (board.getRangeDynamic().getName().equals("lambda")
						|| board.getRangeDynamic().getName().equals("gamma")) {
					this.iterationDouble("PMF", map, plot, recomender, board, datamodel);
				} else {
					this.iterationInteger("PMF", map, plot, recomender, board, datamodel);
				}
				result.add(recomender);
			}
//			else if (board.getAlgorithms().get(i).equals("BNMF")) {
//				// Evaluate BNMF Recommender
//				plot.addSeries("BNMF");
//				RecomenderResponse recomender = new RecomenderResponse("BNMF", new ArrayList(), new ArrayList());
//				recomender.setAlgorithm("BNMF");
//				for (int factors : board.getParam()) {
//					Recommender bnmf = new BNMF(datamodel, factors, NUM_ITERS, 0.2, 10, RANDOM_SEED);
//					bnmf.fit();
//
//					QualityMeasure rmse = new RMSE(bnmf);
//					double rmseScore = rmse.getScore();
//					plot.setValue("BNMF", factors, rmseScore);
//					recomender.getParam().add(factors);
//					recomender.getResults().add(rmseScore);
//				}
//				result.add(recomender);
//			}
			else if (board.getAlgorithms().get(i).getName().equals("BiasedMF")) {
				// Evaluate BiasedMF Recommender
				plot.addSeries("BiasedMF");
				RecomenderResponse recomender = new RecomenderResponse(
						new Algorithm(board.getAlgorithms().get(i).getName(), board.getAlgorithms().get(i).getParams()),
						new ArrayList(), board.getRangeDynamic().getRange());

				// Parametros
				ArrayList<String> params = this.pushParams(board.getAlgorithms().get(i).getName(),
						board.getRangeDynamic().getName());

				// Creamos Map de los parametros con su tipo correcto
				Map<String, Object> map = this.getMap(params, board.getAlgorithms().get(i).getParams());

				// Iteramos la ejecucion en funcion del tipo de dato
				if (board.getRangeDynamic().getName().equals("lambda")
						|| board.getRangeDynamic().getName().equals("gamma")) {
					this.iterationDouble("BiasedMF", map, plot, recomender, board, datamodel);
				} else {
					this.iterationInteger("BiasedMF", map, plot, recomender, board, datamodel);
				}
				result.add(recomender);
			} else if (board.getAlgorithms().get(i).getName().equals("NMF")) {
				// Evaluate NMF Recommender
				plot.addSeries("NMF");
				RecomenderResponse recomender = new RecomenderResponse(
						new Algorithm(board.getAlgorithms().get(i).getName(), board.getAlgorithms().get(i).getParams()),
						new ArrayList(), board.getRangeDynamic().getRange());
				// Parametros
				ArrayList<String> params = this.pushParams(board.getAlgorithms().get(i).getName(),
						board.getRangeDynamic().getName());

				// Creamos Map de los parametros con su tipo correcto
				Map<String, Object> map = this.getMap(params, board.getAlgorithms().get(i).getParams());

				// Iteramos la ejecucion en funcion del tipo de dato
				this.iterationInteger("NMF", map, plot, recomender, board, datamodel);

				result.add(recomender);
			}
			// else if (board.getAlgorithms().get(i).equals("CLiMF")) {
//				// Evaluate CLiMF Recommender
//				plot.addSeries("CLiMF");
//				RecomenderResponse recomender = new RecomenderResponse("CLiMF", new ArrayList(), new ArrayList());
//				recomender.setAlgorithm("CLiMF");
//				for (int factors : board.getParam()) {
//					Recommender climf = new CLiMF(datamodel, factors, NUM_ITERS, RANDOM_SEED);
//					climf.fit();
//
//					QualityMeasure rmse = new RMSE(climf);
//					double rmseScore = rmse.getScore();
//					plot.setValue("CLiMF", factors, rmseScore);
//					recomender.getParam().add(factors);
//					recomender.getResults().add(rmseScore);
//				}
//				result.add(recomender);
//			} else if (board.getAlgorithms().get(i).equals("SVDPlusPlus")) {
//				// Evaluate SVDPlusPlus Recommender
//				plot.addSeries("SVDPlusPlus");
//				RecomenderResponse recomender = new RecomenderResponse("SVDPlusPlus", new ArrayList(), new ArrayList());
//				recomender.setAlgorithm("SVDPlusPlus");
//				for (int factors : board.getParam()) {
//					Recommender svdPlusPlus = new SVDPlusPlus(datamodel, factors, NUM_ITERS, RANDOM_SEED);
//					svdPlusPlus.fit();
//
//					QualityMeasure rmse = new RMSE(svdPlusPlus);
//					double rmseScore = rmse.getScore();
//					plot.setValue("SVDPlusPlus", factors, rmseScore);
//					recomender.getParam().add(factors);
//					recomender.getResults().add(rmseScore);
//				}
//				result.add(recomender);
//			} else if (board.getAlgorithms().get(i).equals("HPF")) {
//				// Evaluate HPF Recommender
//				plot.addSeries("HPF");
//				RecomenderResponse recomender = new RecomenderResponse("HPF", new ArrayList(), new ArrayList());
//				for (int factors : board.getParam()) {
//					Recommender hpf = new HPF(datamodel, factors, NUM_ITERS, RANDOM_SEED);
//					hpf.fit();
//
//					QualityMeasure rmse = new RMSE(hpf);
//					double rmseScore = rmse.getScore();
//					plot.setValue("HPF", factors, rmseScore);
//					recomender.getParam().add(factors);
//					recomender.getResults().add(rmseScore);
//				}
//				result.add(recomender);
//			} else if (board.getAlgorithms().get(i).equals("URP")) {
//				// Evaluate URP Recommender
//				plot.addSeries("URP");
//				RecomenderResponse recomender = new RecomenderResponse("URP", new ArrayList(), new ArrayList());
//				for (int factors : board.getParam()) {
//					Recommender urp = new URP(datamodel, factors, new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 }, NUM_ITERS,
//							RANDOM_SEED);
//					urp.fit();
//
//					QualityMeasure rmse = new RMSE(urp);
//					double rmseScore = rmse.getScore();
//					plot.setValue("URP", factors, rmseScore);
//
//					recomender.getParam().add(factors);
//					recomender.getResults().add(rmseScore);
//				}
//				result.add(recomender);
//			}
		}

		plot.printData("0.000");
//		plot.draw();

//		String[] parts = plot.toString().split("\\r\\n");
//		List<Coordinates> resultadoCoordenadas = new ArrayList<Coordinates>();
//		for (int i = 0; i < parts.length; i++) {
//			if (parts[i].indexOf("|") != -1) {
//				String[] elements = parts[i].split("\\|");
//				if (util.isNumeric(elements[1]) && util.isNumeric(elements[2])) {
//					resultadoCoordenadas
//							.add(new Coordinates(Float.parseFloat(elements[1]), Float.parseFloat(elements[2])));
//				}
//			}
//		}
		return result;
	}

	public void iterationInteger(String algorithm, Map<String, Object> map, LinePlot plot,
			RecomenderResponse recomender, Board board, DataModel datamodel) {
		List<Integer> limit = convertStringList(board.getRangeDynamic().getRange(), Integer::parseInt);

		for (int factors : limit) {
			Recommender alg = null;
			map.put(board.getRangeDynamic().getName(), factors);
			if (algorithm.equals("PMF")) {
				alg = new PMF(datamodel, map);
			} else if (algorithm.equals("BiasedMF")) {
				alg = new BiasedMF(datamodel, map);
			}
			alg.fit();
			double score = this.getQualityMeasure(alg, board.getQualityMeasure());
			plot.setValue(algorithm, factors, score);
			recomender.getResults().add(score);
		}
	}

	public void iterationDouble(String algorithm, Map<String, Object> map, LinePlot plot, RecomenderResponse recomender,
			Board board, DataModel datamodel) {
		List<Double> limit = convertStringList(board.getRangeDynamic().getRange(), Double::parseDouble);

		for (double factors : limit) {
			Recommender alg = null;
			map.put(board.getRangeDynamic().getName(), factors);
			if (algorithm.equals("PMF")) {
				alg = new PMF(datamodel, map);
			} else if (algorithm.equals("BiasedMF")) {
				alg = new BiasedMF(datamodel, map);
			} else if (algorithm.equals("NMF")) {
				alg = new NMF(datamodel, map);
			}
			alg.fit();
			double score = this.getQualityMeasure(alg, board.getQualityMeasure());
			plot.setValue(algorithm, factors, score);
			recomender.getResults().add(score);
		}
	}

	public double getQualityMeasure(Recommender alg, Quality qualityMeasure) {
		double result = 0;
//		 if (alg instanceof NMF) {

		switch (qualityMeasure.getName()) {
		case "Coverage":
			QualityMeasure coverage = new Coverage(alg);
			result = coverage.getScore();
			break;
		case "Discovery":
			QualityMeasure discovery = new Discovery(alg, qualityMeasure.getNumberOfRecommendations());
			result = discovery.getScore();
			break;
		case "Diversity":
			QualityMeasure diversity = new Diversity(alg, qualityMeasure.getNumberOfRecommendations());
			result = diversity.getScore();
			break;
		case "F1":
			QualityMeasure f1 = new F1(alg, qualityMeasure.getNumberOfRecommendations(), qualityMeasure.getRelevantThreshold());
			result = f1.getScore();
			break;
		case "MAE":
			QualityMeasure mae = new MAE(alg);
			result = mae.getScore();
			break;
		case "Max":
			QualityMeasure max = new Max(alg);
			result = max.getScore();
			break;
		case "MSE":
			QualityMeasure mse = new MSE(alg);
			result = mse.getScore();
			break;
		case "MSLE":
			QualityMeasure msle = new MSLE(alg);
			result = msle.getScore();
			break;
		case "NDCG":
			QualityMeasure ndcg = new NDCG(alg, qualityMeasure.getNumberOfRecommendations());
			result = ndcg.getScore();
			break;
		case "Novelty":
			QualityMeasure novelty = new Novelty(alg, qualityMeasure.getNumberOfRecommendations());
			result = novelty.getScore();
			break;
		case "Perfect":
			QualityMeasure perfect = new Perfect(alg, qualityMeasure.getRelevantThreshold());
			result = perfect.getScore();
			break;
		case "Precision":
			QualityMeasure precision = new Precision(alg, qualityMeasure.getNumberOfRecommendations(), qualityMeasure.getRelevantThreshold());
			result = precision.getScore();
			break;
		case "R2":
			QualityMeasure r2 = new R2(alg);
			result = r2.getScore();
			break;
		case "Recall":
			QualityMeasure recall = new Recall(alg, qualityMeasure.getNumberOfRecommendations(), qualityMeasure.getRelevantThreshold());
			result = recall.getScore();
			break;
		case "RMSE":
			QualityMeasure rmse = new RMSE(alg);
			result = rmse.getScore();
			break;
//		default:
//			QualityMeasure rmse = new RMSE(alg);
//			result = rmse.getScore();
//			break;
		}

		return result;
	}

	public ArrayList<String> pushParams(String alg, String removeParam) {
		ArrayList<String> params = new ArrayList<String>();
		if (alg.equals("PMF") || alg.equals("BiasedMF")) {
			params.add("numFactors");
			params.add("numIters");
			params.add("lambda");
			params.add("gamma");
			params.add("seed");
		} else if (alg.equals("NMF")) {
			params.add("numFactors");
			params.add("numIters");
			params.add("seed");
		}

		params.remove(removeParam);
		return params;
	}

	public Map<String, Object> getMap(ArrayList<String> items, List<KeyValue> params) {

		Map<String, Object> map = new HashMap<String, Object>();
		for (int j = 0; j < items.size(); j++) {

			KeyValue value = utils.getKeyValue(items.get(j), params);
			if (value.getValue() != null) {
				if (value.getKey().equals("")) {
					map.put(items.get(j), value.getValue());
				}
				switch (value.getKey()) {
				case "numFactors":
					map.put(items.get(j), Integer.parseInt(value.getValue()));
					break;
				case "numIters":
					map.put(items.get(j), Integer.parseInt(value.getValue()));
					break;
				case "lambda":
					map.put(items.get(j), Double.parseDouble(value.getValue()));
					break;
				case "gamma":
					map.put(items.get(j), Double.parseDouble(value.getValue()));
					break;
				case "seed":
					map.put(items.get(j), Long.parseLong(value.getValue()));
					break;
				default:
					map.put(items.get(j), value.getValue());
					break;
				}
			}
		}

		return map;

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
