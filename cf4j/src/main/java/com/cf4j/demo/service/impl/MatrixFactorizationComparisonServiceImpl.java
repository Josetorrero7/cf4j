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

	public static <T, U> List<U> convertStringList(List<T> listOfString, Function<T, U> function) {
		return listOfString.stream().map(function).collect(Collectors.toList());
	}

	/**
	 * Método que principal de la ejecucion del programa. Evalua todos los datos que
	 * le llegan del frontal
	 */
	@Override
	public List<RecomenderResponse> listMatrixFactorizationComparison(Board board) throws IOException {

		// Recogemos DataModel
		DataModel datamodel = getDataset(board.getDataset());

		// Evaluamos el parametro dinamico
		String[] strings = board.getRangeDynamic().getRange().stream().toArray(String[]::new);
		LinePlot plot = null;
		if (board.getRangeDynamic().getName().equals("lambda") || board.getRangeDynamic().getName().equals("gamma")) {
			// Creamos array de Float
			double[] intArray = Stream.of(strings).mapToDouble(Double::parseDouble).toArray();
			plot = new LinePlot(intArray, "Number of latent " + board.getRangeDynamic().getName(),
					board.getQualityMeasure().getName());

		} else {
			// Creamos array de Integer
			int[] intArray = Stream.of(strings).mapToInt(Integer::parseInt).toArray();
			plot = new LinePlot(intArray, "Number of latent " + board.getRangeDynamic().getName(),
					board.getQualityMeasure().getName());
		}

		// Inicializamos el objeto de respuesta
		List<RecomenderResponse> result = new ArrayList<RecomenderResponse>();

		// Iteramos tantos algoritmos como tengamos
		for (int i = 0; i < board.getAlgorithms().size(); i++) {

			// Evaluamos algoritmo
			plot.addSeries(board.getAlgorithms().get(i).getName());

			// Creamos la recomendación
			RecomenderResponse recomender = new RecomenderResponse(
					new Algorithm(board.getAlgorithms().get(i).getName(), board.getAlgorithms().get(i).getParams()),
					new ArrayList(), board.getRangeDynamic().getRange());

			// Introduciomos parametros
			ArrayList<String> params = this.pushParams(board.getAlgorithms().get(i).getName(),
					board.getRangeDynamic().getName());

			// Creamos Map de los parametros con su tipo correcto
			Map<String, Object> map = this.getMap(params, board.getAlgorithms().get(i).getParams());

			// Iteramos la ejecucion en funcion del tipo de dato
			if (board.getRangeDynamic().getName().equals("lambda")
					|| board.getRangeDynamic().getName().equals("gamma")) {

				// Para los Float
				this.iterationDouble(board.getAlgorithms().get(i).getName(), map, plot, recomender, board, datamodel);
			} else {

				// Para los Integer
				this.iterationInteger(board.getAlgorithms().get(i).getName(), map, plot, recomender, board, datamodel);
			}

			// Añadimos la recomendación del algoritmo evaluado
			result.add(recomender);
		}

		// Pintamos por consola el resultado
		plot.printData("0.000");

		return result;
	}

	/**
	 * Método que itera los valores dinamicos de naturaleza Integer y crea la
	 * recomendacón
	 */
	public void iterationInteger(String algorithm, Map<String, Object> map, LinePlot plot,
			RecomenderResponse recomender, Board board, DataModel datamodel) {
		List<Integer> limit = convertStringList(board.getRangeDynamic().getRange(), Integer::parseInt);
		// Iteramos los valores dinamicos
		for (int factors : limit) {
			// Nos creamos la recomendación
			Recommender alg = null;
			// Añadimos a los parametros del algoritmo el valor que iteramos
			map.put(board.getRangeDynamic().getName(), factors);
			// Añadimos el resto de parametros
			alg = this.getAlgorihm(algorithm, map, datamodel);
			alg.fit();
			// Recogemos la medida de calidad
			double score = this.getQualityMeasure(alg, board.getQualityMeasure());
			plot.setValue(algorithm, factors, score);
			// Añadimos a nuestro resultado la ultima evaluación ejecutada
			recomender.getResults().add(score);
		}
	}

	/**
	 * Método que itera los valores dinamicos de naturaleza Float y crea la
	 * recomendacón
	 */
	public void iterationDouble(String algorithm, Map<String, Object> map, LinePlot plot, RecomenderResponse recomender,
			Board board, DataModel datamodel) {
		List<Double> limit = convertStringList(board.getRangeDynamic().getRange(), Double::parseDouble);
		// Iteramos los valores dinamicos
		for (double factors : limit) {
			// Nos creamos la recomendación
			Recommender alg = null;
			// Añadimos a los parametros del algoritmo el valor que iteramos
			map.put(board.getRangeDynamic().getName(), factors);
			// Añadimos el resto de parametros
			alg = this.getAlgorihm(algorithm, map, datamodel);
			alg.fit();
			// Recogemos la medida de calidad
			double score = this.getQualityMeasure(alg, board.getQualityMeasure());
			plot.setValue(algorithm, factors, score);
			// Añadimos a nuestro resultado la ultima evaluación ejecutada
			recomender.getResults().add(score);
		}
	}

	/**
	 * Método que evalua el Algoritmo que le llega por parametro
	 * 
	 * @return Objeto Recommender con el algoritmo que estamos iterando
	 */
	public Recommender getAlgorihm(String algorithm, Map<String, Object> map, DataModel datamodel) {
		Recommender alg = null;
		//Evaluamos cada uno de los posibles algoritmos soportados.
		switch (algorithm) {
		case "PMF":
			alg = new PMF(datamodel, map);
			break;
		case "BNMF":
			alg = new BNMF(datamodel, map);
			break;
		case "BiasedMF":
			alg = new BiasedMF(datamodel, map);
			break;
		case "NMF":
			alg = new NMF(datamodel, map);
			break;
		case "CLiMF":
			alg = new CLiMF(datamodel, map);
			break;
		case "SVDPlusPlus":
			alg = new SVDPlusPlus(datamodel, map);
			break;
		case "HPF":
			alg = new HPF(datamodel, map);
			break;
		case "URP":
			alg = new URP(datamodel, map);
			break;
		default:
			break;
		}
		return alg;
	}

	/**
	 * Método que evalua la medida de calidad introducida
	 * 
	 * @return Objeto con la medida de calidad
	 */
	public double getQualityMeasure(Recommender alg, Quality qualityMeasure) {
		double result = 0;
		//Evaluamos cada una de las medidas de calidad soportadas.
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
			QualityMeasure f1 = new F1(alg, qualityMeasure.getNumberOfRecommendations(),
					qualityMeasure.getRelevantThreshold());
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
			QualityMeasure precision = new Precision(alg, qualityMeasure.getNumberOfRecommendations(),
					qualityMeasure.getRelevantThreshold());
			result = precision.getScore();
			break;
		case "R2":
			QualityMeasure r2 = new R2(alg);
			result = r2.getScore();
			break;
		case "Recall":
			QualityMeasure recall = new Recall(alg, qualityMeasure.getNumberOfRecommendations(),
					qualityMeasure.getRelevantThreshold());
			result = recall.getScore();
			break;
		case "RMSE":
			QualityMeasure rmse = new RMSE(alg);
			result = rmse.getScore();
			break;
		default:
			break;
		}

		return result;
	}

	/**
	 * Método que evalua el algoritmo que estamos iterando e introduce sus
	 * parametros
	 * 
	 * @return ArrayList con los parametros del algoritmo que estamos iterando
	 */
	public ArrayList<String> pushParams(String alg, String removeParam) {
		ArrayList<String> params = new ArrayList<String>();
		//Evaluamos el algoritmo que estamos iterando y le introducimos sus parametros correspondientes
		if (alg.equals("PMF") || alg.equals("BiasedMF") || alg.equals("SVDPlusPlus")) {
			params.add("numFactors");
			params.add("numIters");
			params.add("lambda");
			params.add("gamma");
			params.add("seed");
		} else if (alg.equals("NMF")) {
			params.add("numFactors");
			params.add("numIters");
			params.add("seed");
		} else if (alg.equals("BNMF")) {
			params.add("numFactors");
			params.add("numIters");
			params.add("alpha");
			params.add("beta");
			params.add("r");
			params.add("seed");
		} else if (alg.equals("CLiMF")) {
			params.add("numFactors");
			params.add("numIters");
			params.add("gamma");
			params.add("lambda");
			params.add("threshold");
			params.add("seed");
		} else if (alg.equals("URP")) {
			params.add("numFactors");
			params.add("numIters");
			params.add("ratings");
			params.add("H");
			params.add("seed");
		} else if (alg.equals("HPF")) {
			params.add("numFactors");
			params.add("numIters");
			params.add("a");
			params.add("aPrime");
			params.add("bPrime");
			params.add("c");
			params.add("cPrime");
			params.add("dPrime");
			params.add("seed");
		}
		// Eliminamos el algorimos dinamico
		params.remove(removeParam);
		return params;
	}

	/**
	 * Método que evalua los parametros del algoritmo que iteramos y crea su tipo de
	 * dato correspondiente
	 * 
	 * @return Map<String, Object> con los parametros del algoritmo que estamos
	 *         iterando y su tipo de dato correspondiente
	 */
	public Map<String, Object> getMap(ArrayList<String> items, List<KeyValue> params) {

		Map<String, Object> map = new HashMap<String, Object>();
		//Nos recorremos los parametros del algoritmos que estamos iterando
		for (int j = 0; j < items.size(); j++) {
			//Buscamos dentro de la lista de parametros recibidos el valor del que queremos
			KeyValue value = utils.getKeyValue(items.get(j), params);
			//En caso de que el valor sea distinto de null lo tratamos
			if (value.getValue() != null) {
				if (value.getKey().equals("")) {
					map.put(items.get(j), value.getValue());
				}
				//Casteamos el valor del parametro al tipo correspondiente
				if (value.getKey().equals("numFactors") || value.getKey().equals("numIters")) {
					map.put(items.get(j), Integer.parseInt(value.getValue()));
				} else if (value.getKey().equals("lambda") || value.getKey().equals("gamma")
						|| value.getKey().equals("alpha") || value.getKey().equals("beta") || value.getKey().equals("r")
						|| value.getKey().equals("threshold") || value.getKey().equals("H")
						|| value.getKey().equals("a") || value.getKey().equals("aPrime")
						|| value.getKey().equals("bPrime") || value.getKey().equals("c")
						|| value.getKey().equals("cPrime") || value.getKey().equals("dPrime")
						|| value.getKey().equals("seed")) {
					map.put(items.get(j), Double.parseDouble(value.getValue()));
				} else if (value.getKey().equals("ratings")) {
					double ratings = Double.parseDouble(value.getValue());
					double[] arrayDouble = new double[1];
					arrayDouble[0] = ratings;
					map.put(items.get(j), arrayDouble);
				} else {
					map.put(items.get(j), value.getValue());
				}
			}
		}

		return map;

	}

	/**
	 * Método que evalua el DataModel introducido
	 * 
	 * @return Objeto con el DataModel introducido
	 */
	public DataModel getDataset(String dataset) throws IOException {
		DataModel datamodel = null;
		//Evaluamos cada una de los DataModel soportados.
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
