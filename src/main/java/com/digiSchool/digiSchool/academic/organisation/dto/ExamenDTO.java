package com.digiSchool.digiSchool.academic.organisation.dto;

import java.time.LocalDate;

public class ExamenDTO {

    private String name;
    private LocalDate start;
    private LocalDate end;
    
    public ExamenDTO() {
        // constructeur vide pour frameworks
    }

    public ExamenDTO(String name, LocalDate start, LocalDate end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getStart() {
		return start;
	}
	public void setStart(LocalDate start) {
		this.start = start;
	}
	public LocalDate getEnd() {
		return end;
	}
	public void setEnd(LocalDate end) {
		this.end = end;
	}

    // Getters & Setters
    
    
}

