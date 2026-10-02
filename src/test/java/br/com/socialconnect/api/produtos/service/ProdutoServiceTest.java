package br.com.socialconnect.api.produtos.service;

import br.com.socialconnect.api.exception.EstoqueNegativoException;
import br.com.socialconnect.api.exception.ProdutoDuplicadoException;
import br.com.socialconnect.api.exception.RecursoNaoEncontradoException;
import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.dto.ProdutoResponseDTO;
import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.model.Produto;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    @Test
    @DisplayName("Deve criar produto com data de cadastro de hoje e sinalizar estoque baixo")
    void deveCriarProdutoQuandoDadosValidos() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Arroz 5kg", CategoriaProduto.ALIMENTO, 3, 10, "unidade");
        Mockito.when(produtoRepository.existsByNomeIgnoreCase("Arroz 5kg")).thenReturn(false);
        Mockito.when(produtoRepository.save(Mockito.any())).thenAnswer(inv -> {
            Produto p = inv.getArgument(0);
            p.setIdProduto(1L);
            return p;
        });

        // ==========================================
        // ACT: Executar a ação
        // ==========================================
        ProdutoResponseDTO resultado = produtoService.criar(dto);

        // ==========================================
        // ASSERT: Verificar o resultado
        // ==========================================
        ArgumentCaptor<Produto> captor = ArgumentCaptor.forClass(Produto.class);
        Mockito.verify(produtoRepository, Mockito.times(1)).save(captor.capture());
        Assertions.assertEquals(LocalDate.now(), captor.getValue().getDataCadastro());
        Assertions.assertEquals(1L, resultado.idProduto());
        Assertions.assertEquals("Arroz 5kg", resultado.nome());
        Assertions.assertTrue(resultado.estoqueBaixo(), "3 < 10 deve sinalizar estoque baixo");
    }

    @Test
    @DisplayName("Deve lançar EstoqueNegativoException quando o estoque atual for negativo")
    void deveLancarExcecaoQuandoEstoqueNegativo() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Feijão 1kg", CategoriaProduto.ALIMENTO, -5, 10, "kg");

        // ==========================================
        // ACT + ASSERT: a ação é executada dentro do assertThrows
        // ==========================================
        Assertions.assertThrows(EstoqueNegativoException.class, () -> produtoService.criar(dto));
        Mockito.verify(produtoRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Deve lançar ProdutoDuplicadoException quando o nome já existir")
    void deveLancarExcecaoQuandoNomeDuplicado() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Arroz 5kg", CategoriaProduto.ALIMENTO, 20, 10, "unidade");
        Mockito.when(produtoRepository.existsByNomeIgnoreCase("Arroz 5kg")).thenReturn(true);

        // ==========================================
        // ACT + ASSERT: a ação é executada dentro do assertThrows
        // ==========================================
        Assertions.assertThrows(ProdutoDuplicadoException.class, () -> produtoService.criar(dto));
        Mockito.verify(produtoRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Não deve sinalizar estoque baixo quando estoque atual for igual ao mínimo")
    void naoDeveSinalizarEstoqueBaixoQuandoEstoqueIgualAoMinimo() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        Produto produto = Produto.builder()
                .idProduto(1L).nome("Sabonete").categoria(CategoriaProduto.HIGIENE)
                .estoqueAtual(10).estoqueMinimo(10).unidadeMedida("unidade")
                .dataCadastro(LocalDate.of(2026, 9, 18))
                .build();
        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        // ==========================================
        // ACT: Executar a ação
        // ==========================================
        ProdutoResponseDTO resultado = produtoService.buscarPorId(1L);

        // ==========================================
        // ASSERT: Verificar o resultado
        // ==========================================
        Assertions.assertFalse(resultado.estoqueBaixo());
    }

    @Test
    @DisplayName("PUT deve lançar ProdutoDuplicadoException quando o nome pertencer a outro produto")
    void deveLancarExcecaoAoAtualizarComNomeDeOutroProduto() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        Produto existente = Produto.builder()
                .idProduto(1L).nome("Arroz 5kg").categoria(CategoriaProduto.ALIMENTO)
                .estoqueAtual(5).estoqueMinimo(10).unidadeMedida("unidade")
                .dataCadastro(LocalDate.of(2026, 9, 18))
                .build();
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Feijão 1kg", CategoriaProduto.ALIMENTO, 5, 10, "kg");
        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(existente));
        Mockito.when(produtoRepository.existsByNomeIgnoreCaseAndIdProdutoNot("Feijão 1kg", 1L)).thenReturn(true);

        // ==========================================
        // ACT + ASSERT: a ação é executada dentro do assertThrows
        // ==========================================
        Assertions.assertThrows(ProdutoDuplicadoException.class, () -> produtoService.atualizar(1L, dto));
        Mockito.verify(produtoRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao deletar ID inexistente")
    void deveLancarExcecaoAoDeletarInexistente() {
        // ==========================================
        // ARRANGE: Preparar o cenário
        // ==========================================
        Mockito.when(produtoRepository.existsById(99L)).thenReturn(false);

        // ==========================================
        // ACT + ASSERT: a ação é executada dentro do assertThrows
        // ==========================================
        Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> produtoService.deletar(99L));
        Mockito.verify(produtoRepository, Mockito.never()).deleteById(Mockito.any());
    }
}
