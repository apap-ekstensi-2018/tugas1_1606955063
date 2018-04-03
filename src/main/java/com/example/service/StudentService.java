package com.example.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.model.*;

public interface StudentService
{
    StudentModel selectStudent (String npm);
    
    ProdiModel selectProdi (Integer id_prodi);
    
    FakultasModel selectFakultas (Integer id_fakultas);
    
    String selectNpm (String npm);
    
    Integer countKelulusan (String tahun, int prodi);
    
    Integer countJumlahMahasiswa (String tahun, int prodi);
    
    //BELUM KEPAKE
    List<StudentModel> selectAllStudents ();
    
    void updateStudentNpm (String npm_baru, String npm_lama);
    
    void updateStudent (StudentModel student);

    void addStudent (StudentModel mahasiswa);

    void deleteStudent (String npm);
}