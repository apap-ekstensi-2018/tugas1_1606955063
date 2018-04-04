package com.example.controller;

import static org.mockito.Matchers.matches;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.model.*;
import com.example.service.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class StudentController
{
    @Autowired
    StudentService studentDAO;
    @Autowired
    UniversitasService universitasDAO;
    @Autowired
    FakultasService fakultasDAO;
    @Autowired
    ProdiService prodiDAO;

    @RequestMapping("/")
    public String index ()
    {
        return "index";
    }

    @RequestMapping("/mahasiswa/tambah")
    public String add(Model model)
    {
    	StudentModel mahasiswa = new StudentModel();
    	model.addAttribute ("mahasiswa", mahasiswa);
        return "form-add";
    }


    @RequestMapping("/mahasiswa/tambah/submit")
    public String addSubmit (@ModelAttribute StudentModel mahasiswa, Model model)
    { 	
    	ProdiModel prodi = studentDAO.selectProdi(mahasiswa.getId_prodi());
    	String tahun_masuk = mahasiswa.getTahun_masuk().substring(2);
    	log.info("tahun_masuk " +tahun_masuk);
    	String kode_univ = prodi.getFakultas().getUniversitas().getKode_univ();
    	log.info("kode_univ " +kode_univ);
    	String kode_prodi = prodi.getKode_prodi();
    	log.info("kode_prodi " +kode_prodi);
    	String jalur_masuk = getJalurMasuk(mahasiswa.getJalur_masuk());
    	log.info("jalur_masuk " +mahasiswa.getJalur_masuk());
    	log.info("id jalur_masuk " +jalur_masuk);

    	String npm_sementara = tahun_masuk + kode_univ + kode_prodi + jalur_masuk;
    	log.info("npm_sementara " +npm_sementara);
    	
    	String max_npm = studentDAO.selectNpm(npm_sementara);
    	log.info("max_npm " +max_npm);
    	String nomor_urut = "001";
    	if(max_npm != null) {
    		nomor_urut = getNomorUrut(max_npm);
        	log.info("nomor_urut " +nomor_urut);	
    	}
    	
    	String npm = npm_sementara + nomor_urut;
    	log.info("npm " +npm);
    	
    	mahasiswa.setNpm(npm);
    	
    	if(studentDAO.selectStudent(npm) == null) {
    		studentDAO.addStudent (mahasiswa);
    		model.addAttribute("npm", npm);
            return "success-add";
    	}else {
    		return "already-add";
    	}    
    }
    
    public String getNomorUrut(String max_npm) {
    	String result = "0";
    	String nomor_urut = max_npm.substring(9);
    	String new_nomor_urut = String.valueOf(Integer.parseInt(nomor_urut) + 1) ;
    	if(new_nomor_urut.length()==1) {
    		result = "00"+new_nomor_urut;
    	}else if(new_nomor_urut.length()==2) {
    		result = "0"+new_nomor_urut;
    	}else {
    		result = new_nomor_urut;
    	}
    	return result;
    }
    
    public String getJalurMasuk(String jalur_masuk) {
    	String id = "0";
    	if(jalur_masuk.equals("Undangan Olimpiade")) {
    		id = "53";
    	}else if(jalur_masuk.equals("Undangan Reguler/SNMPTN")){
    		id = "54";
    	}else if(jalur_masuk.equals("Undangan Paralel/PPKB")) {
    		id = "55";
    	}else if(jalur_masuk.equals("Ujian Tulis Bersama/SBMPTN")) {
    		id = "57";
    	}else if(jalur_masuk.equals("Ujian Tulis Mandiri")) {
    		id = "62";
    	}
    	return id;
    }
    
    @RequestMapping("/mahasiswa")
	public String viewByNPM(Model model, @RequestParam(value="npm", required=true)String npm) {
		StudentModel mahasiswa = studentDAO.selectStudent(npm);
		if (mahasiswa != null) {
            model.addAttribute ("mahasiswa", mahasiswa);
            return "view";
        } else {
            model.addAttribute ("npm", npm);
            return "not-found";
        }
	}
    
    @RequestMapping("/kelulusan")
	public String searchKelulusan() {
		return "search-lulus";
	}
    
    @RequestMapping(value="/kelulusan/submit", method=RequestMethod.GET)
	public String viewLulus(Model model, @RequestParam(value="thn", required=true)String thn,
										 @RequestParam(value="id_prodi", required=true)int id_prodi) {
    	Integer jumlah_mahasiswa = studentDAO.countJumlahMahasiswa(thn, id_prodi);
    	if(jumlah_mahasiswa != 0) {
    		Integer total_lulus = studentDAO.countKelulusan(thn, id_prodi);
        	Integer persentase = (total_lulus * 100) / jumlah_mahasiswa;
        	log.info("test");
        	ProdiModel prodi = studentDAO.selectProdi(id_prodi);
        	
    		model.addAttribute ("prodi", prodi);
        	model.addAttribute ("persentase", persentase);
        	model.addAttribute ("total_lulus", total_lulus);
        	model.addAttribute ("jumlah_mahasiswa", jumlah_mahasiswa);
        	model.addAttribute ("thn", thn);
            return "view-lulus";
    	}else {
    		return "not-found";
    	}	
	}
    
    @RequestMapping("/mahasiswa/viewall")
    public String view (Model model)
    {
        List<StudentModel> students = studentDAO.selectAllStudents ();
        model.addAttribute ("students", students);
        return "viewall";
    }
   
    @RequestMapping("/mahasiswa/ubah/{npm}")
    public String update (Model model, @PathVariable(value = "npm") String npm)
    {
    	StudentModel student = studentDAO.selectStudent(npm);
        if (student != null) {
        	log.info(String.valueOf(student.getId_prodi()));
        	model.addAttribute ("student", student);
        	model.addAttribute ("npm", npm);
            return "form-update";
        } else {
            model.addAttribute ("npm", npm);
            return "not-found";
        }
    }
    
    @RequestMapping(value="/mahasiswa/ubah/submit", method= RequestMethod.POST)
    public String updateSubmit (@ModelAttribute StudentModel student)
    {
    	ProdiModel prodi = studentDAO.selectProdi(student.getId_prodi());
    	String tahun_masuk = student.getTahun_masuk().substring(2);
    	log.info("tahun_masuk " +tahun_masuk);
    	String kode_univ = prodi.getFakultas().getUniversitas().getKode_univ();
    	log.info("kode_univ " +kode_univ);
    	String kode_prodi = prodi.getKode_prodi();
    	log.info("kode_prodi " +kode_prodi);
    	String jalur_masuk = getJalurMasuk(student.getJalur_masuk());
    	log.info("jalur_masuk " +student.getJalur_masuk());
    	log.info("id jalur_masuk " +jalur_masuk);

    	String npm_sementara = tahun_masuk + kode_univ + kode_prodi + jalur_masuk;
    	log.info("npm_sementara " +npm_sementara);
    	
    	String max_npm = studentDAO.selectNpm(npm_sementara);
    	log.info("max_npm " +max_npm);
    	String nomor_urut = "001";
    	if(max_npm != null) {
    		nomor_urut = getNomorUrut(max_npm);
        	log.info("nomor_urut " +nomor_urut);	
    	}
    	
    	String npm = npm_sementara + nomor_urut;
    	log.info("npm " +npm);
    	
    	StudentModel student_temp = studentDAO.selectStudent(student.getNpm());
    	log.info("npm student temp " +student_temp);
    	if(student.getNpm() != npm) {
    		studentDAO.updateStudentNpm(npm, student.getNpm());
    	}
    	student.setNpm(npm);
        studentDAO.updateStudent(student);
        return "success-update";
    }
    
    
    @RequestMapping("/mahasiswa/cariUniv")
	public String cariMahasiswa(Model model)
    {
    	log.info("start cari mahasiswa");
    	List<UniversitasModel> allUniversitas = universitasDAO.selectAllUniversitas();	
    	model.addAttribute("allUniversitas", allUniversitas);
		return "search-universitas";
	}
    
    @RequestMapping("/mahasiswa/cariFakultas")
  	public String cariBasedUniversitas(Model model, @RequestParam(value = "universitas", required = false) String idUniversitas)
    {
      	List<UniversitasModel> allUniversitas = universitasDAO.selectAllUniversitas();
      	for(UniversitasModel universitasSelected: allUniversitas) {
      		if (universitasSelected.getId().equals(idUniversitas)){
      			model.addAttribute("universitasSelected", universitasSelected);
      		}
      	}
      	model.addAttribute("idUniversitas", idUniversitas);
      	
      	List<FakultasModel> allFakultas = fakultasDAO.selectAllFakultas(idUniversitas);
      	model.addAttribute("allFakultas", allFakultas);
		return "search-fakultas";
  	}
    
    @RequestMapping("/mahasiswa/cariProdi")
  	public String cariBasedFakultas(Model model, 
  				@RequestParam(value = "universitas", required = false) String idUniversitas,
  				@RequestParam(value = "fakultas", required = false) String idFakultas)
      {
      	List<UniversitasModel> allUniversitas = universitasDAO.selectAllUniversitas();
      	for(UniversitasModel universitasSelected: allUniversitas) {
      		if (universitasSelected.getId().equals(idUniversitas)){
      			model.addAttribute("universitasSelected", universitasSelected);
      		}
      	}
      	model.addAttribute("idUniversitas", idUniversitas);
      	
      	List<FakultasModel> allFakultas = fakultasDAO.selectAllFakultas(idUniversitas);
      	for(FakultasModel fakultasSelected: allFakultas) {
      		if (fakultasSelected.getId().equals(idFakultas)){
      			model.addAttribute("fakultasSelected", fakultasSelected);
      		}
      	}
      	model.addAttribute("idFakultas", idFakultas);
      	
      	List<ProdiModel> allProdi = prodiDAO.selectAllProdi(idFakultas);
      	model.addAttribute("allProdi", allProdi);
		return "search-prodi";
  	}
    
    @RequestMapping("/mahasiswa/cariMahasiswaMuda")
  	public String cariMahasiswaMuda(Model model,
  			@RequestParam(value = "id_prodi", required = false) String id_prodi, 
    		@RequestParam(value = "thn", required = false) String thn) 
    {
    	StudentModel mahasiswaTermuda = studentDAO.selectMudaMahasiswa(thn, id_prodi);
    	StudentModel mahasiswa = studentDAO.selectStudent(mahasiswaTermuda.getNpm());
    	
    	model.addAttribute("mahasiswa", mahasiswa);	
    	return "view-muda";
    }
    
    @RequestMapping("/mahasiswa/cariMuda")
    public String viewCariTua() {
    	return "search-usia";
    }
    
    @RequestMapping("/mahasiswa/cariTua")
    public String viewCariMuda() {
    	return "search-tua";
    }
    
    @RequestMapping("/mahasiswa/cariMahasiswaTua")
  	public String cariMahasiswaTua(Model model,
  			@RequestParam(value = "id_prodi", required = false) String id_prodi, 
    		@RequestParam(value = "thn", required = false) String thn) 
    {
    	StudentModel mahasiswaTertua = studentDAO.selectTuaMahasiswa(thn, id_prodi);
    	StudentModel mahasiswa = studentDAO.selectStudent(mahasiswaTertua.getNpm());
    	
    	model.addAttribute("mahasiswa", mahasiswa);	
    	return "view-muda";
    }
    
    @RequestMapping("/mahasiswa/cari")
  	public String cariMahasiswa(Model model, 
  				@RequestParam(value = "universitas", required = false) String idUniversitas,
  				@RequestParam(value = "fakultas", required = false) String idFakultas,
  				@RequestParam(value = "prodi", required = false) String idProdi)
      {
    	List<UniversitasModel> allUniversitas = universitasDAO.selectAllUniversitas();
      	for(UniversitasModel universitasSelected: allUniversitas) {
      		if (universitasSelected.getId().equals(idUniversitas)){
      			model.addAttribute("universitasSelected", universitasSelected);
      		}
      	}
      	model.addAttribute("idUniversitas", idUniversitas);
      	
      	List<FakultasModel> allFakultas = fakultasDAO.selectAllFakultas(idUniversitas);
      	for(FakultasModel fakultasSelected: allFakultas) {
      		if (fakultasSelected.getId().equals(idFakultas)){
      			model.addAttribute("fakultasSelected", fakultasSelected);
      		}
      	}
      	model.addAttribute("idFakultas", idFakultas);
      	
      	List<ProdiModel> allProdi = prodiDAO.selectAllProdi(idFakultas);
      	for(ProdiModel prodiSelected: allProdi) {
      		if (prodiSelected.getId().equals(idProdi)){
      			model.addAttribute("prodiSelected", prodiSelected);
      		}
      	}
      	model.addAttribute("idProdi", idProdi);
      	
      	List<StudentModel> mahasiswa = studentDAO.selectAllStudentsByProdi(idProdi);
      	String size = String.valueOf(mahasiswa.size());
      	log.info("size "+size);
      	model.addAttribute("mahasiswa", mahasiswa);
		return "search-mahasiswa";
  	}
    
    //*****************************
    //BELUM DIPAKE
    //*****************************
    @RequestMapping("/student/view")
    public String view (Model model, @RequestParam(value = "npm", required = false) String npm)
    {
        StudentModel student = studentDAO.selectStudent (npm);
        if (student != null) {
            model.addAttribute ("student", student);
            return "view";
        } else {
            model.addAttribute ("npm", npm);
            return "not-found";
        }
    }
}