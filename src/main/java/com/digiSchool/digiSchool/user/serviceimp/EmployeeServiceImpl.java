package com.digiSchool.digiSchool.user.serviceimp;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digiSchool.digiSchool.Exceptionconfig.service.TenantContext;
import com.digiSchool.digiSchool.auth.model.EmployeeStatus;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.model.UserStatus;
import com.digiSchool.digiSchool.auth.repository.UserRepository;
import com.digiSchool.digiSchool.user.dto.EmployeeDTO;
import com.digiSchool.digiSchool.user.model.Employee;
import com.digiSchool.digiSchool.user.repository.EmployeeRepository;
import com.digiSchool.digiSchool.user.service.EmployeeService;

import jakarta.persistence.EntityManager;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Session;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;
    private final UserRepository userRepository;

    // 🔹 Injection par constructeur
    public EmployeeServiceImpl(EmployeeRepository repository,
                               PasswordEncoder passwordEncoder,
                               UserRepository userRepository,
                               EntityManager entityManager) {
        this.employeeRepository = repository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
        this.userRepository = userRepository;
    }

    @Override
    public List<EmployeeDTO> getAll() {
    	String tenant = TenantContext.getTenant();

        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("tenantFilter")
               .setParameter("tenant", tenant);

        return employeeRepository.findAll()
                         .stream()
                         .map(this::mapToDTO)
                         .toList();
    }

    @Override
    public EmployeeDTO getById(Long id) {

        String tenant = TenantContext.getTenant();

        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("tenantFilter")
               .setParameter("tenant", tenant);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee introuvable"));

        return mapToDTO(employee);
    }
    
    @Override
    @Transactional
    public Employee create(EmployeeDTO dto) {
        // Génération du matricule
//        long count = employeeRepository.countByRoleAndHireDate(dto.getHireDate()) + 1;
        String matricule = dto.getRole() + "_" + dto.getHireDate() + "_";
        String tenant = TenantContext.getTenant();
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("tenantFilter")
               .setParameter("tenant", tenant);
        
        

        // Création de l'employé
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setRole(dto.getRole());
        employee.setPhone(dto.getPhone());
        employee.setGrade(dto.getGrade());
        employee.setExperience(dto.getExperience());
        employee.setHireDate(dto.getHireDate());
        employee.setStatus(dto.getStatus());
        employee.setMatricule(matricule);
        employee.setTenant(tenant);
        employee.setQuartier(dto.getNeighborhood());
        employee.setEmail(dto.getEmail());
        employee.setSpeciality(dto.getSpeciality());
        employee.setSalary(dto.getSalary());
        employeeRepository.save(employee);

        // Création de l'utilisateur
        User user = new User();
        user.setEmail(dto.getEmail());
        String rawPassword = generateRandomPassword();
        
        System.out.println("==>>>> Mot de passe <<<====="+rawPassword);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(dto.getRole());
        user.setEmployee(employee);
        user.setNom(dto.getFirstName());
        user.setPrenom(dto.getLastName());
        user.setStatus(UserStatus.ACTIVE);
        user.setTenantId(tenant);
        userRepository.save(user);

        return employee;
    }

    @Override
    @Transactional
    public EmployeeDTO update(Long id, EmployeeDTO dto) {

        String tenant = TenantContext.getTenant();

        // 🔹 Activation du filtre multi-tenant
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("tenantFilter")
               .setParameter("tenant", tenant);

        // 🔹 Recherche simple par ID (le filtre fait le travail)
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé introuvable"));

        // 🔹 Sauvegarde anciennes valeurs pour historique
        EmployeeStatus ancienStatus = employee.getStatus();
        Integer ancienneExperience = employee.getExperience();
        String ancienGrade = employee.getGrade();

        // 🔹 Exemple règle métier
        if ((ancienStatus == EmployeeStatus.RETRAITE
                || ancienStatus == EmployeeStatus.LICENCIE)
                && dto.getStatus() == EmployeeStatus.ACTIF) {

            throw new RuntimeException(
                    "Un employé retraité ou licencié ne peut pas redevenir actif.");
        }

        // 🔹 Mise à jour des champs
        if (dto.getFirstName() != null)
            employee.setFirstName(dto.getFirstName());

        if (dto.getLastName() != null)
            employee.setLastName(dto.getLastName());

        if (dto.getPhone() != null)
            employee.setPhone(dto.getPhone());

        if (dto.getGrade() != null)
            employee.setGrade(dto.getGrade());

        if (dto.getExperience() != null)
            employee.setExperience(dto.getExperience());

        if (dto.getStatus() != null)
            employee.setStatus(dto.getStatus());

//        employee.setUpdatedBy(dto.getUpdatedBy());

        // 🔹 Sécurité tenant
        if (employee.getTenant() == null) {
            employee.setTenant(tenant);
        }

        Employee saved = employeeRepository.save(employee);

        // 🔹 Historique (optionnel)
//        saveHistory(
//                saved,
//                "UPDATE",
//                ancienStatus,
//                dto.getStatus(),
//                ancienneExperience,
//                dto.getExperience(),
//                ancienGrade,
//                dto.getGrade(),
//                dto.getUpdatedBy() != null ? dto.getUpdatedBy() : 1L,
//                "Mise à jour des informations employé"
//        );

        return mapToDTO(saved);
    }
    @Override
    public void delete(Long id) {
//        String tenant = TenantContext.getTenant();
//        Employee employee = employeeRepository.findByIdAndTenant(id, tenant)
//                .orElseThrow(() -> new RuntimeException("Employee not found"));
//        employeeRepository.delete(employee);
    }

    // ================= UTILS =================
    private EmployeeDTO mapToDTO(Employee e) {
        if (e == null) return null;

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(e.getId());
        dto.setFirstName(e.getFirstName());
        dto.setLastName(e.getLastName());
        dto.setRole(e.getRole());
        dto.setPhone(e.getPhone());
        dto.setGrade(e.getGrade());
        dto.setExperience(e.getExperience());
        dto.setHireDate(e.getHireDate());
        dto.setStatus(e.getStatus());
        dto.setEmail(e.getEmail());
        dto.setNeighborhood(e.getQuartier());
        dto.setMatricule(e.getMatricule());
        dto.setSpeciality(e.getSpeciality());
        dto.setSalary(e.getSalary());
//        dto.setTenant(e.getTenant());
        return dto;
    }

    private String formatRole(String role) {
        return role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase();
    }

    private String random3Digits() {
        return String.format("%03d", new Random().nextInt(1000));
    }

    // 🔹 Méthode pour mapper des collections futures (exemple)
    /*
    private void mapCollections(EmployeeDTO dto, Employee entity) {
        if (entity.getCertificats() == null) entity.setCertificats(new ArrayList<>());
        if (dto.getCertificats() != null) {
            entity.getCertificats().clear();
            dto.getCertificats().forEach(c -> {
                Certificat cert = new Certificat();
                cert.setNom(c.getNom());
                cert.setDate(c.getDate());
                cert.setEmployee(entity);
                entity.getCertificats().add(cert);
            });
        }
    }
    */
    
    private String generateRandomPassword() {
    	 return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}