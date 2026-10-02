# SocialConnect API

> API RESTful de gestão para instituições sociais (ONGs, bancos de alimentos,
> CRAS, abrigos). Conecta doadores, voluntários e beneficiários.

**Disciplina:** ITE005 — Tópicos Especiais em Sistemas para Internet III
**Stack:** Java 21 · Spring Boot 4.1.1 · JPA · Flyway · H2 (dev) · PostgreSQL 17 (testes de integração)

---

## Como Rodar

### Pré-requisitos

- JDK 21 LTS ([Adoptium](https://adoptium.net/))
- Maven 3.9+ (ou use o wrapper: `./mvnw`)
- Docker (apenas para os testes de integração com Testcontainers)
- IDE: IntelliJ IDEA (recomendado) ou VS Code

### Passos

```bash
# 1. Clone o repositório
git clone <url-do-repo>
cd socialconnect-api

# 2. Compile o projeto
./mvnw clean compile

# 3. Rode a aplicação (H2 em memória; o Flyway cria as tabelas na subida)
./mvnw spring-boot:run

# 4. Rode os testes (os de integração exigem Docker em execução)
./mvnw test
```

### Endereços úteis

| Recurso | URL |
|---------|-----|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI (JSON) | http://localhost:8080/api-docs |
| Console H2 | http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:socialconnectdb`, usuário `sa`) |

---

## Estrutura de pacotes

Cada módulo segue a mesma organização em camadas (Controller → Service → Repository):

```
br.com.socialconnect.api
├── beneficiarios/   controller · dto · model · repository · service
├── doacoes/         controller · dto · model · repository · service
├── doadores/        model · repository
├── produtos/        controller · dto · model · repository · service
├── exception/       GlobalExceptionHandler + Problem Details (RFC 7807) + exceções de negócio
├── validation/      validações customizadas (@CPF, @DataNaoFutura, @EstoqueNaoNegativo)
└── config/          i18n e serialização de páginas
```

---

## Endpoints principais

### Produtos (`/api/v1/produtos`) — Avaliação A1

| Método | Rota | Descrição | Respostas |
|--------|------|-----------|-----------|
| GET | `/api/v1/produtos?page=0&size=10&sort=nome,asc&nome=arroz&categoria=ALIMENTO` | Lista paginada; filtros opcionais `nome` (parcial) e `categoria` | 200, 400 |
| GET | `/api/v1/produtos/{id_produto}` | Busca por ID | 200, 404 |
| POST | `/api/v1/produtos` | Cadastra produto (header `Location`) | 201, 400, 409, 422 |
| PUT | `/api/v1/produtos/{id_produto}` | Atualização total | 200, 400, 404, 409, 422 |
| DELETE | `/api/v1/produtos/{id_produto}` | Remove produto | 204, 404 |

Regras de negócio:

- **Estoque não negativo:** `estoqueAtual < 0` é rejeitado com **422 Unprocessable Content**.
- **Estoque baixo:** a resposta traz `estoqueBaixo = true` quando `estoqueAtual < estoqueMinimo`.
- **Nome único:** nome repetido (sem diferenciar maiúsculas) retorna **409 Conflict**.
- `dataCadastro` é preenchida automaticamente no cadastro e preservada no PUT.

Exemplo de corpo para POST/PUT:

```json
{
  "nome": "Arroz 5kg",
  "categoria": "ALIMENTO",
  "estoqueAtual": 3,
  "estoqueMinimo": 10,
  "unidadeMedida": "unidade"
}
```

Categorias aceitas: `ALIMENTO`, `ROUPA`, `HIGIENE`, `OUTROS`.

### Outros módulos

| Recurso | Rota base |
|---------|-----------|
| Beneficiários | `/api/v1/beneficiarios` |
| Doações | `/api/v1/doacoes` |

### Formato de erro (Problem Details)

```json
{
  "type": "https://socialconnect.api/errors/estoque-negativo",
  "title": "Estoque negativo",
  "status": 422,
  "detail": "A operação foi rejeitada porque deixaria o estoque negativo.",
  "instance": "/api/v1/produtos",
  "timestamp": "2026-10-02T19:49:53.877",
  "errors": [{ "field": "estoqueAtual", "message": "O estoque atual não pode ser negativo" }]
}
```

---

## Testes

| Classe | Tipo | O que cobre |
|--------|------|-------------|
| `ProdutoServiceTest` | Unitário (Mockito) | criação válida, estoque negativo, nome duplicado, estoque baixo, PUT com nome de outro produto, DELETE inexistente |
| `ProdutoControllerIntegrationTest` | Integração (Testcontainers + PostgreSQL 17) | 201 com `Location`, 409 nome duplicado, 422 estoque negativo, 404 |

---

## Uso de IA

Ver [`AI_USAGE.md`](AI_USAGE.md).
