package com.gpsit.controle_leads.controlador;

import com.gpsit.controle_leads.dominio.Lead;
import com.gpsit.controle_leads.dominio.StatusLead;
import com.gpsit.controle_leads.excecoes.RegraNegocioExcecao;
import com.gpsit.controle_leads.servico.LeadServico;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/leads")
public class LeadControlador {

    private final LeadServico servico;

    public LeadControlador(LeadServico servico) {
        this.servico = servico;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("leads", servico.listarTodos());
        return "lista_leads";
    }

    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("lead", new Lead());
        model.addAttribute("statusValues", StatusLead.values());
        return "form_lead";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("lead") Lead lead, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("statusValues", StatusLead.values());
            return "form_lead"; // Fail early (Validation messages returned)
        }

        try {
            servico.salvar(lead);
            attributes.addFlashAttribute("mensagemSucesso", "Lead salvo com sucesso!");
        } catch (RegraNegocioExcecao ex) {
            model.addAttribute("statusValues", StatusLead.values());
            model.addAttribute("mensagemErro", ex.getMessage());
            return "form_lead"; // Tratamento de erro de banco amigável sem usar a página de erro catastrófica
        }
        
        return "redirect:/leads";
    }

    @GetMapping("/{id}/editar")
    public String editarFormulario(@PathVariable Long id, Model model) {
        Lead lead = servico.buscarPorId(id);
        model.addAttribute("lead", lead);
        model.addAttribute("statusValues", StatusLead.values());
        return "form_lead";
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
        servico.excluir(id);
        attributes.addFlashAttribute("mensagemSucesso", "Lead removido com sucesso!");
        return "redirect:/leads";
    }
}
