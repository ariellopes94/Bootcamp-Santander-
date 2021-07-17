package com.ariellopes.project.bootcamp.rest.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ariellopes.project.bootcamp.persistence.entity.StockEntity;
import com.ariellopes.project.bootcamp.persistence.repository.StockRepository;
import com.ariellopes.project.bootcamp.util.StockCreator;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class RepositoryTest {
	
	@Autowired
	TestEntityManager entityManager;
	
	@Autowired
	StockRepository repositoryMock;
	
	StockEntity stockSalvo;
	
	@BeforeEach
	public void setUp() {
		stockSalvo = entityManager.persist(StockCreator.stockParaSalvarSemID());
	}
	
	@AfterEach
	public  void execurtar_no_final_de_cada_metodo() {
		repositoryMock.deleteById(stockSalvo.getId());

	}
	
	@Test
	@DisplayName("Deve salvar um Stock")
	public void deve_salvar_stock() {
		 
		 //Verrificacoes
		 assertThat(stockSalvo.getId()).isEqualTo(3l);
		 assertThat(stockSalvo.getName()).isEqualTo("Ponto Frio");
		 assertThat(stockSalvo.getPrice()).isEqualTo(200D);
		 assertThat(stockSalvo.getVariation()).isEqualTo(5D);
		
	
	}
	
	@Test
	@DisplayName("Deve obter um stock por id")
	public void buscar_por_id_test() {

		//execução
		 Optional<StockEntity> foundStock = repositoryMock.findById(stockSalvo.getId());
		 
		 //Verrificacoes
		 assertThat(foundStock.isPresent()).isTrue();
		 assertThat(foundStock.get().getId()).isEqualTo(1l);
		 assertThat(foundStock.get().getName()).isEqualTo("Ponto Frio");
		 assertThat(foundStock.get().getPrice()).isEqualTo(200);
		 
		
	}
	
	@Test
	@DisplayName("Deve alterar stock")
	public void alterar_stock() {
		
		stockSalvo.setName("Americanas");
		stockSalvo.setPrice(195D);
		stockSalvo.setVariation(4D);
		
		repositoryMock.save(stockSalvo);
		 
		 //Verrificacoes
		 assertThat(stockSalvo.getId()).isEqualTo(2l);
		 assertThat(stockSalvo.getName()).isEqualTo("Americanas");
		 assertThat(stockSalvo.getPrice()).isEqualTo(195D);
		 assertThat(stockSalvo.getVariation()).isEqualTo(4D);
		 
	}
}
