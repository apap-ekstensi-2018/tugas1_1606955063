package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.UniversitasMapper;
import com.example.model.UniversitasModel;

import lombok.extern.slf4j.Slf4j;;

@Slf4j
@Service
public class UniversitasServiceDatabase implements UniversitasService{
		@Autowired
	    private UniversitasMapper universitasMapper;
	    
	    @Override
	    public List<UniversitasModel> selectAllUniversitas()
	    {
	        log.info ("select all universitas");
	        return universitasMapper.selectAllUniversitas();
	    }

}
