package com.digiSchool.digiSchool.user.model;

import java.time.LocalDate;

import com.digiSchool.digiSchool.Exceptionconfig.model.TenantEntity;
import com.digiSchool.digiSchool.auth.model.EmployeeStatus;
import com.digiSchool.digiSchool.auth.model.RoleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Employee extends TenantEntity{
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "prenom", nullable = false, length = 100)
	    private String firstName;

	    @Column(name = "nom", nullable = false, length = 100)
	    private String lastName;

	    @Enumerated(EnumType.STRING)
	    @Column(name = "role", nullable = false, length = 50)
	    private RoleType role;

	    @Column(name = "telephone", nullable = false, length = 20)
	    private String phone;

	    @Column(name = "grade", length = 50)
	    private String grade;

	    @Column(name = "annees_experience")
	    private Integer experience;

	    @Column(name = "date_embauche", nullable = false)
	    private LocalDate hireDate;

	    @Enumerated(EnumType.STRING)
	    @Column(name = "statut", nullable = false, length = 20)
	    private EmployeeStatus status;

	    @Column(name = "matricule", nullable = false, unique = true, length = 100)
	    private String matricule;
	    
	    @Column(nullable = false, unique = true)
	    private String email;
	    @Column(nullable = false)
	    private String quartier;
	    @Column(nullable = false)
	    private String speciality;
	    @Column(nullable = false)
	    private int salary;

	    @ManyToOne
//	    @JoinColumn(name = "neighborhood_id")
//	    private Neighborhood neighborhood;

	    private String tenant;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}


		public RoleType getRole() {
			return role;
		}

		public void setRole(RoleType role) {
			this.role = role;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getGrade() {
			return grade;
		}

		public void setGrade(String grade) {
			this.grade = grade;
		}

		public Integer getExperience() {
			return experience;
		}

		public void setExperience(Integer experience) {
			this.experience = experience;
		}

		public LocalDate getHireDate() {
			return hireDate;
		}

		public void setHireDate(LocalDate hireDate) {
			this.hireDate = hireDate;
		}

		public EmployeeStatus getStatus() {
			return status;
		}

		public void setStatus(EmployeeStatus status) {
			this.status = status;
		}

		public String getMatricule() {
			return matricule;
		}

		public void setMatricule(String matricule) {
			this.matricule = matricule;
		}

		public String getTenant() {
			return tenant;
		}

		public void setTenant(String tenant) {
			this.tenant = tenant;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getQuartier() {
			return quartier;
		}

		public void setQuartier(String quartier) {
			this.quartier = quartier;
		}

		public String getSpeciality() {
			return speciality;
		}

		public void setSpeciality(String speciality) {
			this.speciality = speciality;
		}

		public int getSalary() {
			return salary;
		}

		public void setSalary(int salary) {
			this.salary = salary;
		}

	
		
	    
}
