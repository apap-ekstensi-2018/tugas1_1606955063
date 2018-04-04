package com.example.service;

import java.util.List;

import com.example.model.ProdiModel;

public interface ProdiService {
	List<ProdiModel> selectAllProdi(String idFakultas);
}