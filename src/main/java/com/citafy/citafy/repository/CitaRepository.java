package com.citafy.citafy.repository;

import com.citafy.citafy.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitaRepository extends JpaRepository<Cita, Long> { }