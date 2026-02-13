package com.digiSchool.digiSchool.Exceptionconfig.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
public class Departement {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 10)
    private String code;

    private String nom;

    private String chefLieu;

    @ManyToOne
    private Region region;

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

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public String getChefLieu() {
		return chefLieu;
	}

	public void setChefLieu(String chefLieu) {
		this.chefLieu = chefLieu;
	}

}

