package com.gpsit.controle_leads.controlador;

import com.gpsit.controle_leads.configuracao.FalhaSimuladaConfig;
import com.gpsit.controle_leads.dominio.Lead;
import com.gpsit.controle_leads.dominio.StatusLead;
import com.gpsit.controle_leads.repositorio.LeadRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LeadControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LeadRepositorio repositorio;

    @Autowired
    private FalhaSimuladaConfig falhaConfig;

    @BeforeEach
    void setUp() {
        repositorio.deleteAll();
        falhaConfig.setSimularErroBanco(false);
        falhaConfig.setSimularLentidao(false);
    }

    @Test
    void listar_Sucesso_RetornaView() throws Exception {
        mockMvc.perform(get("/leads"))
                .andExpect(status().isOk())
                .andExpect(view().name("lista_leads"))
                .andExpect(model().attributeExists("leads"));
    }

    @Test
    void salvar_Sucesso_Redireciona() throws Exception {
        mockMvc.perform(post("/leads/salvar")
                        .param("nome", "Carlos Silva")
                        .param("email", "carlos@email.com")
                        .param("telefone", "11999999999")
                        .param("status", "NOVO"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/leads"))
                .andExpect(flash().attributeExists("mensagemSucesso"));
    }

    @Test
    void salvar_FalhaValidacao_RetornaFormulario() throws Exception {
        mockMvc.perform(post("/leads/salvar")
                        .param("nome", "")
                        .param("email", "email-invalido")
                        .param("telefone", "123")
                        .param("status", "NOVO"))
                .andExpect(status().isOk())
                .andExpect(view().name("form_lead"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("lead", "nome", "email", "telefone"));
    }

    @Test
    void salvar_FalhaEmailDuplicado_RetornaFormularioComErro() throws Exception {
        Lead lead = new Lead("Admin", "admin@email.com", "11000000000", StatusLead.CONTATADO, "");
        repositorio.save(lead);

        mockMvc.perform(post("/leads/salvar")
                        .param("nome", "Outro Admin")
                        .param("email", "admin@email.com")
                        .param("telefone", "11222222222")
                        .param("status", "NOVO"))
                .andExpect(status().isOk())
                .andExpect(view().name("form_lead"))
                .andExpect(model().attributeExists("mensagemErro"));
    }

    @Test
    void simulacaoFalhaBanco_ApresentaPaginaErroGraceful() throws Exception {
        falhaConfig.setSimularErroBanco(true);

        mockMvc.perform(get("/leads"))
                .andExpect(status().isOk())
                .andExpect(view().name("erro"))
                .andExpect(model().attributeExists("erro", "mensagem", "status"));
    }
}
