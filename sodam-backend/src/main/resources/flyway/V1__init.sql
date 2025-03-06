-- 샘플용 추후에 삭제할 예정
CREATE TABLE sample
(
    sample_id          VARCHAR(255) NOT NULL,
    modified_at        datetime     NOT NULL,
    modified_by        VARCHAR(255) NULL,
    created_at         datetime     NOT NULL,
    created_by         VARCHAR(255) NOT NULL,
    sample_name        VARCHAR(255) NULL,
    sample_description VARCHAR(255) NULL,
    CONSTRAINT pk_sample PRIMARY KEY (sample_id)
);
