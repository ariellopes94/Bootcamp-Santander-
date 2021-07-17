package com.ariellopes.project.bootcamp.rest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ariellopes.project.bootcamp.persistence.entity.StockEntity;

public interface StockService {
	
	public StockEntity save(StockEntity stockEntity);
	
	public StockEntity update(StockEntity stockEntity);
	
	public Optional<StockEntity> findById(Long id);
	
	public Page<StockEntity> findAll(Pageable pageable);
	
	public List<StockEntity> findByName(String name);
	
	public List<StockEntity> findByCurrentDate();
	
}
