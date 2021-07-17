package com.ariellopes.project.bootcamp.rest.controller;

import java.net.URI;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ariellopes.project.bootcamp.persistence.entity.StockEntity;
import com.ariellopes.project.bootcamp.rest.controller.domain.dto.stock.NewStockDTO;
import com.ariellopes.project.bootcamp.rest.controller.domain.dto.stock.StockDTO;
import com.ariellopes.project.bootcamp.rest.controller.domain.dto.stock.UpdateStockDTO;
import com.ariellopes.project.bootcamp.rest.service.StockService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/stocks")
public class StockController {

	@Autowired
	private ModelMapper stockMapper;

	@Autowired
	private StockService service;
	

	// @PostMapping(value = "buscar-por/{descricao}")
	@GetMapping(value = "buscar-por/{descricao}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<StockDTO>> findByName(@PathVariable String descricao) {
		List<StockEntity> listaStock = service.findByName(descricao);
		
		List<StockDTO> stockDto = stockMapper
							.map(listaStock,
							         new TypeToken<List<StockDTO>>()
								     {}.getType());
	
	return ResponseEntity.ok(stockDto);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StockDTO> save(@RequestBody @Validated NewStockDTO dto) {
		StockEntity stockSalvo = service.save(stockMapper.map(dto, StockEntity.class) );
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(stockSalvo.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StockDTO> update(@RequestBody @Validated UpdateStockDTO stock){
		service.update(stockMapper.map(stock, StockEntity.class));
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(value = "/buscar-todos",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<StockDTO>> findAll(
	
		@PageableDefault(size = 10, sort = "id", direction = Direction.DESC) Pageable pageable) {
			Page<StockEntity> stockAllpages = service.findAll(pageable);

			List<StockEntity> stockModel =  stockAllpages.getContent();
		
			List<StockDTO>  stockDto = stockMapper.map(stockModel, new TypeToken<List<StockDTO>>() {}.getType());
			
			Page<StockDTO> stockModalPage = new PageImpl<>(stockDto , pageable,
					stockAllpages.getTotalElements());
			
			return ResponseEntity.ok(stockModalPage);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StockDTO> findById(@PathVariable Long id){
		StockEntity stock = service.findById(id).get();
		return ResponseEntity.ok().body(stockMapper.map(stock, StockDTO.class));
	}
	
    @GetMapping(value = "/today", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockDTO>> findByCurrentDate() {
    	List<StockEntity> stockEntity = service.findByCurrentDate();
    	List<StockDTO>  stockDto = stockMapper.map(stockEntity, new TypeToken<List<StockDTO>>() {}.getType());
        return ResponseEntity.ok(stockDto);
    }

}
