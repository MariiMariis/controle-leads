package com.gpsit.controle_leads.repositorio;

import com.gpsit.controle_leads.dominio.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeadRepositorio extends JpaRepository<Lead, Long> {
    Optional<Lead> findByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
}
