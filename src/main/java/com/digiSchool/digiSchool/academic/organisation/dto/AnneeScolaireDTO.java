package com.digiSchool.digiSchool.academic.organisation.dto;

import java.time.LocalDate;
import java.util.List;

public class AnneeScolaireDTO {
	 private String label;
	    private LocalDate from;
	    private LocalDate to;
	    private Boolean current;

	    private List<PeriodeDTO> periods;
	    private List<ExamenDTO> exams;
	    private List<VacanceDTO> holidays;
	    private List<JourFerieDTO> publicHolidays;
	    
	    private String tenantId; // <-- ajouté
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public LocalDate getFrom() {
			return from;
		}
		public void setFrom(LocalDate from) {
			this.from = from;
		}
		public LocalDate getTo() {
			return to;
		}
		public void setTo(LocalDate to) {
			this.to = to;
		}
		public Boolean getCurrent() {
			return current;
		}
		public void setCurrent(Boolean current) {
			this.current = current;
		}
		public List<PeriodeDTO> getPeriods() {
			return periods;
		}
		public void setPeriods(List<PeriodeDTO> periods) {
			this.periods = periods;
		}
		public List<ExamenDTO> getExams() {
			return exams;
		}
		public void setExams(List<ExamenDTO> exams) {
			this.exams = exams;
		}
		public List<VacanceDTO> getHolidays() {
			return holidays;
		}
		public void setHolidays(List<VacanceDTO> holidays) {
			this.holidays = holidays;
		}
		public List<JourFerieDTO> getPublicHolidays() {
			return publicHolidays;
		}
		public void setPublicHolidays(List<JourFerieDTO> publicHolidays) {
			this.publicHolidays = publicHolidays;
		}
		public String getTenantId() {
			return tenantId;
		}
		public void setTenantId(String tenantId) {
			this.tenantId = tenantId;
		}
	
	    
	    
}
