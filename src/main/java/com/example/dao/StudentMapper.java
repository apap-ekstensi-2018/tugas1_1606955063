package com.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.model.*;

@Mapper
public interface StudentMapper
{
    @Select("select * from mahasiswa where npm = #{npm}")
    @Results( value = {
    		@Result(property = "id", column = "id"),
            @Result(property = "npm", column = "npm"),
            @Result(property = "nama", column = "nama"),
            @Result(property = "tempat_lahir", column = "tempat_lahir"),
            @Result(property = "tanggal_lahir", column = "tanggal_lahir"),
            @Result(property = "jenis_kelamin", column = "jenis_kelamin"),
            @Result(property = "agama", column = "agama"),
            @Result(property = "golongan_darah", column = "golongan_darah"),
            @Result(property = "status", column = "status"),
            @Result(property = "tahun_masuk", column = "tahun_masuk"),
            @Result(property = "jalur_masuk", column = "jalur_masuk"),
            @Result(property = "id_prodi", column = "id_prodi"),
            @Result(property = "prodi", column = "id_prodi", one = @One(select = "selectProdi"))
    })
    StudentModel selectStudent (@Param("npm") String npm);
    
    
    @Select("select * from program_studi where id = #{id_prodi}")
    @Results( value = {
    		@Result(property = "id", column = "id"),
            @Result(property = "kode_prodi", column = "kode_prodi"),
            @Result(property = "nama_prodi", column = "nama_prodi"),
            @Result(property = "fakultas", column = "id_fakultas", one = @One(select = "selectFakultas"))
    })
    ProdiModel selectProdi (@Param("id_prodi") Integer id_prodi);
    
    
    @Select("select * from fakultas where id = #{id_fakultas}")
    @Results( value = {
    		@Result(property = "id", column = "id"),
            @Result(property = "kode_fakultas", column = "kode_fakultas"),
            @Result(property = "nama_fakultas", column = "nama_fakultas"),
            @Result(property = "universitas", column = "id_univ", one = @One(select = "selectUniversitas"))
    })
    FakultasModel selectFakultas (@Param("id_fakultas") Integer id_fakultas);
    
    
    @Select("select * from universitas where id = #{id_universitas}")
    @Results( value = {
    		@Result(property = "id", column = "id"),
            @Result(property = "kode_univ", column = "kode_univ"),
            @Result(property = "nama_univ", column = "nama_univ")
    })
    UniversitasModel selectUniversitas (@Param("id_universitas") Integer id_universitas);
    
    @Select("select max(npm) from mahasiswa where npm like CONCAT(#{npm},'%') limit 1")
    String selectNpm (@Param("npm") String npm);
    
    @Insert("INSERT INTO mahasiswa (npm, nama, tempat_lahir, tanggal_lahir, jenis_kelamin, agama, golongan_darah, status, tahun_masuk, jalur_masuk, id_prodi) "
    		+ "VALUES (#{npm}, #{nama}, #{tempat_lahir}, #{tanggal_lahir}, #{jenis_kelamin}, #{agama}, #{golongan_darah}, #{status}, #{tahun_masuk}, #{jalur_masuk}, #{id_prodi})")
    void addStudent (StudentModel mahasiswa);
    
    @Select("select count(*) from mahasiswa where tahun_masuk = #{thn} and id_prodi = #{prodi} and status = 'Lulus'")
    Integer countKelulusan (@Param("thn") String thn, @Param("prodi") int prodi);
    
    @Select("select count(*) from mahasiswa where tahun_masuk = #{thn} and id_prodi = #{prodi}")
    Integer countJumlahMahasiswa (@Param("thn") String thn, @Param("prodi") int prodi);
    
    @Select("select * from mahasiswa")
    List<StudentModel> selectAllStudents ();
    
    @Update("UPDATE mahasiswa SET npm = #{npm}, nama =  #{nama}, tempat_lahir = #{tempat_lahir}, tanggal_lahir = #{tanggal_lahir}, jenis_kelamin = #{jenis_kelamin}, agama = #{agama}, golongan_darah = #{golongan_darah}, status = #{status}, tahun_masuk = #{tahun_masuk}, jalur_masuk = #{jalur_masuk}, id_prodi = #{id_prodi} WHERE npm = #{npm}")
    void updateStudent (StudentModel student);
    
    @Update("UPDATE mahasiswa SET npm = #{npm_baru} WHERE npm = #{npm_lama}")
    void updateStudentNpm (@Param("npm_baru") String npm_baru, @Param("npm_lama") String npm_lama);
    
    @Select("select * from mahasiswa where id_prodi = #{id_prodi}")
    List<StudentModel> selectAllStudentsByProdi(@Param("id_prodi") String id_prodi);
    
    @Select("select * from mahasiswa where tahun_masuk = #{tahun_masuk} and id_prodi = #{id_prodi} ORDER BY tanggal_lahir DESC LIMIT 1")
    StudentModel selectMudaMahasiswa(@Param("tahun_masuk") String tahun_masuk, @Param("id_prodi") String id_prodi);
    
    @Select("select * from mahasiswa where tahun_masuk = #{tahun_masuk} and id_prodi = #{id_prodi} ORDER BY tanggal_lahir ASC LIMIT 1")
    StudentModel selectTuaMahasiswa(@Param("tahun_masuk") String tahun_masuk, @Param("id_prodi") String id_prodi);
}