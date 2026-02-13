package com.digiSchool.digiSchool.academic.organisation.dto;

public class ArrondissementDto {

    private Long id;
    private String code;
    private String nom;
    private Long departementId;
    private String departementNom;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Long getDepartementId() {
		return departementId;
	}
	public void setDepartementId(Long departementId) {
		this.departementId = departementId;
	}
	public String getDepartementNom() {
		return departementNom;
	}
	public void setDepartementNom(String departementNom) {
		this.departementNom = departementNom;
	}

}
