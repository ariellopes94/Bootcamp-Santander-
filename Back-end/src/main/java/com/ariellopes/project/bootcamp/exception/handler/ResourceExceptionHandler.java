package com.ariellopes.project.bootcamp.exception.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ariellopes.project.bootcamp.exception.handler.validation.StandardError;
import com.ariellopes.project.bootcamp.exception.handler.validation.ValidationError;
import com.ariellopes.project.bootcamp.exception.model.JaExisteStockCriadoHoje;
import com.ariellopes.project.bootcamp.exception.model.ObjectNotFoundExcepetion;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, 
																HttpServletRequest request) {

		ValidationError erro = new ValidationError();

		for (FieldError x : e.getBindingResult().getFieldErrors()) {
			erro.addError(x.getField(), x.getDefaultMessage());
		}

		erro.setTimestamp(System.currentTimeMillis());
		erro.setStatus(422);
		erro.setError("Erro de Validação");
		erro.setMessage(e.getMessage());
		erro.setPath(request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}
	
	@ExceptionHandler(ObjectNotFoundExcepetion.class)
	public ResponseEntity<StandardError> objcetNotFoundException(ObjectNotFoundExcepetion e,
			                                                            HttpServletRequest request){
		StandardError erro = new StandardError();
		
		erro.setTimestamp(System.currentTimeMillis());
		erro.setStatus(400);
		erro.setError("Não existe stock");
		erro.setMessage(e.getMessage());
		erro.setPath(request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}
	
	@ExceptionHandler(JaExisteStockCriadoHoje.class)
	public ResponseEntity<StandardError> objcetNotFoundException(JaExisteStockCriadoHoje e,
			                                                            HttpServletRequest request){
		StandardError erro = new StandardError();
		
		erro.setTimestamp(System.currentTimeMillis());
		erro.setStatus(400);
		erro.setError("Já Existe");
		erro.setMessage(e.getMessage());
		erro.setPath(request.getRequestURI());
	
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
	}
}
