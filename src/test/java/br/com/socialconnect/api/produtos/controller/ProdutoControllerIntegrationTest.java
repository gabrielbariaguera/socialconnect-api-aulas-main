package br.com.socialconnect.api.produtos.controller;

import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.dto.ProdutoResponseDTO;
import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestRestTemplate
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class ProdutoControllerIntegrationTest {

    // PostgreSQL 17 efêmero; @ServiceConnection liga o datasource do Spring a este container
    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17");

    // Em testes o Spring injeta os beans no campo; não há construtor para usar aqui
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProdutoRepository produtoRepository;

    @BeforeEach
    void limparBanco() {
        produtoRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/v1/produtos com dados válidos deve retornar 201 Created")
    void deveCriarProdutoQuandoDadosValidos() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Arroz 5kg", CategoriaProduto.ALIMENTO, 3, 10, "unidade");

        // ==========================================
        // ACT: Executar a ação
        // ==========================================
        ResponseEntity<ProdutoResponseDTO> resposta =
                restTemplate.postForEntity("/api/v1/produtos", dto, ProdutoResponseDTO.class);

        // ==========================================
        // ASSERT: Verificar o resultado
        // ==========================================
        Assertions.assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        Assertions.assertNotNull(resposta.getBody());
        Assertions.assertNotNull(resposta.getBody().idProduto(), "ID do produto não deve ser nulo");
        Assertions.assertTrue(resposta.getBody().estoqueBaixo(), "3 < 10 deve sinalizar estoque baixo");
        Assertions.assertNotNull(resposta.getHeaders().getLocation());
        Assertions.assertTrue(resposta.getHeaders().getLocation().toString()
                .endsWith("/api/v1/produtos/" + resposta.getBody().idProduto()));
    }

    @Test
    @DisplayName("POST /api/v1/produtos com nome já existente deve retornar 409 com Problem Details")
    void deveRetornar409QuandoNomeDuplicado() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        ProdutoRequestDTO primeiro = new ProdutoRequestDTO("Feijão 1kg", CategoriaProduto.ALIMENTO, 20, 10, "kg");
        restTemplate.postForEntity("/api/v1/produtos", primeiro, ProdutoResponseDTO.class);
        ProdutoRequestDTO duplicado = new ProdutoRequestDTO("Feijão 1kg", CategoriaProduto.ALIMENTO, 5, 5, "kg");

        // ==========================================
        // ACT: Executar a ação
        // ==========================================
        ResponseEntity<String> resposta = restTemplate.postForEntity("/api/v1/produtos", duplicado, String.class);

        // ==========================================
        // ASSERT: Verificar o resultado
        // ==========================================
        Assertions.assertEquals(HttpStatus.CONFLICT, resposta.getStatusCode());
        Assertions.assertNotNull(resposta.getBody());
        Assertions.assertTrue(resposta.getBody().contains("\"status\":409"));
        Assertions.assertTrue(resposta.getBody().contains("produto-duplicado"));
    }

    @Test
    @DisplayName("POST /api/v1/produtos com estoque negativo deve retornar 422 com Problem Details")
    void deveRetornar422QuandoEstoqueNegativo() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Sabonete", CategoriaProduto.HIGIENE, -5, 10, "unidade");

        // ==========================================
        // ACT: Executar a ação
        // ==========================================
        ResponseEntity<String> resposta = restTemplate.postForEntity("/api/v1/produtos", dto, String.class);

        // ==========================================
        // ASSERT: Verificar o resultado
        // ==========================================
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_CONTENT, resposta.getStatusCode());
        Assertions.assertNotNull(resposta.getBody());
        Assertions.assertTrue(resposta.getBody().contains("estoque-negativo"));
        Assertions.assertTrue(resposta.getBody().contains("\"field\":\"estoqueAtual\""));
        Assertions.assertEquals(0, produtoRepository.count(), "Nada deve ser gravado no banco");
    }

    @Test
    @DisplayName("GET /api/v1/produtos/{id} inexistente deve retornar 404 com Problem Details")
    void deveRetornar404QuandoProdutoNaoExiste() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        long idInexistente = 999_999L;

        // ==========================================
        // ACT: Executar a ação
        // ==========================================
        ResponseEntity<String> resposta =
                restTemplate.getForEntity("/api/v1/produtos/" + idInexistente, String.class);

        // ==========================================
        // ASSERT: Verificar o resultado
        // ==========================================
        Assertions.assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        Assertions.assertNotNull(resposta.getBody());
        Assertions.assertTrue(resposta.getBody().contains("\"status\":404"));
    }
}
