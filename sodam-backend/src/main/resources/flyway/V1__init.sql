-- 게시글 관련 테이블
-- 게시판 카테고리
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories` (
    `CATEGORY_ID`	    VARCHAR(255)	NOT NULL   COMMENT '게시글 카테고리 아이디(UUID)(PK)',
    `TOP_CATEGORY_ID`	VARCHAR(255)	NOT NULL   COMMENT '게시글 상위 카테고리 아이디(FK)',
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

-- 회원 관련 테이블
-- 포지션
CREATE TABLE `positions` (
    `POSITION_ID`       VARCHAR(255)    NOT NULL    COMMENT '직책 ID',
    `POSITION_NAME`     VARCHAR(255)    NOT NULL    COMMENT '직책 이름',
    `ORD`               INT             NOT NULL    COMMENT '정렬순서',
    `VALID_YN`          TINYINT         NOT NULL    COMMENT '사용 가능 여부 (0: 사용 가능, 1: 사용 불가능)',

    -- 시스템 칼럼
    `CREATED_AT`        DATETIME        NOT NULL    COMMENT '생성일자',
    `CREATED_BY`        VARCHAR(50)     NOT NULL    COMMENT '생성자',
    `MODIFIED_AT`       DATETIME        NOT NULL    COMMENT '수정일자',
    `MODIFIED_BY`       VARCHAR(50)     NOT NULL    COMMENT '수정자',

    PRIMARY KEY (`POSITION_ID`)
);


-- 회원
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
    `USER_ID`                   VARCHAR(255)        NOT NULL    COMMENT '사용자 아이디(UUID)(PK)',
    `USER_EMAIL`                VARCHAR(255)        NOT NULL    COMMENT '사용자 이메일',
    `USER_NAME`                 VARCHAR(50)         NOT NULL    COMMENT '사용자 이름',
    `USER_INTRODUCE`            TEXT                NULL        COMMENT '사용자 자기소개글',
    `USER_IMAGE`                VARCHAR(255)        NOT NULL    COMMENT '사용자 프로필 이미지(aws s3에 등록된 url)',
    `PASSWORD`                  VARCHAR(255)        NOT NULL    COMMENT '사용자 비밀번호(암호화)',

    -- 시스템 칼럼
    `CREATED_AT`	            DATETIME	        NOT NULL    COMMENT '생성일자',
    `CREATED_BY`	            VARCHAR(50)		    NOT NULL    COMMENT '생성자',
    `MODIFIED_AT`	            DATETIME	        NOT NULL    COMMENT '수정일자',
    `MODIFIED_BY`	            VARCHAR(50)		    NOT NULL    COMMENT '수정자',

    PRIMARY KEY (USER_ID)
);

-- 소셜 회원(카카오, 구글, ... 등)
DROP TABLE IF EXISTS `social_users`;
CREATE TABLE `social_users` (
    `SOCIAL_USER_ID`            VARCHAR(255)             NOT NULL       COMMENT '소셜 사용자 ID(UUID)(PK)',
    `USER_EMAIL`                VARCHAR(255)             NULL           COMMENT '사용자 이메일',
    `USER_INTRODUCE`            TEXT                     NULL           COMMENT '사용자 자기소개글',
    `USER_IMAGE`                VARCHAR(255)             NULL           COMMENT '사용자 프로필 이미지(aws s3에 등록된 url)',
    `USER_NAME`                 VARCHAR(50)              NULL           COMMENT '소셜 사용자 이름',
    `PROVIDER`                  VARCHAR(255)             NOT NULL       COMMENT '소셜 프로바이더 (구글, 카카오, ... 등)',
    `PROVIDER_ID`               VARCHAR(255)             NOT NULL       COMMENT '프로바이더 아이디(FK)',

    -- 시스템 칼럼
    `CREATED_AT`	            DATETIME	             NOT NULL    COMMENT '생성일자',
    `CREATED_BY`	            VARCHAR(50)		         NOT NULL    COMMENT '생성자',
    `MODIFIED_AT`	            DATETIME	             NOT NULL    COMMENT '수정일자',
    `MODIFIED_BY`	            VARCHAR(50)		         NOT NULL    COMMENT '수정자',


    PRIMARY KEY (SOCIAL_USER_ID)
);

-- 회원 포지션
CREATE TABLE `users_position` (
    `USER_POSITION_ID`  VARCHAR(255)  NOT NULL COMMENT '회원 포지션 생성 ID',
    `POSITION_ID`       VARCHAR(255)  NOT NULL COMMENT '포지션 ID (FK)',
    `USER_ID`           VARCHAR(255)  NULL COMMENT '사용자 ID',
    `SOCIAL_USER_ID`    VARCHAR(255)  NULL COMMENT '소셜 사용자 ID',

    -- 시스템 칼럼
    `CREATED_AT`        DATETIME      NOT NULL COMMENT '생성일자',
    `CREATED_BY`        VARCHAR(50)   NOT NULL COMMENT '생성자',
    `MODIFIED_AT`       DATETIME      NOT NULL COMMENT '수정일자',
    `MODIFIED_BY`       VARCHAR(50)   NOT NULL COMMENT '수정자',

    -- 참조
    CONSTRAINT FK_USERS_POSITION_USER_ID  FOREIGN KEY (USER_ID) REFERENCES `users`(USER_ID), -- FK 참조 : 회원
    CONSTRAINT FK_USERS_POSITION_SOCIAL_USER_ID FOREIGN KEY (SOCIAL_USER_ID) REFERENCES `social_users`(SOCIAL_USER_ID), -- FK 참조 : 소셜 회원
    CONSTRAINT FK_USERS_POSITION_POSITION_ID  FOREIGN KEY (POSITION_ID) REFERENCES `positions`(POSITION_ID), -- FK 참조 : 포지션

    PRIMARY KEY (`USER_POSITION_ID`)
);



-- 회원 이력(접속, 요청 이력 기록)
DROP TABLE IF EXISTS `users_history`;
CREATE TABLE `users_history` (
    `USER_HISTORY_ID`           BIGINT                  NOT NULL     AUTO_INCREMENT     COMMENT '사용자 이력 아이디(PK)',
    `USER_ID`                   VARCHAR(255)            NOT NULL                        COMMENT '사용자 아이디(FK)',
    `USER_ROLE`                 VARCHAR(255)            NOT NULL                        COMMENT '사용자 역할',
    `REQ_IP`                    VARCHAR(255)            NOT NULL                        COMMENT '요청 IP',
    `REQ_METHOD`                VARCHAR(255)            NOT NULL                        COMMENT '요청 메서드',
    `REQ_URL`                   VARCHAR(255)            NOT NULL                        COMMENT '요청 url',
    `REQ_HEADER`                TEXT                    NOT NULL                        COMMENT '요청 헤더',
    `REQ_BODY`                  TEXT                    NOT NULL                        COMMENT '요청 바디',

    -- 시스템 칼럼
    `CREATED_AT`	            DATETIME	            NOT NULL                        COMMENT '생성일자',
    `CREATED_BY`	            VARCHAR(50)		        NOT NULL                        COMMENT '생성자',
    `MODIFIED_AT`	            DATETIME	            NOT NULL                        COMMENT '수정일자',
    `MODIFIED_BY`	            VARCHAR(50)		        NOT NULL                        COMMENT '수정자',

    PRIMARY KEY (USER_HISTORY_ID)
);

-- 등급 테이블
DROP TABLE IF EXISTS `grades`;
CREATE TABLE `grades` (
    `GRADE_ID`         VARCHAR(255)                     NOT NULL                        COMMENT '등급 ID (PK)',
    `GRADE_NAME`       VARCHAR(50)                      NOT NULL                        COMMENT '등급명',
    `GRADE_ORD`        INT                              NOT NULL                        COMMENT '정렬 순서',
    `GRADE_SUMMARY`    VARCHAR(500)                     NOT NULL                        COMMENT '등급 요약',
    `GRADE_DESCRIBE`   TEXT                             NOT NULL                        COMMENT '등급 설명',
    `VALID_YN`         TINYINT                          NOT NULL                        COMMENT '사용 가능 여부 (0: 사용 가능, 1: 사용 불가능)',

    -- 시스템 칼럼
    `CREATED_AT`       DATETIME                         NOT NULL                        COMMENT '생성일자',
    `CREATED_BY`       VARCHAR(50)                      NOT NULL                        COMMENT '생성자',
    `MODIFIED_AT`      DATETIME                         NOT NULL                        COMMENT '수정일자',
    `MODIFIED_BY`      VARCHAR(50)                      NOT NULL                        COMMENT '수정자',

     PRIMARY KEY (`GRADE_ID`)
);


-- 회원 등급 테이블
DROP TABLE IF EXISTS `users_grade`;
CREATE TABLE `users_grade` (
    `USER_GRADE_ID`   VARCHAR(255)                      NOT NULL                        COMMENT '회원 등급 ID (PK)',
    `USER_ID`         VARCHAR(255)                      NULL                            COMMENT '일반 회원 ID (FK)',
    `SOCIAL_USER_ID`  VARCHAR(255)                      NULL                            COMMENT '소셜 회원 ID (FK)',
    `GRADE_ID`        VARCHAR(255)                      NOT NULL                        COMMENT '등급 ID (FK)',
    `START_AT`        DATETIME                          NOT NULL                        COMMENT '등급 시작일',
    `END_AT`          DATETIME                          NOT NULL                        COMMENT '등급 종료일',
    `VALID_YN`        TINYINT                           NOT NULL                        COMMENT '사용 가능 여부 (0: 사용 가능, 1: 사용 불가능)',

    -- 시스템 칼럼
    `CREATED_AT`      DATETIME                          NOT NULL                        COMMENT '생성일자',
    `CREATED_BY`      VARCHAR(50)                       NOT NULL                        COMMENT '생성자',
    `MODIFIED_AT`     DATETIME                          NOT NULL                        COMMENT '수정일자',
    `MODIFIED_BY`     VARCHAR(50)                       NOT NULL                        COMMENT '수정자',

    -- 참조 키 : 등급, 일반 회원, 소셜 회원
    CONSTRAINT FK_USERS_GRADE_GRADE_ID FOREIGN KEY (GRADE_ID) REFERENCES `grades`(GRADE_ID), -- FK 참조 : 등급
    CONSTRAINT FK_USERS_GRADE_USER_ID  FOREIGN KEY (USER_ID) REFERENCES `users`(USER_ID), -- FK 참조 : 회원
    CONSTRAINT FK_USERS_GRADE_SOCIAL_USER_ID FOREIGN KEY (SOCIAL_USER_ID) REFERENCES `social_users`(SOCIAL_USER_ID), -- FK 참조 : 소셜 회원

    PRIMARY KEY (`USER_GRADE_ID`)
);


-- 회원 토큰
DROP TABLE IF EXISTS `user_tokens`;
CREATE TABLE `user_tokens` (
    `TOKEN_ID`                  VARCHAR(255)            NOT NULL    COMMENT '토큰 아이디(pk)',
    `USER_ID`                   VARCHAR(255)                        COMMENT '사용자 아이디(FK)',
    `SOCIAL_USER_ID`            VARCHAR(255)                        COMMENT '소숄 사용자 아이디(FK)',
    `ACCESS_TOKEN`              VARCHAR(255)            NOT NULL    COMMENT '액세스 토큰',
    `REFRESH_TOKEN`             VARCHAR(255)            NOT NULL    COMMENT '리프레시 토큰',
    `ACCESS_TOKEN_EXPIRES_AT`   DATETIME                NOT NULL    COMMENT '액세스 토큰 만료시간',
    `REFRESH_TOKEN_EXPIRES_AT`  DATETIME                NOT NULL    COMMENT '리프레시 토큰 만료시간',

    -- 시스템 칼럼
    `CREATED_AT`	            DATETIME	            NOT NULL    COMMENT '생성일자',
    `CREATED_BY`	            VARCHAR(50)		        NOT NULL    COMMENT '생성자',
    `MODIFIED_AT`	            DATETIME	            NOT NULL    COMMENT '수정일자',
    `MODIFIED_BY`	            VARCHAR(50)		        NOT NULL    COMMENT '수정자',

    -- FK 참조 : 회원
    CONSTRAINT FK_USER_TOKENS_USER_ID FOREIGN KEY (USER_ID) REFERENCES `users`(USER_ID), -- FK 참조 : 회원
    CONSTRAINT FK_USER_TOKENS_SOCIAL_USER_ID FOREIGN KEY (SOCIAL_USER_ID) REFERENCES `social_users`(SOCIAL_USER_ID),

    PRIMARY KEY (TOKEN_ID)
);

-- 정책 부분 추가
-- 규칙 테이블
DROP TABLE IF EXISTS `rules`;
CREATE TABLE `rules` (
    `RULE_ID`         VARCHAR(255)          NOT NULL    COMMENT         '규칙 ID (PK, UUID)',
    `RULE_KIND`       INT                   NOT NULL    COMMENt         '규칙 구분 칼럼, 서비스 제제에서 활용되는 규칙, 쿠폰에서 활용되는 규칙',
    `TARGET_INDEX`    INT                   NOT NULL    COMMENT         '대상 인덱스 (0: 비하발언, 2: 정치발언, 3: 종교발언 등)',
    `TARGET_OPERATOR` VARCHAR(50)           NOT NULL    COMMENT         '대상 연산자 (=, <, <=, >, >=)',
    `TARGET_VALUE`    INT                   NOT NULL    COMMENT         '비교 기준 값',
    `VALID_YN`        TINYINT               NOT NULL    COMMENT         '사용 가능 여부 (0: 사용 가능, 1: 사용 불가능)',

    -- 시스템 칼럼
    `CREATED_AT`      DATETIME              NOT NULL    COMMENT         '생성일자',
    `CREATED_BY`      VARCHAR(50)           NOT NULL    COMMENT         '생성자',
    `MODIFIED_AT`     DATETIME              NOT NULL    COMMENT         '수정일자',
    `MODIFIED_BY`     VARCHAR(50)           NOT NULL    COMMENT         '수정자',

     PRIMARY KEY (`RULE_ID`)
);

-- 제재 정책 테이블
DROP TABLE IF EXISTS `sanctions_policy`;
CREATE TABLE `sanctions_policy` (
    `POLICY_ID`      VARCHAR(255)  NOT NULL    COMMENT '제재 정책 ID (PK, UUID)',
    `RULE_ID1`       VARCHAR(255)  NOT NULL    COMMENT '규칙 ID 1 (FK, UUID)',
    `OPERATOR1`      VARCHAR(50)   NOT NULL    COMMENT '규칙1과 규칙2 조합 연산 -> and, or, not, ...',
    `RULE_ID2`       VARCHAR(255)  NOT NULL    COMMENT '규칙 ID 2 (FK, UUID)',
    `OPERATOR2`      VARCHAR(50)   NOT NULL    COMMENT '규칙2와 규칙3 조합 연산 -> and, or, not, ...',
    `RULE_ID3`       VARCHAR(255)  NOT NULL    COMMENT '규칙 ID 3 (FK, UUID)',
    `VALID_YN`       TINYINT       NOT NULL    COMMENT '사용 가능 여부 (0: 사용 가능, 1: 사용 불가능)',

    -- 시스템 칼럼
    `CREATED_AT`     DATETIME      NOT NULL    COMMENT '생성일자',
    `CREATED_BY`     VARCHAR(50)   NOT NULL    COMMENT '생성자',
    `MODIFIED_AT`    DATETIME      NOT NULL    COMMENT '수정일자',
    `MODIFIED_BY`    VARCHAR(50)   NOT NULL    COMMENT '수정자',

    -- FK 참조 : 규칙 3개
    CONSTRAINT FK_SANCTION_POLICY_RULE_ID1 FOREIGN KEY (RULE_ID1) REFERENCES `rules`(RULE_ID),
    CONSTRAINT FK_SANCTION_POLICY_RULE_ID2 FOREIGN KEY (RULE_ID2) REFERENCES `rules`(RULE_ID),
    CONSTRAINT FK_SANCTION_POLICY_RULE_ID3 FOREIGN KEY (RULE_ID3) REFERENCES `rules`(RULE_ID),

    PRIMARY KEY (`POLICY_ID`)
);


-- 회원 제재 이력 테이블
DROP TABLE IF EXISTS `users_sanction_history`;
CREATE TABLE `users_sanction_history` (
    `SANCTION_ID`    BIGINT          NOT NULL       AUTO_INCREMENT  COMMENT '제재 ID (PK)',
    `USER_ID`        VARCHAR(255)    NULL                           COMMENT '회원 ID (FK)',
    `SOCIAL_USER_ID` VARCHAR(255)    NULL                           COMMENT '소셜 회원 ID (FK)',
    `POLICY_ID`      VARCHAR(255)    NOT NULL                       COMMENT '정책 ID (FK)',
    `START_AT`       DATETIME        NOT NULL                       COMMENT '제재 시작일',
    `END_AT`         DATETIME        NOT NULL                       COMMENT '제재 종료일',

    -- 시스템 칼럼
    `CREATED_AT`     DATETIME       NOT NULL                        COMMENT '생성일자',
    `CREATED_BY`     VARCHAR(50)    NOT NULL                        COMMENT '생성자',
    `MODIFIED_AT`    DATETIME       NOT NULL                        COMMENT '수정일자',
    `MODIFIED_BY`    VARCHAR(50)    NOT NULL                        COMMENT '수정자',

    -- FK 참조 : 회원, 정책
    CONSTRAINT FK_USER_SANCTION_HISTORY_USER_ID FOREIGN KEY (USER_ID) REFERENCES `users`(USER_ID), -- FK 참조 : 회원
    CONSTRAINT FK_USER_SANCTION_HISTORY_SOCIAL_USER_ID FOREIGN KEY (SOCIAL_USER_ID) REFERENCES `social_users`(SOCIAL_USER_ID),
    CONSTRAINT FK_USER_SANCTION_HISTORY_POLICY_ID FOREIGN KEY (POLICY_ID) REFERENCES `sanctions_policy`(POLICY_ID),

    PRIMARY KEY (`SANCTION_ID`)
);

-- 공인 회원 상태 테이블
DROP TABLE IF EXISTS `official_users_status`;
CREATE TABLE `official_users_status` (
    `OFFICIAL_ID`      BIGINT           NOT NULL    AUTO_INCREMENT  COMMENT '공식 회원 ID (PK)',
    `SOCIAL_USER_ID`   VARCHAR(255)     NULL                        COMMENT '소셜 회원 ID (FK)',
    `USER_ID`          VARCHAR(255)     NULL                        COMMENT '일반 회원 ID (FK)',
    `START_AT`         DATETIME         NOT NULL                    COMMENT '공식 인증 시작일',
    `END_AT`           DATETIME         NOT NULL                    COMMENT '공식 인증 종료일',
    `VALID_YN`         TINYINT          NOT NULL                    COMMENT '사용 가능 여부 (0: 사용 가능, 1: 사용 불가능)',

    -- 시스템 칼럼
    `CREATED_AT`       DATETIME         NOT NULL                    COMMENT '생성일자',
    `CREATED_BY`       VARCHAR(50)      NOT NULL                    COMMENT '생성자',
    `MODIFIED_AT`      DATETIME         NOT NULL                    COMMENT '수정일자',
    `MODIFIED_BY`      VARCHAR(50)      NOT NULL                    COMMENT '수정자',

    CONSTRAINT FK_OFFICIAL_USERS_STATUS_USER_ID FOREIGN KEY (USER_ID) REFERENCES `users`(USER_ID), -- FK 참조 : 회원
    CONSTRAINT FK_OFFICIAL_USERS_STATUS_SOCIAL_USER_ID FOREIGN KEY (SOCIAL_USER_ID) REFERENCES `social_users`(SOCIAL_USER_ID),

    PRIMARY KEY (`OFFICIAL_ID`)
);


-- 게시글
DROP TABLE IF EXISTS `articles`;
CREATE TABLE `articles` (
    `ARTICLE_ID`                BIGINT               NOT NULL    AUTO_INCREMENT    COMMENT '게시글 아이디(PK)',
    `CATEGORY_ID`               VARCHAR(255)         NOT NULL                      COMMENT '카테고리 아이디(FK)',
    `USER_ID`                   VARCHAR(255)	                                   COMMENT '회원 아이디(FK)',
    `SOCIAL_USER_ID`            VARCHAR(255)                                       COMMENT '소셜 회원 아이디(FK)',
    `USER_NAME`                 VARCHAR(255)         NOT NULL                      COMMENT '회원 이메일',
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

    -- FK 참조 : 카테고리, 회원
    CONSTRAINT `FK_ARTICLES_CATEGORY_ID` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `categories`(`CATEGORY_ID`) ON DELETE CASCADE,
    CONSTRAINT `FK_ARTICLES_USER_ID` FOREIGN KEY (`USER_ID`) REFERENCES `users`(`USER_ID`) ON DELETE CASCADE,
    CONSTRAINT `FK_ARTICLES_SOCIAL_USER_ID` FOREIGN KEY (`SOCIAL_USER_ID`) REFERENCES `social_users`(`SOCIAL_USER_ID`) ON DELETE CASCADE,

    PRIMARY KEY (ARTICLE_ID)
);

-- 게시글 이미지
DROP TABLE IF EXISTS `article_images`;
CREATE TABLE `article_images` (
    `ARTICLE_IMAGE_ID`          VARCHAR(255)         NOT NULL                      COMMENT '게시글 이미지 아이디(UUID)(PK)',
    `ARTICLE_ID`                BIGINT               NOT NULL                      COMMENT '게시글 아이디(FK)',
    `IMAGE_URL`                 VARCHAR(255)         NOT NULL                      COMMENT 'aws s3에 등록된 이미지 url',

    -- 시스템 칼럼
    `CREATED_AT`	            DATETIME	         NOT NULL                      COMMENT '생성일자',
    `CREATED_BY`	            VARCHAR(50)		     NOT NULL                      COMMENT '생성자',
    `MODIFIED_AT`	            DATETIME	         NOT NULL                      COMMENT '수정일자',
    `MODIFIED_BY`	            VARCHAR(50)		     NOT NULL                      COMMENT '수정자',

    -- FK 참조 : 게시글
    -- FK 참조 : 게시글 (ARTICLE_ID 참조: articles.ARTICLE_ID)
    CONSTRAINT `FK_ARTICLE_IMAGES_ARTICLE_ID` FOREIGN KEY (`ARTICLE_ID`) REFERENCES `articles`(`ARTICLE_ID`) ON DELETE CASCADE,

    PRIMARY KEY (ARTICLE_IMAGE_ID)
);

DROP TABLE IF EXISTS `best_articles`;

CREATE TABLE `best_articles` (
    `BEST_ARTICLE_ID`   BIGINT          NOT NULL    AUTO_INCREMENT COMMENT '베스트 게시글 아이디(PK)',
    `ARTICLE_ID`        BIGINT          NOT NULL                   COMMENT '게시글 아이디(FK)',
    `START_AT`          DATETIME        NOT NULL                   COMMENT '베스트 게시글 적용 시작 시간',
    `END_AT`            DATETIME        NOT NULL                   COMMENT '베스트 게시글 적용 종료 시간',

    -- 게시글 정보
    `ARTICLE_TITLE`     VARCHAR(255)    NOT NULL                   COMMENT '게시글 제목',
    `ARTICLE_SUMMARY`   VARCHAR(500)    NOT NULL                   COMMENT '게시글 요약',
    `ARTICLE_AUTHOR`    VARCHAR(255)    NOT NULL                   COMMENT '게시글 작성자',

    -- 태그
    `TAG1`             VARCHAR(25)      NULL                       COMMENT '태그 1',
    `TAG2`             VARCHAR(25)      NULL                       COMMENT '태그 2',
    `TAG3`             VARCHAR(25)      NULL                       COMMENT '태그 3',

    -- 시스템 칼럼
    `CREATED_AT`        DATETIME        NOT NULL                   COMMENT '생성일자',
    `CREATED_BY`        VARCHAR(50)     NOT NULL                   COMMENT '생성자',
    `MODIFIED_AT`       DATETIME        NOT NULL                   COMMENT '수정일자',
    `MODIFIED_BY`       VARCHAR(50)     NOT NULL                   COMMENT '수정자',

    -- FK 참조 : 게시글
    CONSTRAINT `FK_BEST_ARTICLES_ARTICLE_ID` FOREIGN KEY (`ARTICLE_ID`) REFERENCES `articles`(`ARTICLE_ID`) ON DELETE CASCADE,

    PRIMARY KEY (`BEST_ARTICLE_ID`)
);

-- 회원 싫어요 게시글
DROP TABLE IF EXISTS `users_dislike_articles`;
CREATE TABLE `users_dislike_articles` (
    `USERS_ARTICLE_DISLIKE_ID` BIGINT           NOT NULL    AUTO_INCREMENT    COMMENT '싫어요 게시글 아이디(PK)',
    `ARTICLE_ID`               BIGINT           NOT NULL                      COMMENT '게시글 아이디(FK)',
    `USER_ID`                  VARCHAR(255)	                                  COMMENT '회원 아이디(FK)',
    `SOCIAL_USER_ID`           VARCHAR(255)                                   COMMENT '소셜 회원 아이디(FK)',

    -- 시스템 칼럼
    `CREATED_AT`	           DATETIME	        NOT NULL                     COMMENT '생성일자',
    `CREATED_BY`	           VARCHAR(50)		NOT NULL                     COMMENT '생성자',
    `MODIFIED_AT`	           DATETIME	        NOT NULL                     COMMENT '수정일자',
    `MODIFIED_BY`	           VARCHAR(50)		NOT NULL                     COMMENT '수정자',

    -- FK 참조 : 게시글, 회원
    CONSTRAINT `FK_USERS_DISLIKE_ARTICLES_ARTICLE_ID` FOREIGN KEY (`ARTICLE_ID`) REFERENCES `articles`(`ARTICLE_ID`) ON DELETE CASCADE,
    CONSTRAINT `FK_USERS_DISLIKE_ARTICLES_USER_ID` FOREIGN KEY (`USER_ID`) REFERENCES `users`(`USER_ID`) ON DELETE CASCADE,
    CONSTRAINT `FK_USERS_DISLIKE_ARTICLES_SOCIAL_USER_ID` FOREIGN KEY (`SOCIAL_USER_ID`) REFERENCES `social_users`(`SOCIAL_USER_ID`) ON DELETE CASCADE,

    PRIMARY KEY (USERS_ARTICLE_DISLIKE_ID)
);


-- 회원 좋아요 게시글
DROP TABLE IF EXISTS `users_like_articles`;
CREATE TABLE `users_like_articles` (
    `USERS_ARTICLE_LIKE_ID`     BIGINT              NOT NULL    AUTO_INCREMENT      COMMENT '좋아요 게시글 아이디(PK)',
    `ARTICLE_ID`                BIGINT              NOT NULL                        COMMENT '좋아요 게시글 아이디(FK)',
    `USER_ID`                   VARCHAR(255)                                        COMMENT '회원 아이디(UUID)(FK)',
    `SOCIAL_USER_ID`            VARCHAR(255)                                        COMMENT '소셜 회원 아이디(UUID)(FK)',

    -- 시스템 칼럼
    `CREATED_AT`	            DATETIME	        NOT NULL                        COMMENT '생성일자',
    `CREATED_BY`	            VARCHAR(50)		    NOT NULL                        COMMENT '생성자',
    `MODIFIED_AT`	            DATETIME	        NOT NULL                        COMMENT '수정일자',
    `MODIFIED_BY`	            VARCHAR(50)		    NOT NULL                        COMMENT '수정자',

    -- FK 참조 : 게시글, 회원
    -- FK 참조 : 게시글 (ARTICLE_ID 참조: articles.ARTICLE_ID)
    -- FK 참조 : 회원 (USER_ID 참조: users.USER_ID)
    CONSTRAINT `FK_USERS_LIKE_ARTICLES_ARTICLE_ID` FOREIGN KEY (`ARTICLE_ID`) REFERENCES `articles`(`ARTICLE_ID`) ON DELETE CASCADE,
    CONSTRAINT `FK_USERS_LIKE_ARTICLES_USER_ID` FOREIGN KEY (`USER_ID`) REFERENCES `users`(`USER_ID`) ON DELETE CASCADE,
    CONSTRAINT `FK_USERS_LIKE_ARTICLES_SOCIAL_USER_ID` FOREIGN KEY (`SOCIAL_USER_ID`) REFERENCES `social_users`(`SOCIAL_USER_ID`) ON DELETE CASCADE,

    PRIMARY KEY (USERS_ARTICLE_LIKE_ID)
);

-- 게시글 태그
DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags` (
    `TAG_ID`                    BIGINT              NOT NULL   AUTO_INCREMENT       COMMENT '태그 아이디(PK)',
    `ARTICLE_ID`                BIGINT              NOT NULL                        COMMENT '게시글 아이디(FK)',
    `TAG_NAME`                  VARCHAR(50)         NOT NULL                        COMMENT '태그명',

    -- 시스템 칼럼
    `CREATED_AT`	            DATETIME	        NOT NULL                        COMMENT '생성일자',
    `CREATED_BY`	            VARCHAR(50)		    NOT NULL                        COMMENT '생성자',
    `MODIFIED_AT`	            DATETIME	        NOT NULL                        COMMENT '수정일자',
    `MODIFIED_BY`	            VARCHAR(50)		    NOT NULL                        COMMENT '수정자',

    -- FK 참조 : 게시글
    -- FK 참조 : 게시글 (ARTICLE_ID 참조: articles.ARTICLE_ID)
    CONSTRAINT `FK_TAGS_ARTICLE_ID` FOREIGN KEY (`ARTICLE_ID`) REFERENCES `articles`(`ARTICLE_ID`) ON DELETE CASCADE,

    PRIMARY KEY (TAG_ID)
);



-- 댓글 관련 테이블
-- 댓글
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
    `COMMENT_ID`                  BIGINT              NOT NULL    AUTO_INCREMENT    COMMENT '댓글 아이디(PK)',
    `ARTICLE_ID`                  BIGINT              NOT NULL                      COMMENT '게시글 아이디(FK)',
    `USER_ID`                     VARCHAR(255)                                      COMMENT '사용자 아이디(FK)',
    `SOCIAL_USER_ID`              VARCHAR(255)                                      COMMENT '소셜 사용자 아이디(FK)',
    `USER_IMAGE`                  VARCHAR(255)        NOT NULL                      COMMENT '사용자 프로필 이미지(aws s3에 등록된 url)',
    `COMMENT_CONTENT`             TEXT                NOT NULL                      COMMENT '댓글 내용',
    `COMMENT_LIKE_CNT`            INT                 NOT NULL    DEFAULT 0         COMMENT '댓글 좋아요 수',
    `COMMENT_DISLIKE_CNT`         INT                 NOT NULL    DEFAULT 0         COMMENT '댓글 싫어요 수',

    -- 시스템 칼럼
    `CREATED_AT`	            DATETIME	        NOT NULL                        COMMENT '생성일자',
    `CREATED_BY`	            VARCHAR(50)		    NOT NULL                        COMMENT '생성자',
    `MODIFIED_AT`	            DATETIME	        NOT NULL                        COMMENT '수정일자',
    `MODIFIED_BY`	            VARCHAR(50)		    NOT NULL                        COMMENT '수정자',

    -- FK 참조 : 게시글, 사용자
    CONSTRAINT FK_COMMENTS_ARTICLE_ID FOREIGN KEY (ARTICLE_ID) REFERENCES `articles`(ARTICLE_ID), -- FK 참조 : 게시글
    CONSTRAINT FK_COMMENTS_USER_ID_COMMENTS FOREIGN KEY (USER_ID) REFERENCES `users`(USER_ID), -- FK 참조 : 사용자
    CONSTRAINT FK_COMMENTS_SOCIAL_USER_ID_COMMENTS FOREIGN KEY (SOCIAL_USER_ID) REFERENCES `social_users`(SOCIAL_USER_ID), -- FK 참조 : 사용자

    PRIMARY KEY (COMMENT_ID)
);

-- 댓글 좋아요
DROP TABLE IF EXISTS `user_like_comments`;
CREATE TABLE `user_like_comments` (
    `USERS_LIKE_COMMENT_ID`       BIGINT              NOT NULL    AUTO_INCREMENT    COMMENT '댓글 좋아요 아이디(PK)',
    `COMMENT_ID`                  BIGINT              NOT NULL                      COMMENT '댓글 아이디(FK)',
    `USER_ID`                     VARCHAR(255)                                      COMMENT '사용자 아이디(FK)',
    `SOCIAL_USER_ID`              VARCHAR(255)                                      COMMENT '소셜 사용자 아이디(FK)',

    -- 시스템 칼럼
    `CREATED_AT`	            DATETIME	        NOT NULL                      COMMENT '생성일자',
    `CREATED_BY`	            VARCHAR(50)		    NOT NULL                      COMMENT '생성자',
    `MODIFIED_AT`	            DATETIME	        NOT NULL                      COMMENT '수정일자',
    `MODIFIED_BY`	            VARCHAR(50)		    NOT NULL                      COMMENT '수정자',

    -- FK 참조 : 댓글, 회원
    CONSTRAINT FK_USER_LIKE_COMMENTS_COMMENT_ID FOREIGN KEY (COMMENT_ID) REFERENCES `comments`(COMMENT_ID), -- FK 참조 : 댓글
    CONSTRAINT FK_USER_LIKE_COMMENTS_USER_ID FOREIGN KEY (USER_ID) REFERENCES `users`(USER_ID), -- FK 참조 : 회원
    CONSTRAINT FK_USER_LIKE_COMMENTS_SOCIAL_USER_ID FOREIGN KEY (SOCIAL_USER_ID) REFERENCES `social_users`(SOCIAL_USER_ID), -- FK 참조 : 소셜 회원

    PRIMARY KEY (USERS_LIKE_COMMENT_ID)
);

-- 댓글 싫어요
DROP TABLE IF EXISTS `user_dislike_comments`;
CREATE TABLE `user_dislike_comments` (
    `USERS_DISLIKE_COMMENT_ID`    BIGINT              NOT NULL    AUTO_INCREMENT    COMMENT '댓글 싫어요 아이디(PK)',
    `COMMENT_ID`                  BIGINT              NOT NULL                      COMMENT '댓글 아이디(FK)',
    `USER_ID`                     VARCHAR(255)                                      COMMENT '사용자 아이디(FK)',
    `SOCIAL_USER_ID`              VARCHAR(255)                                      COMMENT '소셜 사용자 아이디(FK)',

    -- 시스템 칼럼
    `CREATED_AT`	            DATETIME	        NOT NULL                      COMMENT '생성일자',
    `CREATED_BY`	            VARCHAR(50)		    NOT NULL                      COMMENT '생성자',
    `MODIFIED_AT`	            DATETIME	        NOT NULL                      COMMENT '수정일자',
    `MODIFIED_BY`	            VARCHAR(50)		    NOT NULL                      COMMENT '수정자',

    -- FK 참조 : 댓글, 회원

    CONSTRAINT FK_USER_DISLIKE_COMMENTS_COMMENT_ID FOREIGN KEY (COMMENT_ID) REFERENCES `comments`(COMMENT_ID), -- FK 참조 : 댓글
    CONSTRAINT FK_USER_DISLIKE_COMMENTS_USER_ID FOREIGN KEY (USER_ID) REFERENCES `users`(USER_ID), -- FK 참조 : 회원
    CONSTRAINT FK_USER_DISLIKE_COMMENTS_SOCIAL_USER_ID FOREIGN KEY (SOCIAL_USER_ID) REFERENCES `social_users`(SOCIAL_USER_ID), -- FK 참조 : 소셜 회원

    PRIMARY KEY (USERS_DISLIKE_COMMENT_ID)
);

-- 📌여기 작업 중
-- 구독권
DROP TABLE IF EXISTS `subscriptions`;
CREATE TABLE `subscriptions` (
    `SUBSCRIPTION_ID`             VARCHAR(255)        NOT NULL                      COMMENT '구독권 아이디(PK)',
    `SUBSCRIPTION_NAME`           VARCHAR(255)        NOT NULL                      COMMENT '구독권 이름',
    `SUBSCRIPTION_CONTENT`        TEXT                NOT NULL                      COMMENT '구독권 내용',
    `VIEW_CNT`                    INT                 NOT NULL    DEFAULT 0         COMMENT '구독자 전용 게시글 최대 조회 가능 횟수',
    `DOWN_CNT`                    INT                 NOT NULL    DEFAULT 0         COMMENT '구독자 전용 게시글 최대 다운로드 가능 횟수',

    -- 시스템 칼럼
    `CREATED_AT`                  DATETIME            NOT NULL                      COMMENT '생성일자',
    `CREATED_BY`                  VARCHAR(50)         NOT NULL                      COMMENT '생성자',
    `MODIFIED_AT`                 DATETIME            NOT NULL                      COMMENT '수정일자',
    `MODIFIED_BY`                 VARCHAR(50)         NOT NULL                      COMMENT '수정자',

    PRIMARY KEY (SUBSCRIPTION_ID)
);


-- 구독 가격 테이블
DROP TABLE IF EXISTS `subscription_price`;
CREATE TABLE `subscription_price` (
    `PRICE_ID`          BIGINT AUTO_INCREMENT  NOT NULL COMMENT '자동증분',
    `SUBSCRIPTION_ID`   VARCHAR(255)           NOT NULL COMMENT '구독 ID (FK)',
    `PRICE`             INT                    NOT NULL COMMENT '기본 가격',
    `DISC_RATE`         FLOAT                  NOT NULL DEFAULT 1 COMMENT '할인율 (기본값 1)',
    `SALE_PRICE`        INT                    NOT NULL COMMENT '할인가',
    `VALID_YN`          TINYINT                NOT NULL COMMENT '사용 가능 여부',
    `START_AT`          DATETIME               NOT NULL COMMENT '시작일',
    `END_AT`            DATETIME               NOT NULL COMMENT '종료일',

    -- 시스템 칼럼
    `CREATED_AT`        DATETIME               NOT NULL COMMENT '생성일자',
    `CREATED_BY`        VARCHAR(50)            NOT NULL COMMENT '생성자',
    `MODIFIED_AT`       DATETIME               NOT NULL COMMENT '수정일자',
    `MODIFIED_BY`       VARCHAR(50)            NOT NULL COMMENT '수정자',

    -- 참조 테이블
    CONSTRAINT FK_SUBSCRIPTION_PRICE_SUBSCRIPTION_ID FOREIGN KEY (SUBSCRIPTION_ID) REFERENCES `subscriptions`(SUBSCRIPTION_ID), -- FK 참조 : 회원

    PRIMARY KEY (`PRICE_ID`)
);

-- 구독 내역 테이블
DROP TABLE IF EXISTS `subscriptions_history`;
CREATE TABLE `subscriptions_history` (
    `HISTORY_ID`        BIGINT AUTO_INCREMENT  NOT NULL COMMENT '자동증분',
    `SUBSCRIPTION_ID`   VARCHAR(255)           NOT NULL COMMENT '구독 ID (FK)',
    `START_AT`          DATETIME               NOT NULL COMMENT '시작일',
    `END_AT`            DATETIME               NOT NULL COMMENT '종료일',
    `SUBSCRIPTION_NAME` VARCHAR(255)           NOT NULL COMMENT '구독 이름',
    `DOWN_CNT`          INT                    NOT NULL COMMENT '다운로드 횟수',
    `VIEW_CNT`         INT                    NOT NULL COMMENT '시청 횟수',
    `SUBSCRIPTION_DESC` TEXT                   NOT NULL COMMENT '구독 설명',

    -- 시스템 칼럼
    `CREATED_AT`        DATETIME               NOT NULL COMMENT '생성일자',
    `CREATED_BY`        VARCHAR(50)            NOT NULL COMMENT '생성자',
    `MODIFIED_AT`       DATETIME               NOT NULL COMMENT '수정일자',
    `MODIFIED_BY`       VARCHAR(50)            NOT NULL COMMENT '수정자',

    -- 참조 테이블
    CONSTRAINT FK_SUBSCRIPTION_HISTORY_SUBSCRIPTION_ID FOREIGN KEY (SUBSCRIPTION_ID) REFERENCES `subscriptions`(SUBSCRIPTION_ID), -- FK 참조 : 회원

    PRIMARY KEY (`HISTORY_ID`)
);

-- 구독 상태 테이블
DROP TABLE IF EXISTS `subscription_status`;
CREATE TABLE `subscription_status` (
    `STATUS_ID`         BIGINT AUTO_INCREMENT  NOT NULL COMMENT '자동증분',
    `SUBSCRIPTION_ID`   VARCHAR(255)           NOT NULL COMMENT '구독 ID (FK)',
    `SUBSCRIPTION_STATUS` INT                 NOT NULL COMMENT '구독 상태 코드',
    `START_AT`          DATETIME               NOT NULL COMMENT '시작일',
    `END_AT`            DATETIME               NOT NULL COMMENT '종료일',
    `VALID_YN`          TINYINT                NOT NULL COMMENT '사용 가능 여부',

    -- 시스템 칼럼
    `CREATED_AT`        DATETIME               NOT NULL COMMENT '생성일자',
    `CREATED_BY`        VARCHAR(50)            NOT NULL COMMENT '생성자',
    `MODIFIED_AT`       DATETIME               NOT NULL COMMENT '수정일자',
    `MODIFIED_BY`       VARCHAR(50)            NOT NULL COMMENT '수정자',

    -- 참조 테이블
    CONSTRAINT FK_SUBSCRIPTION_STATUS_SUBSCRIPTION_ID FOREIGN KEY (SUBSCRIPTION_ID) REFERENCES `subscriptions`(SUBSCRIPTION_ID), -- FK 참조 : 회원

    PRIMARY KEY (`STATUS_ID`)
);


-- 회원 보유 구독권
DROP TABLE IF EXISTS `user_subscriptions`;
CREATE TABLE `user_subscriptions` (
    `USER_SUBSCRIPTION_ID`      VARCHAR(255)            NOT NULL    COMMENT '회원 구독권 아이디(UUID)(pk)',
    `USER_ID`                   VARCHAR(255)                        COMMENT '회원 아이디(FK)',
    `SOCIAL_USER_ID`            VARCHAR(255)                        COMMENT '소셜 회원 아이디(FK)',
    `SUBSCRIPTION_ID`           VARCHAR(255)            NOT NULL    COMMENT '구독권 아이디(UUID)(FK)',
    `SUBSCRIPTION_NAME`         VARCHAR(255)            NOT NULL    COMMENT '구독권명',
    `START_AT`                  DATETIME                NOT NULL    COMMENT '구독권 적용 시작 시점',
    `END_AT`                    DATETIME                NOT NULL    COMMENT '구독권 적용 종료 시점',
    `VALID_YN`                  TINYINT(1)              NOT NULL    COMMENT '구독권 사용 가능 여부(0: 사용가능, 1: 사용불가능)',

    -- 시스템 칼럼
    `CREATED_AT`	            DATETIME	            NOT NULL    COMMENT '생성일자',
    `CREATED_BY`	            VARCHAR(50)		        NOT NULL    COMMENT '생성자',
    `MODIFIED_AT`	            DATETIME	            NOT NULL    COMMENT '수정일자',
    `MODIFIED_BY`	            VARCHAR(50)		        NOT NULL    COMMENT '수정자',

    -- FK 참조 : 회원, 구독
    CONSTRAINT FK_SUBSCRIPTIONS_USER_ID FOREIGN KEY (USER_ID) REFERENCES `users`(USER_ID), -- FK 참조 : 회원
    CONSTRAINT FK_SUBSCRIPTIONS_SOCIAL_USER_ID FOREIGN KEY (SOCIAL_USER_ID) REFERENCES `social_users`(SOCIAL_USER_ID), -- FK 참조 : 회원
    CONSTRAINT FK_SUBSCRIPTIONS_SUBSCRIPTION_ID FOREIGN KEY (SUBSCRIPTION_ID) REFERENCES `subscriptions`(SUBSCRIPTION_ID), -- FK 참조 : 구독

    PRIMARY KEY (USER_SUBSCRIPTION_ID)
);

-- 시크릿(구독자 전용 게시글)
DROP TABLE IF EXISTS `secretes`;
CREATE TABLE `secretes` (
    `SECRETE_ID`                  BIGINT              NOT NULL    AUTO_INCREMENT              COMMENT '시크릿(구독자 전용 게시글) 아이디(PK)',
    `SECRETE_TITLE`               VARCHAR(255)        NOT NULL                                COMMENT '시크릿 타이틀',
    `SECRETE_CONTENT`             TEXT                NOT NULL                                COMMENT '시크릿 내용',
    `SECRETE_AUTHOR`              VARCHAR(255)        NOT NULL                                COMMENT '시크릿 작성자',
    `SECRETE_THUMBNAIL_URL`       VARCHAR(255)        NOT NULL                                COMMENT '시크릿 섬네일 url',
    `SECRETE_VIEW_CNT`            INT                 NOT NULL    DEFAULT 0                   COMMENT '시크릿 조회수',
    `SECRETE_LIKE_CNT`            INT                 NOT NULL    DEFAULT 0                   COMMENT '시크릿 좋아요 수',
    `SECRETE_DISLIKE_CNT`         INT                 NOT NULL    DEFAULT 0                   COMMENT '시크릿 싫어요 수',

    -- 시스템 칼럼
    `CREATED_AT`                  DATETIME            NOT NULL                                COMMENT '생성일자',
    `CREATED_BY`                  VARCHAR(50)         NOT NULL                                COMMENT '생성자',
    `MODIFIED_AT`                 DATETIME            NOT NULL                                COMMENT '수정일자',
    `MODIFIED_BY`                 VARCHAR(50)         NOT NULL                                COMMENT '수정자',

     PRIMARY KEY (SECRETE_ID)
);

-- 시크릿 태그
DROP TABLE IF EXISTS `secret_tags`;
CREATE TABLE `secret_tags` (
    `TAG_ID`                    BIGINT              NOT NULL   AUTO_INCREMENT       COMMENT '태그 아이디(PK)',
    `SECRETE_ID`                BIGINT              NOT NULL                        COMMENT '구독자 전용 게시글 아이디(FK)',
    `TAG_NAME`                  VARCHAR(50)         NOT NULL                        COMMENT '태그명',

    -- 시스템 칼럼
    `CREATED_AT`	            DATETIME	        NOT NULL                        COMMENT '생성일자',
    `CREATED_BY`	            VARCHAR(50)		    NOT NULL                        COMMENT '생성자',
    `MODIFIED_AT`	            DATETIME	        NOT NULL                        COMMENT '수정일자',
    `MODIFIED_BY`	            VARCHAR(50)		    NOT NULL                        COMMENT '수정자',

    CONSTRAINT `FK_TAGS_SECRET_ID` FOREIGN KEY (`SECRETE_ID`) REFERENCES `secretes`(`SECRETE_ID`) ON DELETE CASCADE,

    PRIMARY KEY (TAG_ID)
);

-- 시크릿(구독자 전용 게시글) 다운로드
DROP TABLE IF EXISTS `download_secrets`;
CREATE TABLE `download_secrets` (
    `USER_DOWN_ID`                VARCHAR(255)        NOT NULL                    COMMENT '회원 구독자 전용 게시글 다운로드 아이디(PK)',
    `USER_ID`                     VARCHAR(255)        NULL                        COMMENT '회원 아이디(FK)',
    `SOCIAL_USER_ID`              VARCHAR(255)        NULL                        COMMENT '소셜 회원 아이디(FK)',

    `SECRETE_ID`                  BIGINT              NOT NULL                    COMMENT '구독자 전용 게시글 아이디(FK)',

    -- 시스템 칼럼
    `CREATED_AT`                  DATETIME            NOT NULL                    COMMENT '생성일자',
    `CREATED_BY`                  VARCHAR(50)         NOT NULL                    COMMENT '생성자',
    `MODIFIED_AT`                 DATETIME            NOT NULL                    COMMENT '수정일자',
    `MODIFIED_BY`                 VARCHAR(50)         NOT NULL                    COMMENT '수정자',

    -- FK 참조 : 시크릿, 사용자
    CONSTRAINT FK_DOWNLOAD_SECRETS_USER_ID FOREIGN KEY (`USER_ID`) REFERENCES `users`(`USER_ID`) ON DELETE CASCADE,
    CONSTRAINT FK_DOWNLOAD_SECRETS_SOCIAL_USER_ID FOREIGN KEY (`SOCIAL_USER_ID`) REFERENCES `social_users`(`SOCIAL_USER_ID`) ON DELETE CASCADE,
    CONSTRAINT FK_DOWNLOAD_SECRETS_SECRETE_ID FOREIGN KEY (`SECRETE_ID`) REFERENCES `secretes`(`SECRETE_ID`) ON DELETE CASCADE,

    PRIMARY KEY (USER_DOWN_ID)
);

-- 구독자 전용 게시글 이력 테이블
DROP TABLE IF EXISTS `secret_history`;
CREATE TABLE `secret_history` (
    `HISTORY_ID`       BIGINT AUTO_INCREMENT   NOT NULL COMMENT '자동증분',
    `SECRETE_ID`       BIGINT                  NOT NULL COMMENT '비밀 ID (FK)',
    `VALID_STEP`       INT                     NOT NULL COMMENT '검증 단계 (0: 검증대기, 1:검증진행, 2:검증실패, 3:검증성공)',

    -- 시스템 칼럼
    `CREATED_AT`       DATETIME                NOT NULL COMMENT '생성일자',
    `CREATED_BY`       VARCHAR(50)             NOT NULL COMMENT '생성자',
    `MODIFIED_AT`      DATETIME                NOT NULL COMMENT '수정일자',
    `MODIFIED_BY`      VARCHAR(50)             NOT NULL COMMENT '수정자',

    CONSTRAINT FK_SECRET_HISTORY_SECRET_ID FOREIGN KEY (`SECRETE_ID`) REFERENCES `secretes`(`SECRETE_ID`) ON DELETE CASCADE,

     PRIMARY KEY (`HISTORY_ID`)
);

-- 구독자 전용 게시글 베스트 테이블
DROP TABLE IF EXISTS `best_secrets`;
CREATE TABLE `best_secrets` (
    `BEST_SECRET_ID`   BIGINT          NOT NULL    AUTO_INCREMENT COMMENT '베스트 비밀글 아이디(PK)',
    `SECRETE_ID`        BIGINT          NOT NULL                  COMMENT '비밀글 아이디(FK)',
    `START_AT`         DATETIME        NOT NULL                   COMMENT '베스트 비밀글 적용 시작 시간',
    `END_AT`           DATETIME        NOT NULL                   COMMENT '베스트 비밀글 적용 종료 시간',

    -- 비밀글 정보
    `SECRET_TITLE`     VARCHAR(255)    NOT NULL                   COMMENT '비밀글 제목',
    `SECRET_SUMMARY`   VARCHAR(500)    NOT NULL                   COMMENT '비밀글 요약',
    `SECRET_AUTHOR`    VARCHAR(255)    NOT NULL                   COMMENT '비밀글 작성자',

    -- 태그
    `TAG1`            VARCHAR(25)      NULL                       COMMENT '태그 1',
    `TAG2`            VARCHAR(25)      NULL                       COMMENT '태그 2',
    `TAG3`            VARCHAR(25)      NULL                       COMMENT '태그 3',

    -- 시스템 칼럼
    `CREATED_AT`       DATETIME        NOT NULL                   COMMENT '생성일자',
    `CREATED_BY`       VARCHAR(50)     NOT NULL                   COMMENT '생성자',
    `MODIFIED_AT`      DATETIME        NOT NULL                   COMMENT '수정일자',
    `MODIFIED_BY`      VARCHAR(50)     NOT NULL                   COMMENT '수정자',

    -- FK 참조 : 비밀글
    CONSTRAINT `FK_BEST_SECRETS_SECRET_ID` FOREIGN KEY (`SECRETE_ID`) REFERENCES `secretes`(`SECRETE_ID`) ON DELETE CASCADE,

    PRIMARY KEY (`BEST_SECRET_ID`)
);

-- 주문
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
    `ORDER_ID`                   VARCHAR(255)          NOT NULL                                COMMENT '주문 아이디(UUID)(PK)',
    `USER_ID`                    VARCHAR(255)          NOT NULL                                COMMENT '회원 아이디(UUID)(FK)',
    `SOCIAL_USER_ID`             VARCHAR(255)          NOT NULL                                COMMENT '소셜 회원 아이디(UUID)(FK)',
    `SUBSCRIPTION_ID`            VARCHAR(255)          NOT NULL                                COMMENT '구독 아이디(UUID)(FK)',
    `ORDER_TOT_AMOUNT`           INT                   NOT NULL         DEFAULT 0              COMMENT '총 주문 금액',
    `DISC_TOT_AMOUNT`            INT                   NOT NULL         DEFAULT 0              COMMENT '총 할인 금액',
    `PAID_TOT_AMOUNT`            INT                   NOT NULL         DEFAULT 0              COMMENT '총 결제 금액',
    `ORDERED_AT`                 DATETIME              NOT NULL                                COMMENT '주문일자',

    -- 시스템 칼럼
    `CREATED_AT`                 DATETIME              NOT NULL                                COMMENT '생성일자',
    `CREATED_BY`                 VARCHAR(50)           NOT NULL                                COMMENT '생성자',
    `MODIFIED_AT`                DATETIME              NOT NULL                                COMMENT '수정일자',
    `MODIFIED_BY`                VARCHAR(50)           NOT NULL                                COMMENT '수정자',

    -- FK 참조 : 사용자, 구독
    CONSTRAINT FK_ORDERS_USER_ID FOREIGN KEY (`USER_ID`) REFERENCES `users`(`USER_ID`) ON DELETE CASCADE,
    CONSTRAINT FK_ORDERS_SOCIAL_USER_ID FOREIGN KEY (`SOCIAL_USER_ID`) REFERENCES `social_users`(`SOCIAL_USER_ID`) ON DELETE CASCADE,
    CONSTRAINT FK_ORDERS_SUBSCRIPTION_ID FOREIGN KEY (`SUBSCRIPTION_ID`) REFERENCES `subscriptions`(`SUBSCRIPTION_ID`) ON DELETE CASCADE,

    PRIMARY KEY (ORDER_ID)
);

-- 주문 상태
DROP TABLE IF EXISTS `orders_status`;
CREATE TABLE `orders_status` (
    `ORDER_STATUS_ID`            VARCHAR(255)          NOT NULL                                COMMENT '주문 상태 아이디(UUID)(PK)',
    `ORDER_ID`                   VARCHAR(255)          NOT NULL                                COMMENT '주문 아이디(UUID)(FK)',
    `ORDER_STATUS`               VARCHAR(255)          NOT NULL                                COMMENT '주문 상태',
    `SUBSCRIPTION_ID`            VARCHAR(255)          NOT NULL                                COMMENT '구독권 아이디(UUID)(FK)',
    `ORDER_TOT_AMOUNT`           INT                   NOT NULL        DEFAULT 0               COMMENT '총 주문 금액',
    `DISC_TOT_AMOUNT`            INT                   NOT NULL        DEFAULT 0               COMMENT '총 할인 금액',
    `PAID_TOT_AMOUNT`            INT                   NOT NULL        DEFAULT 0               COMMENT '총 결제 금액',
    `ORDERED_AT`                 DATETIME              NOT NULL                                COMMENT '주문일자',

    -- 시스템 칼럼
    `CREATED_AT`                 DATETIME              NOT NULL                                COMMENT '생성일자',
    `CREATED_BY`                 VARCHAR(50)           NOT NULL                                COMMENT '생성자',
    `MODIFIED_AT`                DATETIME              NOT NULL                                COMMENT '수정일자',
    `MODIFIED_BY`                VARCHAR(50)           NOT NULL                                COMMENT '수정자',

    -- FK 참조 : 주문, 구독
    CONSTRAINT FK_ORDERS_STATUS_ORDER_ID FOREIGN KEY (`ORDER_ID`) REFERENCES `orders`(`ORDER_ID`) ON DELETE CASCADE,
    CONSTRAINT FK_ORDERS_STATUS_SUBSCRIPTION_ID FOREIGN KEY (`SUBSCRIPTION_ID`) REFERENCES `subscriptions`(`SUBSCRIPTION_ID`) ON DELETE CASCADE,

    PRIMARY KEY (ORDER_STATUS_ID)
);

-- 주문 이력
DROP TABLE IF EXISTS `orders_history`;
CREATE TABLE `orders_history` (
    `ORDER_HISTORY_ID`           VARCHAR(255)          NOT NULL                                COMMENT '주문 이력 아이디(UUID)(PK)',
    `ORDER_ID`                   VARCHAR(255)          NOT NULL                                COMMENT '주문 아이디(UUID)(FK)',
    `SUBSCRIPTION_ID`            VARCHAR(255)          NOT NULL                                COMMENT '구독권 아이디(UUID)(FK)',
    `FIN_ORDER_STATUS`           VARCHAR(255)          NOT NULL                                COMMENT '주문 최종 상태',
    `ORD_TOT_AMOUNT`             INT                   NOT NULL        DEFAULT 0               COMMENT '최종 주문 금액',
    `DISC_TOT_AMOUNT`            INT                   NOT NULL        DEFAULT 0               COMMENT '최종 할인 금액',
    `PAID_TOT_AMOUNT`            INT                   NOT NULL        DEFAULT 0               COMMENT '최종 결제 금액',
    `ORDERED_AT`                 DATETIME              NOT NULL                                COMMENT '주문일자',

    -- 시스템 칼럼
    `CREATED_AT`                 DATETIME              NOT NULL                                COMMENT '생성일자',
    `CREATED_BY`                 VARCHAR(50)           NOT NULL                                COMMENT '생성자',
    `MODIFIED_AT`                DATETIME              NOT NULL                                COMMENT '수정일자',
    `MODIFIED_BY`                VARCHAR(50)           NOT NULL                                COMMENT '수정자',

    -- FK 참조 : 주문, 구독
    CONSTRAINT FK_ORDERS_HISTORY_ORDER_ID FOREIGN KEY (`ORDER_ID`) REFERENCES `orders`(`ORDER_ID`) ON DELETE CASCADE,
    CONSTRAINT FK_ORDERS_HISTORY_SUBSCRIPTION_ID FOREIGN KEY (`SUBSCRIPTION_ID`) REFERENCES `subscriptions`(`SUBSCRIPTION_ID`) ON DELETE CASCADE,

    PRIMARY KEY (ORDER_HISTORY_ID)
);

-- 결제
DROP TABLE IF EXISTS `payments`;
CREATE TABLE `payments` (
    `PAYMENT_ID`                 VARCHAR(255)          NOT NULL                                COMMENT '결제 아이디(UUID)(PK)',
    `USER_ID`                    VARCHAR(255)          NULL                                    COMMENT '사용자 아이디(UUID)(FK)',
    `SOCIAL_USER_ID`             VARCHAR(255)          NULL                                    COMMENT '소셜 사용자 아이디(UUID)(FK)',
    `ORDER_ID`                   VARCHAR(255)          NOT NULL                                COMMENT '주문 아이디(UUID)(FK)',
    `PAYMENT_AMOUNT`             INT                   NOT NULL         DEFAULT 0              COMMENT '결제 금액',
    `PAYMENT_CODE`               VARCHAR(255)          NOT NULL                                COMMENT '결제 코드',
    `CARD_APPR_CODE`             VARCHAR(255)          NOT NULL                                COMMENT '카드 결제 승인 코드',
    `CARD_CANC_CODE`             VARCHAR(255)          NOT NULL                                COMMENT '카드 결제 취소 코드',
    `PAID_AT`                    DATETIME              NOT NULL                                COMMENT '결제일자',
    `PAID_STAT`                  VARCHAR(255)          NOT NULL                                COMMENT '결제 상태 코드',

    -- 시스템 칼럼
    `CREATED_AT`                 DATETIME              NOT NULL                                COMMENT '생성일자',
    `CREATED_BY`                 VARCHAR(50)           NOT NULL                                COMMENT '생성자',
    `MODIFIED_AT`                DATETIME              NOT NULL                                COMMENT '수정일자',
    `MODIFIED_BY`                VARCHAR(50)           NOT NULL                                COMMENT '수정자',

    -- FK 참조 : 회원, 주문
    CONSTRAINT FK_PAYMENTS_USER_ID FOREIGN KEY (`USER_ID`) REFERENCES `users`(`USER_ID`) ON DELETE CASCADE,
    CONSTRAINT FK_PAYMENTS_SOCIAL_USER_ID FOREIGN KEY (`SOCIAL_USER_ID`) REFERENCES `social_users`(`SOCIAL_USER_ID`) ON DELETE CASCADE,
    CONSTRAINT FK_PAYMENTS_ORDER_ID FOREIGN KEY (`ORDER_ID`) REFERENCES `orders`(`ORDER_ID`) ON DELETE CASCADE,

    PRIMARY KEY (PAYMENT_ID)
);

-- 결제 이력
DROP TABLE IF EXISTS `payments_history`;
CREATE TABLE `payments_history` (
    `PAYMENT_HISTORY_ID`         VARCHAR(255)          NOT NULL                                COMMENT '결제 이력 아이디(UUID)(PK)',
    `PAYMENT_ID`                 VARCHAR(255)          NOT NULL                                COMMENT '결제 아이디(UUID)(FK)',
    `PAYMENT_STAT`               VARCHAR(255)          NOT NULL                                COMMENT '결제 상태',
    `PAYMENT_AMOUNT`             INT                   NOT NULL         DEFAULT 0              COMMENT '결제 금액',
    `PAYMENT_CODE`               VARCHAR(255)          NOT NULL                                COMMENT '결제 코드',
    `CARD_APPR_CODE`             VARCHAR(255)          NOT NULL                                COMMENT '카드 결제 승인 코드',
    `CARD_CANC_CODE`             VARCHAR(255)          NOT NULL                                COMMENT '카드 결제 취소 코드',
    `PAID_AT`                    VARCHAR(255)          NOT NULL                                COMMENT '결제일자',

    -- 시스템 칼럼
    `CREATED_AT`                 DATETIME              NOT NULL                                COMMENT '생성일자',
    `CREATED_BY`                 VARCHAR(50)           NOT NULL                                COMMENT '생성자',
    `MODIFIED_AT`                DATETIME              NOT NULL                                COMMENT '수정일자',
    `MODIFIED_BY`                VARCHAR(50)           NOT NULL                                COMMENT '수정자',

    -- FK 참조 : 결제
    CONSTRAINT FK_PAYMENTS_HISTORY_PAYMENT_ID FOREIGN KEY (`PAYMENT_ID`) REFERENCES `payments`(`PAYMENT_ID`) ON DELETE CASCADE,

    PRIMARY KEY (PAYMENT_HISTORY_ID)
);







