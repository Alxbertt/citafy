package com.citafy.citafy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class TipoNegocio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String nombre;              // Barbería, Clínica, Salón

    @NotBlank
    private String etiquetaCliente;     // Cliente / Paciente

    @NotBlank
    private String etiquetaProfesional; // Barbero / Doctor / Estilista

    public TipoNegocio() {}

    public TipoNegocio(String nombre, String etiquetaCliente, String etiquetaProfesional) {
        this.nombre = nombre;
        this.etiquetaCliente = etiquetaCliente;
        this.etiquetaProfesional = etiquetaProfesional;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEtiquetaCliente() { return etiquetaCliente; }
    public void setEtiquetaCliente(String etiquetaCliente) { this.etiquetaCliente = etiquetaCliente; }
    public String getEtiquetaProfesional() { return etiquetaProfesional; }
    public void setEtiquetaProfesional(String etiquetaProfesional) { this.etiquetaProfesional = etiquetaProfesional; }
}