package com.gpsit.controle_leads.excecoes;

public class LeadNaoEncontradoExcecao extends RuntimeException {
    public LeadNaoEncontradoExcecao(String mensagem) {
        super(mensagem);
    }
}
