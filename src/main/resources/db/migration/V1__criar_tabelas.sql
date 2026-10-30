CREATE TABLE tb_usuario (
                            id      NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            nome    VARCHAR2(100) NOT NULL,
                            email   VARCHAR2(150) NOT NULL,
                            senha   VARCHAR2(100) NOT NULL,
                            perfil  VARCHAR2(20)  NOT NULL,
                            CONSTRAINT uk_usuario_email  UNIQUE (email),
                            CONSTRAINT ck_usuario_perfil CHECK (perfil IN ('ADMIN', 'USUARIO'))
);

CREATE TABLE tb_ponto_coleta (
                                 id                    NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                 nome                  VARCHAR2(100) NOT NULL,
                                 endereco              VARCHAR2(200) NOT NULL,
                                 tipo_residuo          VARCHAR2(20)  NOT NULL,
                                 capacidade_maxima_kg  NUMBER(10,2)  NOT NULL,
                                 quantidade_atual_kg   NUMBER(10,2)  DEFAULT 0 NOT NULL,
                                 status                VARCHAR2(20)  DEFAULT 'NORMAL' NOT NULL,
                                 CONSTRAINT ck_ponto_tipo       CHECK (tipo_residuo IN
                                                                       ('PLASTICO', 'PAPEL', 'METAL', 'VIDRO', 'ORGANICO', 'ELETRONICO')),
                                 CONSTRAINT ck_ponto_status     CHECK (status IN ('NORMAL', 'CHEIO')),
                                 CONSTRAINT ck_ponto_capacidade CHECK (capacidade_maxima_kg > 0)
);

CREATE TABLE tb_registro_descarte (
                                      id               NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                      ponto_coleta_id  NUMBER       NOT NULL,
                                      usuario_id       NUMBER       NOT NULL,
                                      quantidade_kg    NUMBER(10,2) NOT NULL,
                                      data_descarte    TIMESTAMP    DEFAULT SYSTIMESTAMP NOT NULL,
                                      CONSTRAINT fk_descarte_ponto   FOREIGN KEY (ponto_coleta_id) REFERENCES tb_ponto_coleta (id),
                                      CONSTRAINT fk_descarte_usuario FOREIGN KEY (usuario_id)      REFERENCES tb_usuario (id),
                                      CONSTRAINT ck_descarte_qtd     CHECK (quantidade_kg > 0)
);