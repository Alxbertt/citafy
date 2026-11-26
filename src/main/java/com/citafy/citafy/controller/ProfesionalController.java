
package com.citafy.citafy.controller;

import com.citafy.citafy.model.Profesional;
import com.citafy.citafy.model.TipoNegocio;
import com.citafy.citafy.repository.ProfesionalRepository;
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
@RequestMapping("/profesionales")
public class ProfesionalController {

    private final ProfesionalRepository repository;

    public ProfesionalController(ProfesionalRepository repository) {
        this.repository = repository;
    }

    @InitBinder("profesional")
    public void configurarCampos(WebDataBinder binder) {
        binder.setAllowedFields("nombre", "especialidad");
    }

    @GetMapping
    public String listar(
            @ModelAttribute(value = "negocioActivo", binding = false)
            TipoNegocio negocio, Model model) {

        model.addAttribute("profesionales",
                repository.findByTipoNegocioId(negocio.getId()));

        return "profesionales/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("profesional", new Profesional());
        return "profesionales/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("profesional") Profesional profesional,
            BindingResult resultado,
            @RequestParam(required = false) Long id,
            @ModelAttribute(value = "negocioActivo", binding = false)
            TipoNegocio negocio, Model model) {

        Profesional existente = id == null
                ? new Profesional()
                : buscar(id, negocio);

        if (resultado.hasErrors()) {
            model.addAttribute("idEdicion", id);
            return "profesionales/formulario";
        }

        existente.setNombre(profesional.getNombre().trim());
        existente.setEspecialidad(profesional.getEspecialidad().trim());
        existente.setTipoNegocio(negocio);

        repository.save(existente);

        return "redirect:/profesionales";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id,
            @ModelAttribute(value = "negocioActivo", binding = false)
            TipoNegocio negocio, Model model) {

        model.addAttribute("profesional", buscar(id, negocio));
        model.addAttribute("idEdicion", id);

        return "profesionales/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
            @ModelAttribute(value = "negocioActivo", binding = false)
            TipoNegocio negocio, RedirectAttributes atributos) {

        Profesional profesional = buscar(id, negocio);

        try {
            repository.delete(profesional);
            atributos.addFlashAttribute("mensaje",
                    "Profesional eliminado correctamente.");
        } catch (DataIntegrityViolationException e) {
            atributos.addFlashAttribute("error",
                    "No se puede eliminar un profesional con citas asociadas.");
        }

        return "redirect:/profesionales";
    }

    private Profesional buscar(Long id, TipoNegocio negocio) {
        return repository.findById(id)
                .filter(p -> p.getTipoNegocio().getId()
                        .equals(negocio.getId()))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
