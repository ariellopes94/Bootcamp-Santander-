package com.ariellopes.project.bootcamp.exception.model;

public class ObjectNotFoundExcepetion extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public ObjectNotFoundExcepetion(String mensagem) {
		super(mensagem);
	}
	
	public ObjectNotFoundExcepetion(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}
