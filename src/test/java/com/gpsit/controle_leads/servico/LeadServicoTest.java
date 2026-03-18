package com.gpsit.controle_leads.servico;

import com.gpsit.controle_leads.configuracao.FalhaSimuladaConfig;
import com.gpsit.controle_leads.dominio.Lead;
import com.gpsit.controle_leads.dominio.StatusLead;
import com.gpsit.controle_leads.excecoes.LeadNaoEncontradoExcecao;
import com.gpsit.controle_leads.excecoes.RegraNegocioExcecao;
import com.gpsit.controle_leads.repositorio.LeadRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LeadServicoTest {

    @Mock
    private LeadRepositorio repositorio;

    @Mock
    private FalhaSimuladaConfig falhaConfig;

    @InjectMocks
    private LeadServico servico;

    private Lead lead;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lead = new Lead("João Silva", "joao@email.com", "11999999999", StatusLead.NOVO, "Teste");
        lead.setId(1L);

        when(falhaConfig.isSimularLentidao()).thenReturn(false);
        when(falhaConfig.isSimularErroBanco()).thenReturn(false);
    }

    @Test
    void buscarPorId_Sucesso() {
        when(repositorio.findById(1L)).thenReturn(Optional.of(lead));

        Lead resultado = servico.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        verify(repositorio, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_Falha_LeadNaoEncontrado() {
        when(repositorio.findById(2L)).thenReturn(Optional.empty());

        assertThrows(LeadNaoEncontradoExcecao.class, () -> servico.buscarPorId(2L));
    }

    @Test
    void salvar_Sucesso_NovoLead() {
        Lead novoLead = new Lead("Maria", "maria@email.com", "11888888888", StatusLead.CONTATADO, "");
        when(repositorio.findByEmail(novoLead.getEmail())).thenReturn(Optional.empty());
        when(repositorio.save(any(Lead.class))).thenReturn(novoLead);

        Lead resultado = servico.salvar(novoLead);

        assertNotNull(resultado);
        verify(repositorio, times(1)).save(novoLead);
    }

    @Test
    void salvar_Falha_EmailJaExiste() {
        Lead novoLead = new Lead("Maria", "joao@email.com", "11888888888", StatusLead.CONTATADO, "");
        when(repositorio.findByEmail(novoLead.getEmail())).thenReturn(Optional.of(lead));

        RegraNegocioExcecao excecao = assertThrows(RegraNegocioExcecao.class, () -> servico.salvar(novoLead));
        assertEquals("Já existe um lead cadastrado com este e-mail.", excecao.getMessage());
        verify(repositorio, never()).save(any(Lead.class));
    }

    @Test
    void simulacaoFalhaBanco_DeveLancarExcecao() {
        when(falhaConfig.isSimularErroBanco()).thenReturn(true);

        RuntimeException excecao = assertThrows(RuntimeException.class, () -> servico.listarTodos());
        assertEquals("Falha simulada de conexão com o banco de dados.", excecao.getMessage());
    }
}
