-- 게시글 관련 테이블
-- 게시판 카테고리
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `CATEGORY_ID`	    VARCHAR(255)	NOT NULL   COMMENT '게시글 카테고리 id(UUID)',
    `TOP_CATEGORY_ID`	VARCHAR(255)	NOT NULL   COMMENT '게시글 상위 카테고리 id(FK)',
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

-- 베스트 게시판
DROP TABLE IF EXISTS `best_article`;
CREATE TABLE `best_article` (
    `BEST_ARTICLE_ID`   BIGINT          NOT NULL    AUTO_INCREMENT COMMENT '베스트 게시글 id',
    `ARTICLE_ID`        BIGINT          NOT NULL                   COMMENT '게시글 id(FK)',
    `START_AT`          DATETIME        NOT NULL                   COMMENT '베스트 게시글 적용 시작 시간',
    `END_AT`            DATETIME        NOT NULL                   COMMENT '베스트 게시글 적용 종료 시간',

    -- 시스템 칼럼
    `CREATED_AT`	    DATETIME	    NOT NULL                   COMMENT '생성일자',
    `CREATED_BY`	    VARCHAR(50)		NOT NULL                   COMMENT '생성자',
    `MODIFIED_AT`	    DATETIME	    NOT NULL                   COMMENT '수정일자',
    `MODIFIED_BY`	    VARCHAR(50)		NOT NULL                   COMMENT '수정자',

    PRIMARY KEY (BEST_ARTICLE_ID)
);


-- 회원 싫어요 게시글
DROP TABLE IF EXISTS `users_dislike_article`;
CREATE TABLE `users_dislike_article` (
    `USERS_ARTICLE_DISLIKE_ID` BIGINT           NOT NULL    AUTO_INCREMENT    COMMENT '싫어요 게시글 id',
    `ARTICLE_ID`               BIGINT           NOT NULL                      COMMENT '게시글 id(FK)',
    `USER_ID`                  VARCHAR(255)	    NOT NULL                      COMMENT '회원 id(FK)',

    -- 시스템 칼럼
    `CREATED_AT`	           DATETIME	        NOT NULL                     COMMENT '생성일자',
    `CREATED_BY`	           VARCHAR(50)		NOT NULL                     COMMENT '생성자',
    `MODIFIED_AT`	           DATETIME	        NOT NULL                     COMMENT '수정일자',
    `MODIFIED_BY`	           VARCHAR(50)		NOT NULL                     COMMENT '수정자',

    PRIMARY KEY (USERS_ARTICLE_DISLIKE_ID)
);


-- 게시글
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
    `ARTICLE_ID`                BIGINT               NOT NULL    AUTO_INCREMENT    COMMENT '게시글 id',
    `CATEGORY_ID`               VARCHAR(255)         NOT NULL                      COMMENT '카테고리 아이디(FK)',
    `USER_ID`                   VARCHAR(255)	     NOT NULL                      COMMENT '회원 id(FK)',
    `USER_EMAIL`                VARCHAR(255)         NOT NULL                      COMMENT '회원 이메일',
    `ARTICLE_TITLE`             VARCHAR(255)         NOT NULL                      COMMENT '게시글 타이틀',
    `ARTICLE_SUMMARY`           VARCHAR(500)         NOT NULL                      COMMENT '게시글 요약글',
    `ARTICLE_CONTENT`           TEXT                 NOT NULL                      COMMENT '게시글 내용',
    `ARTICLE_VIEW_CNT`          INT                  NOT NULL    DEFAULT 0         COMMENT '게시글 조회수',
    `ARTICLE_LIKE_CNT`          INT                  NOT NULL    DEFAULT 0         COMMENT '게시글 좋아요 수',
    `ARTICLE_DISLIKE_CNT`       INT                  NOT NULL    DEFAULT 0         COMMENT '게시글 싫어요 수',

    -- 시스템 칼럼
    `CREATED_AT`	            DATETIME	         NOT NULL                      COMMENT '생성일자',
    `CREATED_BY`	            VARCHAR(50)		     NOT NULL                      COMMENT '생성자',
    `MODIFIED_AT`	            DATETIME	         NOT NULL                      COMMENT '수정일자',
    `MODIFIED_BY`	            VARCHAR(50)		     NOT NULL                      COMMENT '수정자',

    PRIMARY KEY (ARTICLE_ID)
);

-- 게시글 이미지
DROP TABLE IF EXISTS `article_image`;
CREATE TABLE `article_image` (
    `ARTICLE_IMAGE_ID`          VARCHAR(255)         NOT NULL                      COMMENT '게시글 이미지 아이디(UUID)',
    `ARTICLE_ID`                BIGINT               NOT NULL                      COMMENT '게시글 아이디(FK)',
    `IMAGE_URL`                 VARCHAR(255)         NOT NULL                      COMMENT 'aws s3에 등록된 이미지 url',

    -- 시스템 칼럼
    `CREATED_AT`	            DATETIME	         NOT NULL                      COMMENT '생성일자',
    `CREATED_BY`	            VARCHAR(50)		     NOT NULL                      COMMENT '생성자',
    `MODIFIED_AT`	            DATETIME	         NOT NULL                      COMMENT '수정일자',
    `MODIFIED_BY`	            VARCHAR(50)		     NOT NULL                      COMMENT '수정자',


    PRIMARY KEY (ARTICLE_IMAGE_ID)
);


-- 회원 좋아요 게시글
DROP TABLE IF EXISTS `users_like_article`;
CREATE TABLE `users_like_article` (
    `USERS_ARTICLE_LIKE_ID`     BIGINT              NOT NULL    COMMENT '좋아요 게시글 id',
    `ARTICLE_ID`                BIGINT              NOT NULL    COMMENT '좋아요 게시글 id',
    `USER_ID`                   VARCHAR(255)        NOT NULL    COMMENT '회원 아이디(UUID)',

    -- 시스템 칼럼
    `CREATED_AT`	            DATETIME	        NOT NULL    COMMENT '생성일자',
    `CREATED_BY`	            VARCHAR(50)		    NOT NULL    COMMENT '생성자',
    `MODIFIED_AT`	            DATETIME	        NOT NULL    COMMENT '수정일자',
    `MODIFIED_BY`	            VARCHAR(50)		    NOT NULL    COMMENT '수정자',


    PRIMARY KEY (USERS_ARTICLE_LIKE_ID)
);

-- 게시글 태그
DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags` (
    `TAG_ID`                    BIGINT              NOT NULL   COMMENT '태그 아이디',
    `ARTICLE_ID`                BIGINT              NOT NULL   COMMENT '게시글 아이디',
    `TAG_NAME`                  VARCHAR(50)         NOT NULL   COMMENT '태그명',

    -- 시스템 칼럼
    `CREATED_AT`	            DATETIME	        NOT NULL   COMMENT '생성일자',
    `CREATED_BY`	            VARCHAR(50)		    NOT NULL   COMMENT '생성자',
    `MODIFIED_AT`	            DATETIME	        NOT NULL   COMMENT '수정일자',
    `MODIFIED_BY`	            VARCHAR(50)		    NOT NULL   COMMENT '수정자',


    PRIMARY KEY (TAG_ID)
);

-- 회원 관련 테이블




