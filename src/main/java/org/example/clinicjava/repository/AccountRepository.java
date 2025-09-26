package org.example.clinicjava.repository;

import org.example.clinicjava.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
    @Query("SELECT a FROM Account a")
    Page<Account> searchList(Pageable pageable);

    @Query(value = "SELECT COUNT(a) > 0 FROM Account a WHERE a.isActive = 1 " +
            "AND (:email IS NULL OR (a.email = :email)) " +
            "AND (:accountId IS NULL OR (a.accountId <> :accountId)) ")
    Boolean findByEmailAndAccountId(@Param("email") String email, @Param("accountId") Long accountId);

    @Query(value = "SELECT a FROM Account a" +
                   " WHERE a.isActive = 1 AND a.accountId = :accountId AND a.roleId = :roleId ")
    Optional<Account> findByIdAndRoleDoctor(@Param("accountId") Long accountId, @Param("roleId") Long roleId);

    @Query(value = "SELECT a.accountId FROM Account a" +
            " WHERE a.isActive = 1 AND a.roleId = :roleId ")
    List<Long> findByRoleId(@Param("roleId") Long roleId);
}
