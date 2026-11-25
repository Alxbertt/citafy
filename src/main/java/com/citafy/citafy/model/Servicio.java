package com.citafy.citafy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private int duracionMinutos;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    private BigDecimal precio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_negocio_id")
    private TipoNegocio tipoNegocio;

    public Servicio() {}

    public Servicio(String nombre, int duracionMinutos, BigDecimal precio, TipoNegocio tipoNegocio) {
        this.nombre = nombre;
        this.duracionMinutos = duracionMinutos;
        this.precio = precio;
        this.tipoNegocio = tipoNegocio;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public TipoNegocio getTipoNegocio() { return tipoNegocio; }
    public void setTipoNegocio(TipoNegocio tipoNegocio) { this.tipoNegocio = tipoNegocio; }
}