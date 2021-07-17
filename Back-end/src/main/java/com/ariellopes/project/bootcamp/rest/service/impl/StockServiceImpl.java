package com.ariellopes.project.bootcamp.rest.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ariellopes.project.bootcamp.exception.model.JaExisteStockCriadoHoje;
import com.ariellopes.project.bootcamp.exception.model.ObjectNotFoundExcepetion;
import com.ariellopes.project.bootcamp.persistence.entity.StockEntity;
import com.ariellopes.project.bootcamp.persistence.repository.StockRepository;
import com.ariellopes.project.bootcamp.rest.service.StockService;
import com.ariellopes.project.bootcamp.util.MessageUtils;

import javassist.tools.rmi.ObjectNotFoundException;

@Service
public class StockServiceImpl implements StockService {

	@Autowired
	private StockRepository repository;
	
	@Override
	@Transactional
	public StockEntity save(StockEntity stockEntity) {
		verrificarExistenciaPorNomeEdate(stockEntity);
		return repository.save(stockEntity);
	}

	@Override
	@Transactional
	public StockEntity update(StockEntity stockEntity) {
	   verrificarExistencia(stockEntity.getId());
	   verrificarExistenciaUpdatePorNomeAndDateAndId(stockEntity);
	   return repository.save(stockEntity);
	}
	
	@Override
	@Transactional(readOnly =true)
	public Optional<StockEntity> findById(Long id) {
		verrificarExistencia(id);
		return repository.findById(id);
	}

	@Override
	@Transactional(readOnly =true)
	public Page<StockEntity> findAll(Pageable pageable) {
		
		Page<StockEntity> stock = repository.findAll(pageable);
		
		if(stock.getTotalElements() == 0) {
			throw new ObjectNotFoundExcepetion(MessageUtils.NAO_EXISTE_STOCK);
		}
		return stock;
	}

	@Override
	@Transactional(readOnly =true)
	public List<StockEntity> findByName(String name) {
		 List<StockEntity> retorno = repository.findTop5ByNameContainingIgnoreCase(name);
		return retorno;
	}
	
	@Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<StockEntity> findByCurrentDate() {
         Optional<List<StockEntity>> stockDeHoje = repository.findByCurnrentDate();
      
         if(stockDeHoje.get().size() == 0) {
        	 throw new ObjectNotFoundExcepetion(MessageUtils.NAO_EXISTE_STOCK);
         }
         
         return stockDeHoje.get();
	    }

	private void verrificarExistenciaPorNomeEdate(StockEntity stock) {
		 boolean existeStock = repository.existsByNameContainingIgnoreCaseAndDate(stock.getName(), stock.getDate());
		 if(existeStock) {
			 throw new JaExisteStockCriadoHoje(MessageUtils.JA_EXISTE_STOCK_PARA_HOJE);
		 }
	}
	
	private void verrificarExistenciaUpdatePorNomeAndDateAndId(StockEntity stock) {
		 Optional<StockEntity> existeStock = repository.findByStockUpdate(stock.getName(), stock.getDate(), stock.getId());
		 
		 if(existeStock.isPresent()) {
			 throw new JaExisteStockCriadoHoje(MessageUtils.JA_EXISTE_STOCK_PARA_HOJE);
		 }
	}
	
	private void verrificarExistencia(Long id) {
		 boolean existeStock = repository.existsById(id);
		 if(!existeStock) {
			 throw new ObjectNotFoundExcepetion(MessageUtils.NAO_EXISTE_STOCK);
		 }
	}
	
}
