package com.digiSchool.digiSchool.academic.organisation.dto;

import java.time.LocalDate;

public class JourFerieDTO {

    private String name;
    private LocalDate date;
    
    public JourFerieDTO() {
    	
    }
    
	public JourFerieDTO(String name, LocalDate date) {
		super();
		this.name = name;
		this.date = date;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}

    // Getters & Setters
    
    
}

