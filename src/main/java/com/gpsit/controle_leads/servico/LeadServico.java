package com.gpsit.controle_leads.servico;

import com.gpsit.controle_leads.configuracao.FalhaSimuladaConfig;
import com.gpsit.controle_leads.dominio.Lead;
import com.gpsit.controle_leads.excecoes.LeadNaoEncontradoExcecao;
import com.gpsit.controle_leads.excecoes.RegraNegocioExcecao;
import com.gpsit.controle_leads.repositorio.LeadRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeadServico {

    private final LeadRepositorio repositorio;
    private final FalhaSimuladaConfig falhaConfig;

    public LeadServico(LeadRepositorio repositorio, FalhaSimuladaConfig falhaConfig) {
        this.repositorio = repositorio;
        this.falhaConfig = falhaConfig;
    }

    public List<Lead> listarTodos() {
        simularFalhasCasoConfigurado();
        return repositorio.findAll();
    }

    public Lead buscarPorId(Long id) {
        simularFalhasCasoConfigurado();
        return repositorio.findById(id)
                .orElseThrow(() -> new LeadNaoEncontradoExcecao("Lead não encontrado com o ID: " + id));
    }

    @Transactional
    public Lead salvar(Lead lead) {
        simularFalhasCasoConfigurado();
        validarEmailUnico(lead);
        return repositorio.save(lead);
    }

    @Transactional
    public void excluir(Long id) {
        simularFalhasCasoConfigurado();
        Lead lead = buscarPorId(id);
        repositorio.delete(lead);
    }

    private void validarEmailUnico(Lead lead) {
        if (lead.getId() == null) {
            if (repositorio.findByEmail(lead.getEmail()).isPresent()) {
                throw new RegraNegocioExcecao("Já existe um lead cadastrado com este e-mail.");
            }
        } else {
            if (repositorio.existsByEmailAndIdNot(lead.getEmail(), lead.getId())) {
                throw new RegraNegocioExcecao("Já existe um lead cadastrado com este e-mail.");
            }
        }
    }

    private void simularFalhasCasoConfigurado() {
        if (falhaConfig.isSimularLentidao()) {
            try {
                Thread.sleep(falhaConfig.getDelayMs());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (falhaConfig.isSimularErroBanco()) {
            throw new RuntimeException("Falha simulada de conexão com o banco de dados.");
        }
    }
}
