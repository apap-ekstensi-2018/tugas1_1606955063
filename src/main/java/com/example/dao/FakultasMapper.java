package com.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.model.FakultasModel;;

@Mapper
public interface FakultasMapper {
	 @Select("select * from fakultas where id_univ = #{idUniversitas}")
	 List<FakultasModel> selectAllFakultas(@Param("idUniversitas") String idUniversitas);
}