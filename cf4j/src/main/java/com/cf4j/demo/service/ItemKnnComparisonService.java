package com.cf4j.demo.service;

import java.io.IOException;
import java.util.List;

import com.cf4j.demo.entity.Board;
import com.cf4j.demo.entity.RecomenderResponse;

public interface ItemKnnComparisonService {
	
	List<RecomenderResponse> listItemKnnComparison(List<Board> series)throws IOException;

}
