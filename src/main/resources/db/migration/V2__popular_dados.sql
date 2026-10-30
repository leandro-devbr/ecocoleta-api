-- =====================================================================
-- V2__popular_dados.sql
-- Dados iniciais para teste da API EcoColeta.
--
-- Usuarios de teste (senhas ja criptografadas com BCrypt):
--   admin@ecocoleta.com  / admin123  (perfil ADMIN)
--   user@ecocoleta.com   / user123   (perfil USUARIO)
--   maria@ecocoleta.com  / user123   (perfil USUARIO)
--
-- Observacoes:
--   - Os ids NAO sao informados: o Oracle gera sozinho (IDENTITY).
--   - A quantidade atual de cada ponto e igual a soma dos seus descartes.
--   - "Ecoponto Mercado" ja esta CHEIO (para testar GET /pontos-coleta/alertas).
--   - "Ecoponto Oficina" nao tem descartes (para testar o DELETE sem erro).
-- =====================================================================

-- ---------------------------------------------------------------------
-- USUARIOS
-- ---------------------------------------------------------------------
INSERT INTO tb_usuario (nome, email, senha, perfil) VALUES
    ('Administrador', 'admin@ecocoleta.com',
     '$2a$10$IQHFcxZ46NgD8lc7sAXEWO2wk4ty6xhBXebi74J9BUWxVset5fuYa', 'ADMIN');

INSERT INTO tb_usuario (nome, email, senha, perfil) VALUES
    ('Usuario Comum', 'user@ecocoleta.com',
     '$2a$10$b/x1dBUo3GbJ5U3ORi0SMeQbxHPFkhSropaICl4PfczEmgLbw33ZW', 'USUARIO');

INSERT INTO tb_usuario (nome, email, senha, perfil) VALUES
    ('Maria Silva', 'maria@ecocoleta.com',
     '$2a$10$b/x1dBUo3GbJ5U3ORi0SMeQbxHPFkhSropaICl4PfczEmgLbw33ZW', 'USUARIO');

-- ---------------------------------------------------------------------
-- PONTOS DE COLETA
-- ---------------------------------------------------------------------
INSERT INTO tb_ponto_coleta
(nome, endereco, tipo_residuo, capacidade_maxima_kg, quantidade_atual_kg, status)
VALUES ('Ecoponto Praca Central', 'Praca Central, 100', 'PLASTICO', 100, 45, 'NORMAL');

INSERT INTO tb_ponto_coleta
(nome, endereco, tipo_residuo, capacidade_maxima_kg, quantidade_atual_kg, status)
VALUES ('Ecoponto Escola Municipal', 'Rua das Flores, 250', 'PAPEL', 50, 20, 'NORMAL');

INSERT INTO tb_ponto_coleta
(nome, endereco, tipo_residuo, capacidade_maxima_kg, quantidade_atual_kg, status)
VALUES ('Ecoponto Mercado', 'Av. Brasil, 1500', 'VIDRO', 80, 80, 'CHEIO');

INSERT INTO tb_ponto_coleta
(nome, endereco, tipo_residuo, capacidade_maxima_kg, quantidade_atual_kg, status)
VALUES ('Ecoponto Oficina', 'Rua dos Metalurgicos, 75', 'METAL', 60, 0, 'NORMAL');

INSERT INTO tb_ponto_coleta
(nome, endereco, tipo_residuo, capacidade_maxima_kg, quantidade_atual_kg, status)
VALUES ('Ecoponto Condominio Verde', 'Rua das Palmeiras, 320', 'ORGANICO', 120, 30, 'NORMAL');

INSERT INTO tb_ponto_coleta
(nome, endereco, tipo_residuo, capacidade_maxima_kg, quantidade_atual_kg, status)
VALUES ('Ecoponto Biblioteca', 'Av. dos Estudantes, 900', 'ELETRONICO', 40, 10, 'NORMAL');

-- ---------------------------------------------------------------------
-- REGISTROS DE DESCARTE
-- Os ids de ponto e usuario sao buscados pelo nome/e-mail (sem fixar numeros).
-- ---------------------------------------------------------------------

-- Ecoponto Praca Central: 25 + 20 = 45 kg
INSERT INTO tb_registro_descarte (ponto_coleta_id, usuario_id, quantidade_kg, data_descarte)
VALUES ((SELECT id FROM tb_ponto_coleta WHERE nome = 'Ecoponto Praca Central'),
        (SELECT id FROM tb_usuario WHERE email = 'user@ecocoleta.com'),
        25, SYSTIMESTAMP - INTERVAL '5' DAY);

INSERT INTO tb_registro_descarte (ponto_coleta_id, usuario_id, quantidade_kg, data_descarte)
VALUES ((SELECT id FROM tb_ponto_coleta WHERE nome = 'Ecoponto Praca Central'),
        (SELECT id FROM tb_usuario WHERE email = 'maria@ecocoleta.com'),
        20, SYSTIMESTAMP - INTERVAL '2' DAY);

-- Ecoponto Escola Municipal: 20 kg
INSERT INTO tb_registro_descarte (ponto_coleta_id, usuario_id, quantidade_kg, data_descarte)
VALUES ((SELECT id FROM tb_ponto_coleta WHERE nome = 'Ecoponto Escola Municipal'),
        (SELECT id FROM tb_usuario WHERE email = 'user@ecocoleta.com'),
        20, SYSTIMESTAMP - INTERVAL '3' DAY);

-- Ecoponto Mercado: 50 + 30 = 80 kg (ponto CHEIO)
INSERT INTO tb_registro_descarte (ponto_coleta_id, usuario_id, quantidade_kg, data_descarte)
VALUES ((SELECT id FROM tb_ponto_coleta WHERE nome = 'Ecoponto Mercado'),
        (SELECT id FROM tb_usuario WHERE email = 'maria@ecocoleta.com'),
        50, SYSTIMESTAMP - INTERVAL '4' DAY);

INSERT INTO tb_registro_descarte (ponto_coleta_id, usuario_id, quantidade_kg, data_descarte)
VALUES ((SELECT id FROM tb_ponto_coleta WHERE nome = 'Ecoponto Mercado'),
        (SELECT id FROM tb_usuario WHERE email = 'user@ecocoleta.com'),
        30, SYSTIMESTAMP - INTERVAL '1' DAY);

-- Ecoponto Condominio Verde: 30 kg
INSERT INTO tb_registro_descarte (ponto_coleta_id, usuario_id, quantidade_kg, data_descarte)
VALUES ((SELECT id FROM tb_ponto_coleta WHERE nome = 'Ecoponto Condominio Verde'),
        (SELECT id FROM tb_usuario WHERE email = 'maria@ecocoleta.com'),
        30, SYSTIMESTAMP - INTERVAL '6' HOUR);

-- Ecoponto Biblioteca: 10 kg
INSERT INTO tb_registro_descarte (ponto_coleta_id, usuario_id, quantidade_kg, data_descarte)
VALUES ((SELECT id FROM tb_ponto_coleta WHERE nome = 'Ecoponto Biblioteca'),
        (SELECT id FROM tb_usuario WHERE email = 'user@ecocoleta.com'),
        10, SYSTIMESTAMP - INTERVAL '2' HOUR);