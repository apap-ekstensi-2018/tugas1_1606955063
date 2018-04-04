package com.example.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.ProdiMapper;
import com.example.dao.StudentMapper;
import com.example.model.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentServiceDatabase implements StudentService
{
    @Autowired
    private StudentMapper studentMapper;
    private ProdiMapper prodiMapper;
    
    @Override
    public StudentModel selectStudent (String npm)
    {
        log.info ("select student with npm {}", npm);
        return studentMapper.selectStudent (npm);
    }
    
    @Override
    public ProdiModel selectProdi (Integer id_prodi)
    {
        log.info ("select prodi name with id {}", id_prodi);
        return studentMapper.selectProdi(id_prodi);
    }
    
    @Override
    public FakultasModel selectFakultas (Integer id_fakultas)
    {
    	log.info ("select fakultas with id {}", id_fakultas);
        return studentMapper.selectFakultas(id_fakultas);
    }
    
    @Override
    public void addStudent (StudentModel mahasiswa)
    {
        studentMapper.addStudent (mahasiswa);
    }
    
    @Override
    public String selectNpm (String npm)
    {
    	log.info("select NPM like (to calculate)");
    	return studentMapper.selectNpm(npm);
    }  
      
    @Override
    public Integer countKelulusan (String tahun, int prodi)
    {
    	log.info("Get Jumlah Kelulusan dari tahun "+tahun+" dan prodi "+prodi);
    	return studentMapper.countKelulusan(tahun, prodi);
    }  
    
    @Override
    public Integer countJumlahMahasiswa (String tahun, int prodi)
    {
    	log.info("Get Jumlah Mahasiswa dari tahun "+tahun+" dan prodi "+prodi);
    	return studentMapper.countJumlahMahasiswa(tahun, prodi);
    }  
    
    //BELUM KEPAKE
    @Override
    public List<StudentModel> selectAllStudents ()
    {
        log.info ("select all students");
        return studentMapper.selectAllStudents ();
    }
    
    @Override
    public void updateStudent (StudentModel student)
    {
    	log.info("update student dengan npm"+student.getNpm());
    	studentMapper.updateStudent(student);
    }  
    
    @Override
    public void updateStudentNpm (String npm_baru, String npm_lama)
    {
    	log.info("npm student "+npm_lama+" updated becomes "+npm_baru);
    	studentMapper.updateStudentNpm(npm_baru, npm_lama);
    }
    
    @Override
    public List<StudentModel> selectAllStudentsByProdi(String id_prodi)
    {
    	log.info("select student with id_prodi "+id_prodi);
    	return studentMapper.selectAllStudentsByProdi(id_prodi);
    }  
    
    @Override
    public StudentModel selectMudaMahasiswa(String tahun_masuk, String id_prodi)
    {
    	return studentMapper.selectMudaMahasiswa(tahun_masuk, id_prodi);
    }  
    
    @Override
    public StudentModel selectTuaMahasiswa(String tahun_masuk, String id_prodi)
    {
    	return studentMapper.selectMudaMahasiswa(tahun_masuk, id_prodi);
    }  
}