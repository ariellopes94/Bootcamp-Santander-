package com.ariellopes.project.bootcamp.exception.model;

public class JaExisteStockCriadoHoje extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public JaExisteStockCriadoHoje(String mensagem) {
		super(mensagem);
	}
	
	public JaExisteStockCriadoHoje(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}
