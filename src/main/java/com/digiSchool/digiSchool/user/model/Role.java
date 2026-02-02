package com.digiSchool.digiSchool.user.model;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Role {
    @Id @GeneratedValue
    private Long idRole;
    private String nomRole;

    @OneToMany(mappedBy="role")
    private List<Utilisateur> utilisateurs;

	public String getNomRole() {
		return nomRole;
	}

	public void setNomRole(String nomRole) {
		this.nomRole = nomRole;
	}
}
