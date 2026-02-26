package com.digiSchool.digiSchool.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.model.UserStatus;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByTelephone(String telephone);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByEmailAndTenantId(String email, String tenantId);

    boolean existsByEmail(String email);

    Optional<User> findByPasswordResetToken(String token);

    List<User> findByTenantId(String tenantId);

    List<User> findByTenantIdAndRole(String tenantId, RoleType role);

    List<User> findByTenantIdAndStatus(String tenantId, UserStatus status);

    @Query("SELECT u FROM User u WHERE u.tenantId = :tenantId AND u.role IN :roles")
    List<User> findByTenantIdAndRoles(@Param("tenantId") String tenantId, @Param("roles") List<RoleType> roles);

    long countByTenantId(String tenantId);

    long countByTenantIdAndRole(String tenantId, RoleType role);
}
