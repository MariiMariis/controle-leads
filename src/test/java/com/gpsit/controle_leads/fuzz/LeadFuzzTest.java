package com.gpsit.controle_leads.fuzz;

import com.gpsit.controle_leads.dominio.Lead;
import com.gpsit.controle_leads.dominio.StatusLead;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import net.jqwik.api.*;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.StringLength;
import org.junit.jupiter.api.BeforeAll;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LeadFuzzTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Property(tries = 50)
    void testeFuzzNomeInvalido_FalhaValidacao(
            @ForAll @AlphaChars @StringLength(max = 1) String nomeCurto,
            @ForAll("nomesMuitoLongos") String nomeLongo) {

        Lead leadCurto = new Lead(nomeCurto, "valido@email.com", "11999999999", StatusLead.NOVO, "Obs");
        Set<ConstraintViolation<Lead>> violacoesCurto = validator.validate(leadCurto);
        assertThat(violacoesCurto).isNotEmpty();

        Lead leadLongo = new Lead(nomeLongo, "valido@email.com", "11999999999", StatusLead.NOVO, "Obs");
        Set<ConstraintViolation<Lead>> violacoesLongo = validator.validate(leadLongo);
        assertThat(violacoesLongo).isNotEmpty();
    }

    @Property(tries = 50)
    void testeFuzzEmailInvalidoFormatoXSS(
            @ForAll("emailsMaliciosos") String emailMalicioso) {
        
        Lead lead = new Lead("Nome Valido", emailMalicioso, "11999999999", StatusLead.NOVO, "Obs");
        Set<ConstraintViolation<Lead>> violacoes = validator.validate(lead);

        assertThat(violacoes).isNotEmpty();
    }

    @Provide
    Arbitrary<String> nomesMuitoLongos() {
        return Arbitraries.strings().withCharRange('a', 'z').ofMinLength(101).ofMaxLength(500);
    }

    @Provide
    Arbitrary<String> emailsMaliciosos() {
        return Arbitraries.of(
                "<script>alert(1)</script>@email.com",
                "admin' OR 1=1--@email.com",
                "javascript:alert(1)",
                "test@test..com",
                "test@.com",
                "@domain.com"
        );
    }
}
