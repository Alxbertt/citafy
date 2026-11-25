package com.citafy.citafy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Profesional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_negocio_id")
    private TipoNegocio tipoNegocio;

    public Profesional() {}

    public Profesional(String nombre, String especialidad, TipoNegocio tipoNegocio) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.tipoNegocio = tipoNegocio;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public TipoNegocio getTipoNegocio() { return tipoNegocio; }
    public void setTipoNegocio(TipoNegocio tipoNegocio) { this.tipoNegocio = tipoNegocio; }
}