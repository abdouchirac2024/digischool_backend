package com.digiSchool.digiSchool.academic.organisation.dto;

public class QuartierDto {

    private Long id;
    private String code;
    private String nom;
    private String description;
    private Long villeId;
    private String villeNom;

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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getVilleId() {
		return villeId;
	}
	public void setVilleId(Long villeId) {
		this.villeId = villeId;
	}
	public String getVilleNom() {
		return villeNom;
	}
	public void setVilleNom(String villeNom) {
		this.villeNom = villeNom;
	}

}
