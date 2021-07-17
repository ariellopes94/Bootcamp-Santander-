package com.ariellopes.project.bootcamp.rest.controller.domain.dto.stock;

import java.time.LocalDate;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewStockDTO {
	
	@NotNull(message = "Nome não pode ser Nulo")
	@NotBlank(message = "Campo nome não pode ser vazio")
	private String name;
	
	@NotNull(message = "Preço não pode ser Nulo")
	@DecimalMin(value = "0.00")
	@Digits(integer = 6, fraction = 2)
	private Double price;
	
	@NotNull(message = "Data não pode ser Nulo")
	@JsonFormat(pattern = "dd/MM/uuuu")
	@JsonDeserialize(using = LocalDateDeserializer.class)  
	@JsonSerialize(using = LocalDateSerializer.class)  
	private LocalDate date;
	
	@NotNull(message = "Variação não pode ser Nulo")
	@Digits(integer = 3, fraction = 2)
	private Double variation;

}
