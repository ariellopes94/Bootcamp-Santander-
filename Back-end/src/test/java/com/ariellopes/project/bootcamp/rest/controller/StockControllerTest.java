package com.ariellopes.project.bootcamp.rest.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.ariellopes.project.bootcamp.exception.model.ObjectNotFoundExcepetion;
import com.ariellopes.project.bootcamp.persistence.entity.StockEntity;
import com.ariellopes.project.bootcamp.rest.controller.domain.dto.stock.NewStockDTO;
import com.ariellopes.project.bootcamp.rest.controller.domain.dto.stock.UpdateStockDTO;
import com.ariellopes.project.bootcamp.rest.service.StockService;
import com.ariellopes.project.bootcamp.util.StockCreator;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = StockController.class)
@AutoConfigureMockMvc
public class StockControllerTest {

	static String STOCK_API = "/api/stocks";
	
	@Autowired
	MockMvc mvc;

	@MockBean
	StockService stockServiceMock;
	
	@BeforeEach
	void setUp() {

		BDDMockito.given(stockServiceMock.save(Mockito.any(StockEntity.class)))
											.willReturn(StockCreator.stockSalvo());
		
		BDDMockito.given(stockServiceMock.update(Mockito.any(StockEntity.class)))
											.willReturn(StockCreator.stockSalvo());
		

		BDDMockito.given(stockServiceMock.findById(55l))
											.willReturn(Optional.of(StockCreator.stockSalvo()));
		
		
		BDDMockito.given(stockServiceMock.findById(1l))
										.willThrow(new ObjectNotFoundExcepetion("Não existe stock"));
					
	
	}

	@Test
    @DisplayName("Deve salvar Stock")
    void create_stock_test() throws Exception {
	 
    	String json = new ObjectMapper().writeValueAsString(StockCreator.newStock()); 
    	
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders 
				.post(STOCK_API) 
				.contentType(MediaType.APPLICATION_JSON) 
				.accept(MediaType.APPLICATION_JSON)
				.content(json); 
		mvc
				.perform(request).andExpect(status().isCreated())
				.andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/stocks/55"));
    }
	 
	@Test
	@DisplayName("Deve lançar erro de validação quando não houver dados suficiente para criação do cliente")
	public void create_invalid_stock_exception_test() throws Exception {
		String json = new ObjectMapper().writeValueAsString(new NewStockDTO());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(STOCK_API)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

		mvc
			.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errors", hasSize(5)));
	}
	
	@Test
	@DisplayName("Deve lançar erro de validação quando não houver dados suficiente para edição do cliente")
	public void update_invalid_stock_exception_test() throws Exception {
		String json = new ObjectMapper().writeValueAsString(new UpdateStockDTO());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(STOCK_API)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

		mvc
			.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errors", hasSize(6)));
	}
	
	@Test
	@DisplayName("Deve editar um stock")
	public void update_stock_WhenSucccess() throws Exception {
		
		String json = new ObjectMapper().writeValueAsString(StockCreator.updateClient()); 
    	
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders 
				.put(STOCK_API) 
				.contentType(MediaType.APPLICATION_JSON) 
				.accept(MediaType.APPLICATION_JSON)
				.content(json); 
		mvc
				.perform(request)
					.andExpect(status().isNoContent());
				
	}
	
	@Test
	@DisplayName("Deve buscar um stock por ID")
	public void deve_buscar_por_id() throws Exception {
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(STOCK_API.concat("/"+55))
				.accept(MediaType.APPLICATION_JSON);
		
		mvc
			.perform(request)
				.andExpect( status().isOk() )
				.andExpect( jsonPath("id").value(55l) )
				.andExpect( jsonPath("name").value("Ponto Frio") )
				.andExpect( jsonPath("price").value(200D) )
				.andExpect( jsonPath("variation").value(5D) );
	}
  
	@Test
	@DisplayName("Deve retornar Objcet Not Found quando o stock não for encontado")
	public void deve_lancar_execao_objcet_not_found_excepetion_test() throws Exception {
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(STOCK_API.concat("/"+1))
				.accept(MediaType.APPLICATION_JSON);
		
		mvc
			.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ObjectNotFoundExcepetion))
				.andExpect(result -> assertEquals("Não existe stock", result.getResolvedException().getMessage()));
		
	}
	
	@Test
	@DisplayName("Deve Buscar todos os Stocks")
	public void deve_buscar_todos_test() throws Exception {
		
		PageRequest paginacao = PageRequest.of(1, 10);
	    List<StockEntity> listaStocks = StockCreator.listaDeStocks();
	    Page<StockEntity> stockPage = new PageImpl<>(listaStocks, paginacao, listaStocks.size());
	    BDDMockito.given(stockServiceMock.findAll(Mockito.any(PageRequest.class))).willReturn(stockPage);

	    mvc
	    	.perform(get(STOCK_API.concat("/buscar-todos"))
	    		.contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("content", Matchers.hasSize(4)))
	            .andExpect( jsonPath("pageable.pageNumber").value(0) );
	    		//.andExpect(jsonPath("pageable.pageNumber")).value(0));
	    
		
	}
}
