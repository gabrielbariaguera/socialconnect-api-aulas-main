package br.com.socialconnect.api.produtos.controller;

import br.com.socialconnect.api.exception.ProblemDetail;
import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.dto.ProdutoResponseDTO;
import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/produtos")
@Tag(name = "Produtos", description = "API para gestão do estoque de produtos doados")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    @Operation(summary = "Lista produtos",
            description = "Lista paginada com filtros opcionais por nome (parcial, sem diferenciar maiúsculas) e categoria")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<Page<ProdutoResponseDTO>> listar(
            @Parameter(description = "Nome para filtrar (parcial)", example = "arroz")
            @RequestParam(required = false) String nome,
            @Parameter(description = "Categoria do produto", example = "ALIMENTO")
            @RequestParam(required = false) CategoriaProduto categoria,
            @Parameter(description = "Número da página (começa em 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de registros por página", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Ordenação no formato campo,direção", example = "nome,asc")
            @RequestParam(defaultValue = "nome,asc") String sort) {

        // Sanitiza o parâmetro sort (o Swagger UI pode enviar ["nome,asc"])
        String[] sortParts = sort.replaceAll("[\\[\\]\" ]", "").split(",");
        String campo = sortParts[0];
        Sort.Direction direcao = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direcao, campo));

        return ResponseEntity.ok(produtoService.listar(nome, categoria, pageable));
    }

    @GetMapping("/{id_produto}")
    @Operation(summary = "Busca produto por ID")
    @ApiResponse(responseCode = "200", description = "Produto encontrado")
    @ApiResponse(responseCode = "400", description = "ID inválido",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Produto não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(
            @Parameter(description = "ID do produto", example = "1") @PathVariable("id_produto") Long idProduto) {
        return ResponseEntity.ok(produtoService.buscarPorId(idProduto));
    }

    @PostMapping
    @Operation(summary = "Cadastra um produto", description = "A data de cadastro é preenchida automaticamente com a data de hoje")
    @ApiResponse(responseCode = "201", description = "Produto cadastrado (URL no cabeçalho Location)")
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Já existe produto com este nome",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "422", description = "Estoque atual negativo",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO salvo = produtoService.criar(dto);
        URI location = URI.create("/api/v1/produtos/" + salvo.idProduto());
        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{id_produto}")
    @Operation(summary = "Substitui um produto", description = "Atualização total: todos os campos devem ser enviados")
    @ApiResponse(responseCode = "200", description = "Produto atualizado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Produto não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Já existe outro produto com este nome",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "422", description = "Estoque atual negativo",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<ProdutoResponseDTO> atualizar(
            @Parameter(description = "ID do produto", example = "1") @PathVariable("id_produto") Long idProduto,
            @Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.ok(produtoService.atualizar(idProduto, dto));
    }

    @DeleteMapping("/{id_produto}")
    @Operation(summary = "Remove um produto")
    @ApiResponse(responseCode = "204", description = "Produto removido")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do produto", example = "1") @PathVariable("id_produto") Long idProduto) {
        produtoService.deletar(idProduto);
        return ResponseEntity.noContent().build();
    }
}
