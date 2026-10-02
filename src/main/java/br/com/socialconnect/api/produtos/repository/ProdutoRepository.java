package br.com.socialconnect.api.produtos.repository;

import br.com.socialconnect.api.produtos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>, JpaSpecificationExecutor<Produto> {

    // Unicidade do nome no POST
    boolean existsByNomeIgnoreCase(String nome);

    // Unicidade do nome no PUT (ignora o próprio produto)
    boolean existsByNomeIgnoreCaseAndIdProdutoNot(String nome, Long idProduto);
}
