-- Execute no Oracle SQL Developer conectado como SYSTEM ou outro usuario administrador.
-- Depois rode a aplicacao. O Hibernate cria/atualiza as tabelas automaticamente.

CREATE USER sosorbit IDENTIFIED BY sosorbit;

GRANT CONNECT, RESOURCE TO sosorbit;
GRANT UNLIMITED TABLESPACE TO sosorbit;

-- Conferencia rapida apos a aplicacao subir pela primeira vez:
-- SELECT table_name FROM user_tables ORDER BY table_name;
