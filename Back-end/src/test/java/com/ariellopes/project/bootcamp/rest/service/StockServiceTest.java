package com.ariellopes.project.bootcamp.rest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ariellopes.project.bootcamp.exception.model.ObjectNotFoundExcepetion;
import com.ariellopes.project.bootcamp.persistence.entity.StockEntity;
import com.ariellopes.project.bootcamp.persistence.repository.StockRepository;
import com.ariellopes.project.bootcamp.rest.service.impl.StockServiceImpl;
import com.ariellopes.project.bootcamp.util.StockCreator;

@ExtendWith(SpringExtension.class)
public class StockServiceTest {
	
	@InjectMocks
	StockServiceImpl stockService;
	
	@Mock
	private StockRepository stockRepositoryMock;

	@BeforeEach
	void setUp(){
		
		BDDMockito.when(stockRepositoryMock.save(ArgumentMatchers.any(StockEntity.class)))
												.thenReturn(StockCreator.stockSalvo());
		
		Mockito.when(stockRepositoryMock.save(StockCreator.stockParaSalvarSemID()) )
												.thenReturn(StockCreator.stockSalvo());
		
		
		Mockito.when( stockRepositoryMock.existsById(Mockito.anyLong()))
													.thenReturn(true);
		
		Mockito.when( stockRepositoryMock.findById(Mockito .anyLong()))
		 				.thenReturn(Optional.of(StockCreator.stockSalvo()));
		
	}
	
	@Test
    @DisplayName("deve salvar um stock e retornar sucesso")
    void deve_salvar_um_stock_test(){
		

		StockEntity stockSalvo = stockService.save(StockCreator.stockSalvo());
		
		assertThat( stockSalvo.getId() ).isNotNull();
		assertThat( stockSalvo.getId() ).isEqualTo(55L);
		assertThat( stockSalvo.getName() ).isEqualTo("Ponto Frio");
		assertThat( stockSalvo.getPrice() ).isEqualTo(200D);
		assertThat( stockSalvo.getVariation() ).isEqualTo(5D);
    }
	
	@Test
	@DisplayName("Deve buscar Stock por ID")
	public void find_by_id_test() {
		
		Optional<StockEntity>  stockSalvo = stockService.findById(55l);
		
		assertThat( stockSalvo.get().getId() ).isNotNull();
		assertThat( stockSalvo.get().getId() ).isEqualTo(55L);
		assertThat( stockSalvo.get().getName() ).isEqualTo("Ponto Frio");
		assertThat( stockSalvo.get().getPrice() ).isEqualTo(200D);
		assertThat( stockSalvo.get().getVariation() ).isEqualTo(5D);
	}
	
	@Test
    @DisplayName("Deve lançar uma exerção caso não encontre Stock por ID")
    void deve_lancar_object_not_found_excpetion_test(){
		
		Mockito.when( stockRepositoryMock.existsById(Mockito.anyLong()))
		.thenReturn(false);
		
		ObjectNotFoundExcepetion exception = assertThrows(ObjectNotFoundExcepetion.class, () ->{
			
			 stockService.findById(1l);
		});
		assertThat("Não existe stock").isEqualTo( exception.getMessage() );
    }
	
	@Test
	@DisplayName("Deve Buscar todos os Stocks")
	public void deve_buscar_todos_test() {
		
		 	int pageNumber = 0;
	        int pageSize = 1;
	        Pageable pageable = PageRequest.of(pageNumber, pageSize);
	        
			List<StockEntity> stocks = StockCreator.listaDeStocks();
	        
	        Page<StockEntity> stockPage = new PageImpl<>(stocks);
	        when(stockRepositoryMock.findAll(pageable)).thenReturn(stockPage);
	        Page<StockEntity> stockListPage = stockRepositoryMock.findAll(pageable);
	        
	        assertEquals(4, stockListPage.getNumberOfElements());
	        assertEquals(1, stockListPage.getTotalPages());
		
	}
	
	@Test
    @DisplayName("deve editar um stock e retornar sucesso")
    void deve_editar_um_stock_test(){
		
		StockEntity stockSalvo = stockService.update(StockCreator.stockSalvo());
		
		stockSalvo.setName("Ponto Frio Loja fisica");
		stockSalvo.setPrice(250D);
		stockSalvo.setVariation(6D);
		
		assertThat( stockSalvo.getId() ).isNotNull();
		assertThat( stockSalvo.getId() ).isEqualTo(55L);
		assertThat( stockSalvo.getName() ).isEqualTo("Ponto Frio Loja fisica");
		assertThat( stockSalvo.getPrice() ).isEqualTo(250D);
		assertThat( stockSalvo.getVariation() ).isEqualTo(6D);
    }
    
    
}
