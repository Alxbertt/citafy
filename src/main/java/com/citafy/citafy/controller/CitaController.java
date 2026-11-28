package com.citafy.citafy.controller;

import com.citafy.citafy.model.Cita;
import com.citafy.citafy.model.EstadoCita;
import com.citafy.citafy.model.TipoNegocio;
import com.citafy.citafy.repository.CitaRepository;
import com.citafy.citafy.repository.ClienteRepository;
import com.citafy.citafy.repository.ProfesionalRepository;
import com.citafy.citafy.repository.ServicioRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.util.Comparator;

@Controller
@RequestMapping("/citas")
public class CitaController {

    private final CitaRepository citaRepository;
    private final ClienteRepository clienteRepository;
    private final ProfesionalRepository profesionalRepository;
    private final ServicioRepository servicioRepository;

    public CitaController(CitaRepository citaRepository,
                          ClienteRepository clienteRepository,
                          ProfesionalRepository profesionalRepository,
                          ServicioRepository servicioRepository) {
        this.citaRepository = citaRepository;
        this.clienteRepository = clienteRepository;
        this.profesionalRepository = profesionalRepository;
        this.servicioRepository = servicioRepository;
    }

    @InitBinder("cita")
    public void configurarCampos(WebDataBinder binder) {
        binder.setAllowedFields("cliente", "profesional", "servicio", "fechaHora", "notas");

        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String texto) {
                setValue(texto == null || texto.isBlank() ? null : LocalDateTime.parse(texto));
            }

            @Override
            public String getAsText() {
                Object valor = getValue();
                return valor == null ? "" : valor.toString();
            }
        });
    }

    @GetMapping
    public String listar(
            @ModelAttribute(value = "negocioActivo", binding = false)
            TipoNegocio negocio, Model model) {

        model.addAttribute("citas", citaRepository.findAll().stream()
                .filter(c -> c.getServicio().getTipoNegocio().getId()
                        .equals(negocio.getId()))
                .sorted(Comparator.comparing(Cita::getFechaHora))
                .toList());

        return "citas/lista";
    }

    @GetMapping("/nueva")
    public String nueva(
            @ModelAttribute(value = "negocioActivo", binding = false)
            TipoNegocio negocio, Model model) {

        model.addAttribute("cita", new Cita());
        cargarListas(model, negocio);
        return "citas/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("cita") Cita cita,
            BindingResult resultado,
            @ModelAttribute(value = "negocioActivo", binding = false)
            TipoNegocio negocio,
            Model model, RedirectAttributes atributos) {

        if (cita.getProfesional() != null
                && !cita.getProfesional().getTipoNegocio().getId().equals(negocio.getId())) {
            resultado.rejectValue("profesional", "negocio",
                    "El profesional no pertenece al negocio activo");
        }
        if (cita.getServicio() != null
                && !cita.getServicio().getTipoNegocio().getId().equals(negocio.getId())) {
            resultado.rejectValue("servicio", "negocio",
                    "El servicio no pertenece al negocio activo");
        }

        if (resultado.hasErrors()) {
            cargarListas(model, negocio);
            return "citas/formulario";
        }

        cita.setEstado(EstadoCita.PENDIENTE);
        citaRepository.save(cita);
        atributos.addFlashAttribute("mensaje", "Cita agendada correctamente.");

        return "redirect:/citas";
    }

    private void cargarListas(Model model, TipoNegocio negocio) {
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("profesionales",
                profesionalRepository.findByTipoNegocioId(negocio.getId()));
        model.addAttribute("servicios",
                servicioRepository.findByTipoNegocioId(negocio.getId()));
    }
}