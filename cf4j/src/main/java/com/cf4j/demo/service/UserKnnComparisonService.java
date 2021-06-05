package com.cf4j.demo.service;

import java.io.IOException;
import java.util.List;

import com.cf4j.demo.entity.Board;
import com.cf4j.demo.entity.RecomenderResponse;

public interface UserKnnComparisonService {
	
	List<RecomenderResponse> listUserKnnComparison(List<Board> series)throws IOException;

}
