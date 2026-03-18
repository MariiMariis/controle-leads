package com.gpsit.controle_leads.configuracao;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.falhas")
public class FalhaSimuladaConfig {
    
    private boolean simularLentidao;
    private boolean simularErroBanco;
    private long delayMs;

    public boolean isSimularLentidao() {
        return simularLentidao;
    }

    public void setSimularLentidao(boolean simularLentidao) {
        this.simularLentidao = simularLentidao;
    }

    public boolean isSimularErroBanco() {
        return simularErroBanco;
    }

    public void setSimularErroBanco(boolean simularErroBanco) {
        this.simularErroBanco = simularErroBanco;
    }

    public long getDelayMs() {
        return delayMs;
    }

    public void setDelayMs(long delayMs) {
        this.delayMs = delayMs;
    }
}
