package com.citafy.citafy.controller;

import com.citafy.citafy.model.TipoNegocio;
import com.citafy.citafy.repository.TipoNegocioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class NegocioVista {

    static final String TIPO_NEGOCIO_SESION = "tipoNegocioId";

    private final TipoNegocioRepository tipoNegocioRepository;

    public NegocioVista(TipoNegocioRepository tipoNegocioRepository) {
        this.tipoNegocioRepository = tipoNegocioRepository;
    }

    @ModelAttribute
    public void agregarNegociosAlModelo(HttpSession sesion, Model modelo) {
        List<TipoNegocio> tipos = tipoNegocioRepository.findAll(Sort.by("id"));
        Long idGuardado = (Long) sesion.getAttribute(TIPO_NEGOCIO_SESION);

        TipoNegocio negocioActivo = tipos.stream()
                .filter(tipo -> tipo.getId().equals(idGuardado))
                .findFirst()
                .or(() -> tipos.stream()
                        .filter(tipo -> "Barbería".equals(tipo.getNombre()))
                        .findFirst())
                .orElse(tipos.isEmpty() ? null : tipos.getFirst());

        modelo.addAttribute("tiposNegocio", tipos);
        modelo.addAttribute("negocioActivo", negocioActivo);
    }
}