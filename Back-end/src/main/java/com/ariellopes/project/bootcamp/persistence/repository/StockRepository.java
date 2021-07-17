package com.ariellopes.project.bootcamp.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ariellopes.project.bootcamp.persistence.entity.StockEntity;

public interface StockRepository extends JpaRepository<StockEntity, Long> {
	
	List<StockEntity> findTop5ByNameContainingIgnoreCase (String name);

	boolean existsByNameContainingIgnoreCaseAndDate(String name, LocalDate date);
	
	@Query("SELECT stock " +
		   "FROM StockEntity stock " + 
		   "WHERE stock.name = :name " + 
		   "AND stock.date = :date " + 
		   "AND stock.id != :id" )
	Optional<StockEntity> findByStockUpdate(String name, LocalDate date, Long id);
	
	@Query("SELECT stock FROM StockEntity stock WHERE stock.date = CURRENT_DATE ")
    Optional<List<StockEntity>> findByCurnrentDate();
	
	//boolean existsByNameContainingIgnoreCaseAndDate(Long id);
	
	//List<StockEntity> findByNameContainingIgnoreCaseOrSobenomeContainingIgnoreCase (String nome, String sobrenome);
	
}
