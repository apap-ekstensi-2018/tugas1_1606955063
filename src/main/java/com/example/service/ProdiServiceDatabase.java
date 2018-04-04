package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.ProdiMapper;
import com.example.model.ProdiModel;

import lombok.extern.slf4j.Slf4j;;

@Slf4j
@Service
public class ProdiServiceDatabase implements ProdiService{
		@Autowired
	    private ProdiMapper prodiMapper;
	    
	    @Override
	    public List<ProdiModel> selectAllProdi(String idFakultas)
	    {
	        log.info ("select all fakultas");
	        return prodiMapper.selectAllProdi(idFakultas);
	    }

}