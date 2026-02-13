package com.digiSchool.digiSchool.academic.organisation.dto;

public class AdresseDto {

    private Long id;
    private Long quartierId;
    private String quartierNom;
    private String rue;
    private String repere;
    private Double latitude;
    private Double longitude;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getQuartierId() {
		return quartierId;
	}
	public void setQuartierId(Long quartierId) {
		this.quartierId = quartierId;
	}
	public String getQuartierNom() {
		return quartierNom;
	}
	public void setQuartierNom(String quartierNom) {
		this.quartierNom = quartierNom;
	}
	public String getRue() {
		return rue;
	}
	public void setRue(String rue) {
		this.rue = rue;
	}
	public String getRepere() {
		return repere;
	}
	public void setRepere(String repere) {
		this.repere = repere;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}
