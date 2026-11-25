package com.citafy.citafy.repository;

import com.citafy.citafy.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    List<Servicio> findByTipoNegocioId(Long tipoNegocioId);
}