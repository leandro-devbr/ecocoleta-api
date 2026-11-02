# EcoColeta - API de Gestão de Resíduos e Reciclagem (ESG)

API RESTful para controle de pontos de coleta seletiva. Usuários registram descartes
de resíduos e, quando um ponto atinge a capacidade máxima, ele fica CHEIO e aparece
nos alertas de coleta.

## Integrantes
- Cauã Araujo Teixeira
- Leandro Falcão de Seixas
- Abraao Felipe Severo Estevam da Silva
- Bruna Yokomizo Ferreira de Souza

## Tecnologias
Java 21, Spring Boot 4, Spring Data JPA, Spring Security (JWT), Bean Validation,
Oracle XE, Flyway, Docker e Docker Compose, Swagger (springdoc).

## Como executar
Pré-requisito: Docker Desktop instalado e aberto.

    docker compose up --build

- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html
- Primeira execução demora alguns minutos (o Oracle precisa inicializar).
- Para zerar o banco: docker compose down -v

## Usuários de teste (criados pela migration V2)
| Perfil  | E-mail              | Senha    |
|---------|---------------------|----------|
| ADMIN   | admin@ecocoleta.com | admin123 |
| USUARIO | user@ecocoleta.com  | user123  |

## Autenticação
Faça login em POST /auth/login e envie o token no cabeçalho:
Authorization: Bearer <token> (validade de 1 hora).

## Endpoints
| Método | Rota                           | Função                        | Acesso  |
|--------|--------------------------------|-------------------------------|---------|
| POST   | /auth/login                    | Login (devolve o token)       | Público |
| POST   | /usuarios                      | Cadastrar usuário             | Público |
| GET    | /pontos-coleta                 | Listar (paginado; ?tipo, ?status) | Logado |
| GET    | /pontos-coleta/{id}            | Buscar um ponto               | Logado  |
| POST   | /pontos-coleta                 | Cadastrar ponto               | ADMIN   |
| PUT    | /pontos-coleta/{id}            | Atualizar ponto               | ADMIN   |
| DELETE | /pontos-coleta/{id}            | Remover ponto                 | ADMIN   |
| POST   | /pontos-coleta/{id}/coleta     | Registrar coleta (zera o ponto) | ADMIN |
| GET    | /pontos-coleta/alertas         | Listar pontos CHEIOS          | Logado  |
| POST   | /descartes                     | Registrar descarte            | Logado  |
| GET    | /descartes/ponto/{pontoId}     | Histórico de descartes        | Logado  |

## Regras de negócio
- Ao registrar um descarte, a quantidade é somada ao ponto. Ao atingir a capacidade máxima,
  o status muda para CHEIO.
- Ponto CHEIO não aceita novos descartes até a coleta ser registrada.
- Registrar coleta zera a quantidade e volta o status para NORMAL.
- Ao alterar a capacidade de um ponto, o status é recalculado.
- O descarte é sempre registrado em nome do usuário logado.

## Erros
Respostas de erro seguem um JSON padrão (status, mensagem, caminho e campos inválidos):
400 dados inválidos ou regra de negócio, 401 não autenticado, 403 sem permissão,
404 não encontrado, 409 conflito de dados, 500 erro interno.

## Banco de dados
Tabelas criadas pelo Flyway (src/main/resources/db/migration):
V1 cria as tabelas e V2 insere os dados iniciais.