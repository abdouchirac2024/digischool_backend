package com.digiSchool.digiSchool.academic.organisation.dto;

public class VilleDto {

    private Long id;
    private String code;
    private String nom;
    private String codePostal;
    private Long arrondissementId;
    private String arrondissementNom;

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
	public String getCodePostal() {
		return codePostal;
	}
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}
	public Long getArrondissementId() {
		return arrondissementId;
	}
	public void setArrondissementId(Long arrondissementId) {
		this.arrondissementId = arrondissementId;
	}
	public String getArrondissementNom() {
		return arrondissementNom;
	}
	public void setArrondissementNom(String arrondissementNom) {
		this.arrondissementNom = arrondissementNom;
	}

}
