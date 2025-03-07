-- 샘플용 추후에 삭제할 예정
DROP TABLE IF EXISTS `sample`;
CREATE TABLE `sample`
(
    sample_id          VARCHAR(255) NOT NULL,
    modified_at        datetime     NOT NULL,
    modified_by        VARCHAR(255) NULL,
    created_at         datetime     NOT NULL,
    created_by         VARCHAR(255) NOT NULL,
    sample_name        VARCHAR(255) NULL,
    sample_description VARCHAR(255) NULL,
    PRIMARY KEY (sample_id)
);

-- 게시판 카테고리
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `CATEGORY_ID`	    VARCHAR(255)	NOT NULL   COMMENT '게시글 카테고리 id',
    `TOP_CATEGORY_ID`	VARCHAR(255)	NOT NULL   COMMENT '게시글 상위 카테고리 id, FK',
    `CATEGORY_NAME`	    VARCHAR(50)	    NOT NULL   COMMENT '게시글 카테고리 이름',
    `CATEGORY_ORD`	    INT	            NOT NULL   COMMENT '게시글 카테고리 정렬순서',
    `VALID_YN`	        TINYINT(1)	    NOT NULL   COMMENT '게시글 카테고리 사용가능 여부, 0 : 사용가능, 1 : 사용불가능',

    -- 시스템 칼럼
    `CREATED_AT`	    DATETIME	    NOT NULL   COMMENT '생성일자',
    `CREATED_BY`	    VARCHAR(50)		NOT NULL   COMMENT '생성자',
    `MODIFIED_AT`	    DATETIME	    NOT NULL   COMMENT '수정일자',
    `MODIFIED_BY`	    VARCHAR(50)		NOT NULL   COMMENT '수정자',

    PRIMARY KEY (CATEGORY_ID)
);
