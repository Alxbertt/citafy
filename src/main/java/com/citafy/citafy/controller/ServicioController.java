package com.citafy.citafy.controller;

import com.citafy.citafy.model.Servicio;
import com.citafy.citafy.model.TipoNegocio;
import com.citafy.citafy.repository.ServicioRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    private final ServicioRepository repository;

    public ServicioController(ServicioRepository repository) {
        this.repository = repository;
    }

    @InitBinder("servicio")
    public void configurarCampos(WebDataBinder binder) {
        binder.setAllowedFields("nombre", "duracionMinutos", "precio");
    }

    @GetMapping
    public String listar(
            @ModelAttribute(value = "negocioActivo", binding = false)
            TipoNegocio negocio, Model model) {

        model.addAttribute("servicios",
                repository.findByTipoNegocioId(negocio.getId()));

        return "servicios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("servicio", new Servicio());
        return "servicios/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id,
            @ModelAttribute(value = "negocioActivo", binding = false)
            TipoNegocio negocio, Model model) {

        model.addAttribute("servicio", buscar(id, negocio));
        model.addAttribute("idEdicion", id);

        return "servicios/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("servicio") Servicio servicio,
            BindingResult resultado,
            @RequestParam(required = false) Long id,
            @ModelAttribute(value = "negocioActivo", binding = false)
            TipoNegocio negocio, Model model) {

        Servicio existente = id == null
                ? new Servicio()
                : buscar(id, negocio);

        if (resultado.hasErrors()) {
            model.addAttribute("idEdicion", id);
            return "servicios/formulario";
        }

        existente.setNombre(servicio.getNombre().trim());
        existente.setDuracionMinutos(servicio.getDuracionMinutos());
        existente.setPrecio(servicio.getPrecio());
        existente.setTipoNegocio(negocio);

        repository.save(existente);

        return "redirect:/servicios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
            @ModelAttribute(value = "negocioActivo", binding = false)
            TipoNegocio negocio, RedirectAttributes atributos) {

        Servicio servicio = buscar(id, negocio);

        try {
            repository.delete(servicio);
            atributos.addFlashAttribute("mensaje",
                    "Servicio eliminado correctamente.");
        } catch (DataIntegrityViolationException e) {
            atributos.addFlashAttribute("error",
                    "No se puede eliminar un servicio con citas asociadas.");
        }

        return "redirect:/servicios";
    }

    private Servicio buscar(Long id, TipoNegocio negocio) {
        return repository.findById(id)
                .filter(s -> s.getTipoNegocio().getId()
                        .equals(negocio.getId()))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}