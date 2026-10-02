# Registro de Uso de IA Generativa

> **Política da disciplina:** O uso de IA generativa é permitido como
> assistente. O discente é **integralmente responsável** por testar, auditar e
> defender todo o código entregue, independentemente de como foi gerado.

## Instruções

Para cada aula ou entrega, registre abaixo:
- **Data**
- **Ferramenta** (ChatGPT, Copilot, Claude, etc.)
- **Prompt(s) utilizado(s)** (resumo ou cópia)
- **O que foi feito com a saída** (copiado integralmente, adaptado, usado como referência, descartado)

---

## Registro

| Data | Aula | Ferramenta | Prompt (resumo) | Uso da saída |
|------|------|------------|-----------------|--------------|
| 04/09/2026 | Aula 03 | Claude | "Preencha o modelo de pull request (`.github/pull_request_template.md`) com as mudanças desta entrega" | Descrição do PR `.github/pull-requests/PR-aula-03.md` gerada integralmente pelo Claude a partir das mudanças da entrega; revisada por mim. |
| 04/09/2026 | Aula 04 | Claude | "Gere 3 exemplos de JSONs válidos do recurso Beneficiario com nomes e CPFs fictícios para testar a rota POST no Swagger." | Copiado integralmente e colado na interface do Swagger UI para testar o salvamento no banco de dados. |
| 04/09/2026 | Aula 04 | Claude | "Organize minhas respostas da atividade em um arquivo Markdown bem estruturado" | Usado para a modelagem e estilização do `atividade-aula-04.md`: a IA pegou as minhas respostas da atividade e criou o arquivo `.md` (títulos, tabelas, checklist e blocos de código). |
| 04/09/2026 | Aula 04 | Claude | "Preencha o modelo de pull request (`.github/pull_request_template.md`) com as mudanças desta entrega" | Descrição do PR `.github/pull-requests/PR-aula-04.md` gerada integralmente pelo Claude a partir das mudanças da entrega; revisada por mim. |
| 11/09/2026 | Aula 05 | Claude | "Como é a anotação para definir o valor padrão e tamanho da página no Pageable do Controller" | Usado para relembrar a sintaxe da anotação `@PageableDefault(size = 10, sort = "nome")` |
| 11/09/2026 | Aula 05 | Claude | "Como é a sintaxe do Java Record para instanciar um DTO a partir de uma Entity usando o construtor?" | Usado como referência do trecho de código para fazer o mapeamento manual `new BeneficiarioResponseDTO(...)` |
| 11/09/2026 | Aula 05 | Claude | "Preencha o modelo de pull request (`.github/pull_request_template.md`) com as mudanças desta entrega" | Descrição do PR `.github/pull-requests/PR-aula-05.md` gerada integralmente pelo Claude a partir das mudanças da entrega; revisada por mim. |
| 14/09/2026 | Aula 06 | Claude | "Por que a minha validação @NotBlank não está disparando no Controller mesmo com as anotações no DTO?" | Usado para diagnosticar o erro e lembrar de adicionar a anotação `@Valid` antes do `@RequestBody` na assinatura do método. |
| 14/09/2026 | Aula 06 | Claude | "Como usar o springDoc" | Usado para entender documentação. |
| 14/09/2026 | Aula 06 | Claude | "Organize minhas respostas da atividade em um arquivo Markdown bem estruturado" | Usado para a modelagem e estilização do `atividade-aula-06.md`: a IA pegou as minhas respostas da atividade e criou o arquivo `.md` (títulos, tabelas, checklist e blocos de código). |
| 14/09/2026 | Aula 06 | Claude | "Preencha o modelo de pull request (`.github/pull_request_template.md`) com as mudanças desta entrega" | Descrição do PR `.github/pull-requests/PR-aula-06.md` gerada integralmente pelo Claude a partir das mudanças da entrega; revisada por mim. |
| 25/09/2026 | Aula 07 | Claude | "Como funciona a anotação @Testcontainers e a declaração estática do PostgreSQLContainer no JUnit 5?" | Usado como referência para compreender o ciclo de vida do contentor Docker durante a execução dos teste. |
| 25/09/2026 | Aula 07 | ChatGPT | "Tenho a lógica do teste pronta, mas estou na dúvida em qual bloco (Arrange ou Act) deve ficar a chamada do assertThrows para exceções. Como estruturar?" | Usado para entender como aplicar o padrão AAA corretamente quando o teste espera que uma exceção seja lançada. |
| 25/09/2026 | Aula 07 | Claude | "Como posso usar o Mockito.verify() no bloco ASSERT para garantir que o método save() do repositório foi chamado exatamente 1 vez?" | Adaptado para estruturar a verificação de comportamento dos mocks na etapa de Assert. |
| 25/09/2026 | Aula 07 | Claude | "Organize minhas respostas da atividade em um arquivo Markdown bem estruturado" | Usado para a modelagem e estilização do `atividade-aula-07.md`: a IA pegou as minhas respostas da atividade e criou o arquivo `.md` (títulos, tabelas, checklist e blocos de código). |
| 25/09/2026 | Aula 07 | Claude | "Preencha o modelo de pull request (`.github/pull_request_template.md`) com as mudanças desta entrega" | Descrição do PR `.github/pull-requests/PR-aula-07.md` gerada integralmente pelo Claude a partir das mudanças da entrega; revisada por mim. |
| 25/09/2026 | Correção GET beneficiários (pós Aula 07) | Claude | "O professor mandou uma correção da busca por beneficiários (GET) pelo Swagger, não entendi o que significa" | Usado para entender o problema do `Pageable` no Swagger UI e o código enviado pelo professor. A aplicação do código do professor e o tratamento 400 para ordenação por campo inexistente também foram feitos com o Claude e revisados por mim. |
| 25/09/2026 | Correção GET beneficiários (pós Aula 07) | Claude | "Preencha o modelo de pull request (`.github/pull_request_template.md`) com as mudanças desta entrega" | Descrição do PR `.github/pull-requests/PR-fix-busca-beneficiarios-swagger.md` gerada integralmente pelo Claude a partir das mudanças da entrega; revisada por mim. |

| 02/10/2026 | Avaliação A1 | Claude (Cursor) | Enunciado completo da A1 (módulo de Produtos) + "Ao final de tudo, explique para mim tudo que foi inserido" | Código do módulo `produtos` (Entity, migration V4, DTOs, `@EstoqueNaoNegativo`, Service, Controller, handlers 409/422), testes, README e esta linha gerados pelo Claude seguindo os padrões dos módulos existentes; revisados e estudados por mim a partir da explicação gerada. |

---

## Declaração de Uso de IA (A1)

### Ferramentas utilizadas:
- [x] ChatGPT / Claude / Gemini — Claude, via agente do Cursor
- [ ] Copilot / Codeium
- [ ] Nenhuma

### Como utilizei:
- Usei IA para gerar a implementação do módulo de Produtos (Entity, migration Flyway, DTOs, validação customizada, Service, Controller e tratamento de erros) seguindo os padrões já existentes em `beneficiarios` e `doacoes`.
- Usei IA para gerar os testes unitários (`ProdutoServiceTest`) e de integração (`ProdutoControllerIntegrationTest`).
- Usei IA para reescrever o README com os endpoints de Produtos.
- Usei IA para me explicar cada decisão do código (por exemplo, por que estoque negativo retorna 422 e não 400).

### O que eu entendo 100%:
- _(preencher)_

### O que precisei estudar mais:
- _(preencher)_

---

_Declaração: Ao submeter este repositório, confirmo que todo o código foi
revisado, testado e compreendido por mim._