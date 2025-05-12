DROP TABLE IF EXISTS TB_ARTICLE;

CREATE TABLE IF NOT EXISTS  TB_ARTICLE (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           title VARCHAR(255),
                                           body VARCHAR(2000),
                                           author_id BIGINT,
                                           created_at TIMESTAMP,
                                           updated_at TIMESTAMP,
                                           version INT
);

INSERT INTO TB_ARTICLE (title, body, author_id, created_at, updated_at, version) VALUES ( 'title 1', 'blabla 01', 1234, current_timestamp(), current_timestamp(), 1 );
INSERT INTO TB_ARTICLE (title, body, author_id, created_at, updated_at, version) VALUES ( 'title 2', 'blabla 02', 1234, current_timestamp(), current_timestamp(), 1 );
INSERT INTO TB_ARTICLE (title, body, author_id, created_at, updated_at, version) VALUES ( 'title 3', 'blabla 03', 1234, current_timestamp(), current_timestamp(), 1 );