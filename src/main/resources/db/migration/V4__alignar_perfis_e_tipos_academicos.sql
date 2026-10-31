ALTER TABLE tb_usuario DROP CONSTRAINT ck_usuario_perfil;

UPDATE tb_usuario
SET perfil = 'USER'
WHERE perfil = 'USUARIO';

ALTER TABLE tb_usuario
    ADD CONSTRAINT ck_usuario_perfil CHECK (perfil IN ('ADMIN', 'USER'));

ALTER TABLE tb_ponto_coleta DROP CONSTRAINT ck_ponto_tipo;

UPDATE tb_ponto_coleta
SET tipo_residuo = 'PAPEL'
WHERE tipo_residuo = 'ELETRONICO';

ALTER TABLE tb_ponto_coleta
    ADD CONSTRAINT ck_ponto_tipo CHECK (tipo_residuo IN ('PLASTICO', 'PAPEL', 'VIDRO', 'METAL', 'ORGANICO'));
