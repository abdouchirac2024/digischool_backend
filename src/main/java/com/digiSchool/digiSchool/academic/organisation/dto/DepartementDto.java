package com.digiSchool.digiSchool.academic.organisation.dto;

public class DepartementDto {

    private Long id;
    private String code;
    private String nom;
    private String chefLieu;
    private Long regionId;
    private String regionNom;

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
	public String getChefLieu() {
		return chefLieu;
	}
	public void setChefLieu(String chefLieu) {
		this.chefLieu = chefLieu;
	}
	public Long getRegionId() {
		return regionId;
	}
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
	public String getRegionNom() {
		return regionNom;
	}
	public void setRegionNom(String regionNom) {
		this.regionNom = regionNom;
	}

}
