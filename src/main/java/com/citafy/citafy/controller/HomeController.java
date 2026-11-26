package com.citafy.citafy.controller;

import com.citafy.citafy.repository.TipoNegocioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class HomeController {

    private final TipoNegocioRepository tipoNegocioRepository;

    public HomeController(TipoNegocioRepository tipoNegocioRepository) {
        this.tipoNegocioRepository = tipoNegocioRepository;
    }

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @PostMapping("/negocio/seleccionar")
    public String seleccionarNegocio(@RequestParam("tipoId") Long tipoId,
                                     HttpSession sesion) {
        if (!tipoNegocioRepository.existsById(tipoId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El tipo de negocio no existe"
            );
        }

        sesion.setAttribute(NegocioVista.TIPO_NEGOCIO_SESION, tipoId);
        return "redirect:/";
    }
}