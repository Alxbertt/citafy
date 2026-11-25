package com.citafy.citafy.repository;

import com.citafy.citafy.model.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {
    List<Profesional> findByTipoNegocioId(Long tipoNegocioId);
}