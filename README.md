# ecocoleta-api

API de gestão de pontos de coleta e registros de descarte.

## Configuração local

O projeto requer Java 17, Docker Desktop com WSL 2 e uma chave JWT Base64 com pelo
menos 32 bytes. Gere uma chave nova para cada ambiente; não compartilhe nem
versione essa chave.

Gere uma chave no PowerShell e copie-a para a variável `JWT_SECRET` da
configuração de execução `EcocoletaApplication` no IntelliJ:

```powershell
$bytes = [byte[]]::new(32)
$rng = [Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($bytes)
$rng.Dispose()
[Convert]::ToBase64String($bytes) | Set-Clipboard
```

Para a execução local pela IDE, configure `JWT_SECRET` e, se necessário,
`DB_URL`, `DB_USERNAME` e `DB_PASSWORD` na configuração de execução.

Para executar API e Oracle em containers:

```powershell
$env:JWT_SECRET = '<cole a chave gerada acima>'
docker compose up --build
```

A aplicação falha na inicialização com uma mensagem explícita se a chave não
estiver configurada. As credenciais padrão do Compose e as contas de demonstração
inseridas pela migração V2 são exclusivamente para desenvolvimento; substitua-as
antes de disponibilizar a API em produção.

## Segurança e endpoints

- `POST /auth/login` autentica e retorna um token Bearer JWT com validade de 30 minutos.
- `POST /usuarios` cadastra um usuário `USER`; o perfil nunca é aceito do cliente.
- `GET /pontos-coleta` lista pontos com paginação e filtros por tipo/status; `GET /pontos-coleta/{id}` busca por id.
- `POST /pontos-coleta`, `PUT /pontos-coleta/{id}` e `DELETE /pontos-coleta/{id}` exigem perfil `ADMIN`.
- `POST /descartes` exige autenticação, associa o descarte ao usuário do token, atualiza o volume do ponto e marca-o como `CHEIO` ao atingir a capacidade.
- `GET /descartes/ponto/{pontoId}` retorna o histórico paginado de descartes do ponto.
- `POST /pontos-coleta/{id}/coleta` zera o volume e retorna o status para `NORMAL`.
- `GET /pontos-coleta/alertas` lista os pontos com status `CHEIO`.
- Todo endpoint funcional, exceto login e cadastro, exige JWT; a documentação Swagger/OpenAPI é pública em `/swagger-ui.html`.

Envie o token nas rotas protegidas pelo cabeçalho `Authorization: Bearer <token>`.
Erros de validação, autenticação e autorização retornam JSON sem expor detalhes
internos ou credenciais.
