package com.digiSchool.digiSchool.academic.evaluation.model;
import com.digiSchool.digiSchool.Exceptionconfig.model.TenantEntity;
import com.digiSchool.digiSchool.academic.pedagogique.model.Bulletin;
import com.digiSchool.digiSchool.user.model.Eleve;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Note extends TenantEntity{
	 @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long idResultat;

	    private Double note;
	    private String appreciation;

	    @ManyToOne
	    @JoinColumn(name = "evaluation_id")
	    private Evaluation evaluation;

	    @ManyToOne
	    @JoinColumn(name = "eleve_id")
	    private Eleve eleve;
	    
	    @ManyToOne
	    @JoinColumn(name = "bulletin_id")
	    private Bulletin bulletin;

		public Double getNote() {
			return note;
		}

		public void setNote(Double note) {
			this.note = note;
		}

		public String getAppreciation() {
			return appreciation;
		}

		public void setAppreciation(String appreciation) {
			this.appreciation = appreciation;
		}
}

