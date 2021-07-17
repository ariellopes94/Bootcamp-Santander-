package com.ariellopes.project.bootcamp.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

import com.ariellopes.project.bootcamp.persistence.entity.StockEntity;
import com.ariellopes.project.bootcamp.rest.controller.domain.dto.stock.NewStockDTO;
import com.ariellopes.project.bootcamp.rest.controller.domain.dto.stock.UpdateStockDTO;

public class StockCreator {
	

	// DateFormat formato = new SimpleDateFormat("yyyy-mm-dd");
	
	
	static DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/uuuu")
		    .withResolverStyle(ResolverStyle.STRICT); // para não aceitar datas inválidas
		//static LocalDate data = LocalDate.parse("20/01/2020", parser);

	public static NewStockDTO newStock() {
		return NewStockDTO.builder()
				.name("Magazine Luiza")
				.price(100D)
				.date(LocalDate.parse("20/01/2020", parser))
				.variation(10D)
			.build();
	}
	
	public static StockEntity stockSalvo() {
		return StockEntity.builder()
				.id(55l)
				.name("Ponto Frio")
				.price(200D)
				.date(LocalDate.parse("20/01/2020", parser))
				.variation(5D)
			.build();
	}
	
	public static StockEntity stockParaSalvarSemID() {
		return StockEntity.builder()
				.name("Ponto Frio")
				.price(200D)
				.date(LocalDate.parse("20/01/2020", parser))
				.variation(5D)
			.build();
	}
	
	public static NewStockDTO createValidClient() {
		return NewStockDTO.builder()
				.name("Magazine Luiza")
				.price(100D)
				.date(LocalDate.parse("20/01/2020", parser))
				.variation(10D)
			.build();
	}
	
	public static UpdateStockDTO updateClient() {
		return UpdateStockDTO.builder()
				.id(2l)
				.name("Ponto Frio")
				.price(200D)
				.date(LocalDate.parse("20/01/2021", parser))
				.variation(5D)
			.build();
	}
	
	public static List<StockEntity> listaDeStocks(){
		
		List<StockEntity> listaDeStock = new ArrayList<>();
		
		listaDeStock.add(new StockEntity(1l, "Ponto Frio", 200d, LocalDate.parse("20/01/2020", parser), 5d));
		listaDeStock.add(new StockEntity(2l, "Magazine Luiza", 200d, LocalDate.parse("20/01/2020", parser), 10d));
		listaDeStock.add(new StockEntity(3l, "Santader", 50d, LocalDate.parse("20/01/2020", parser), 15d));
		listaDeStock.add(new StockEntity(4l, "Loja Americanas", 250d, LocalDate.parse("20/01/2020", parser), 35d));
		
		return listaDeStock;
	}
	
}
