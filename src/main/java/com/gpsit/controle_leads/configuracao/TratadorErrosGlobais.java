package com.gpsit.controle_leads.configuracao;

import com.gpsit.controle_leads.excecoes.LeadNaoEncontradoExcecao;
import com.gpsit.controle_leads.excecoes.RegraNegocioExcecao;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class TratadorErrosGlobais {

    @ExceptionHandler(LeadNaoEncontradoExcecao.class)
    public ModelAndView tratarLeadNaoEncontrado(LeadNaoEncontradoExcecao ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("erro");
        mav.addObject("timestamp", new Date());
        mav.addObject("status", 404);
        mav.addObject("erro", "Recurso não encontrado");
        mav.addObject("mensagem", ex.getMessage());
        mav.addObject("caminho", request.getRequestURI());
        return mav;
    }

    @ExceptionHandler(RegraNegocioExcecao.class)
    public ModelAndView tratarRegraNegocio(RegraNegocioExcecao ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("erro");
        mav.addObject("timestamp", new Date());
        mav.addObject("status", 400);
        mav.addObject("erro", "Regra de Negócio Violada");
        mav.addObject("mensagem", ex.getMessage());
        mav.addObject("caminho", request.getRequestURI());
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView tratarErroInesperado(Exception ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("erro");
        mav.addObject("timestamp", new Date());
        mav.addObject("status", 500);
        mav.addObject("erro", "Erro Interno no Servidor");
        mav.addObject("mensagem", "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde. Se o problema persistir, contate o suporte.");
        // Removido ex.getMessage() no fallback para evitar vazar informações do banco ou sistema (fail-gracefully)
        mav.addObject("caminho", request.getRequestURI());
        return mav;
    }
}
