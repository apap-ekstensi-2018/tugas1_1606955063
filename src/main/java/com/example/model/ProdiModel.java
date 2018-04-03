package com.example.model;

import java.sql.Date;
import com.example.model.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdiModel {
	private String id;
	private String kode_prodi;
	private String nama_prodi;
	private FakultasModel fakultas;
}