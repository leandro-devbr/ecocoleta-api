ALTER TABLE tb_ponto_coleta DROP CONSTRAINT ck_ponto_tipo;

ALTER TABLE tb_ponto_coleta
    ADD CONSTRAINT ck_ponto_tipo CHECK (tipo_residuo IN ('PLASTICO', 'PAPEL', 'METAL', 'VIDRO', 'ORGANICO', 'ELETRONICO'));

UPDATE tb_ponto_coleta
SET tipo_residuo = 'ELETRONICO'
WHERE nome = 'Ecoponto Biblioteca'
  AND tipo_residuo = 'PAPEL';
