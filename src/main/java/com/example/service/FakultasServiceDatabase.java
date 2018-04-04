package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.FakultasMapper;
import com.example.model.FakultasModel;

import lombok.extern.slf4j.Slf4j;;

@Slf4j
@Service
public class FakultasServiceDatabase implements FakultasService{
		@Autowired
	    private FakultasMapper fakultasMapper;
	    
	    @Override
	    public List<FakultasModel> selectAllFakultas(String idUniversitas)
	    {
	        log.info ("select all fakultas");
	        return fakultasMapper.selectAllFakultas(idUniversitas);
	    }

}