package com.citafy.citafy.config;

import com.citafy.citafy.model.*;
import com.citafy.citafy.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DatosIniciales implements CommandLineRunner {

    private final TipoNegocioRepository tipoRepo;
    private final ServicioRepository servicioRepo;
    private final ProfesionalRepository profesionalRepo;

    public DatosIniciales(TipoNegocioRepository tipoRepo,
                          ServicioRepository servicioRepo,
                          ProfesionalRepository profesionalRepo) {
        this.tipoRepo = tipoRepo;
        this.servicioRepo = servicioRepo;
        this.profesionalRepo = profesionalRepo;
    }

    @Override
    public void run(String... args) {
        if (tipoRepo.count() > 0) {
            return;
        }

        TipoNegocio barberia = tipoRepo.save(new TipoNegocio("Barbería", "Cliente", "Barbero"));
        TipoNegocio clinica  = tipoRepo.save(new TipoNegocio("Clínica", "Paciente", "Doctor"));
        TipoNegocio salon    = tipoRepo.save(new TipoNegocio("Salón", "Cliente", "Estilista"));

        // Barbería
        servicioRepo.save(new Servicio("Corte de cabello", 30, new BigDecimal("300"), barberia));
        servicioRepo.save(new Servicio("Arreglo de barba", 20, new BigDecimal("200"), barberia));
        servicioRepo.save(new Servicio("Corte y barba", 50, new BigDecimal("450"), barberia));
        profesionalRepo.save(new Profesional("Carlos Pérez", "Cortes clásicos", barberia));
        profesionalRepo.save(new Profesional("Luis Gómez", "Barbas y degradados", barberia));

        // Clínica
        servicioRepo.save(new Servicio("Consulta general", 30, new BigDecimal("800"), clinica));
        servicioRepo.save(new Servicio("Chequeo completo", 60, new BigDecimal("1500"), clinica));
        servicioRepo.save(new Servicio("Control de seguimiento", 20, new BigDecimal("500"), clinica));
        profesionalRepo.save(new Profesional("Dra. María Santos", "Medicina general", clinica));
        profesionalRepo.save(new Profesional("Dr. Juan Rodríguez", "Pediatría", clinica));

        // Salón
        servicioRepo.save(new Servicio("Peinado", 45, new BigDecimal("600"), salon));
        servicioRepo.save(new Servicio("Manicure", 40, new BigDecimal("400"), salon));
        servicioRepo.save(new Servicio("Tinte", 90, new BigDecimal("1200"), salon));
        profesionalRepo.save(new Profesional("Ana Martínez", "Colorimetría", salon));
        profesionalRepo.save(new Profesional("Laura Díaz", "Uñas y peinados", salon));
    }
}