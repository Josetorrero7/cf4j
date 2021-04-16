package com.cf4j.demo.service;

import java.io.IOException;
import java.util.List;

import com.cf4j.demo.entity.Board;
import com.cf4j.demo.entity.Coordinates;

public interface ItemKnnComparisonService {
	
	List<Coordinates> listItemKnnComparison(List<Board> series)throws IOException;

}
