package com.dio.santander.bankline.api.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dio.santander.bankline.api.dto.NovaMovimentacao;
import com.dio.santander.bankline.api.model.Correntista;
import com.dio.santander.bankline.api.model.Movimentacao;
import com.dio.santander.bankline.api.model.MovimentacaoTipo;
import com.dio.santander.bankline.api.repository.CorrentistaRepository;
import com.dio.santander.bankline.api.repository.MovimentacaoRepository;

@Service
public class MovimentacaoService {
	
	@Autowired
	private MovimentacaoRepository repository;
	
	@Autowired
	private CorrentistaRepository correntistaRepository;
	
	public void save(NovaMovimentacao novaMovimentacao) {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setDescricao(novaMovimentacao.getDescricao());
		movimentacao.setMovimentacaoTipo(novaMovimentacao.getMovimentacaoTipo());
		movimentacao.setIdConta(novaMovimentacao.getIdConta());
		
		movimentacao.setDataHora(LocalDateTime.now());
		
		Double valor = novaMovimentacao.getValor();
		if (novaMovimentacao.getMovimentacaoTipo() == MovimentacaoTipo.DESPESA) {
			valor = novaMovimentacao.getValor() * -1;
		}
		
		movimentacao.setValor(valor);
		
		// Faz o uso do optional
		Correntista correntista = correntistaRepository.findById(novaMovimentacao.getIdConta()).orElse(null);
		
		if(correntista != null) {
			correntista.getConta().setSaldo(correntista.getConta().getSaldo() + valor);
			correntistaRepository.save(correntista);
		}
		
		repository.save(movimentacao);
		
	}
	
}
