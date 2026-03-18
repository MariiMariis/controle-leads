package com.gpsit.controle_leads.dominio;

public enum StatusLead {
    NOVO("Novo"),
    CONTATADO("Contatado"),
    QUALIFICADO("Qualificado"),
    CONVERTIDO("Convertido"),
    PERDIDO("Perdido");

    private String descricao;

    StatusLead(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
