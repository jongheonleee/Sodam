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
-- 회원
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
    `USER_ID`                   VARCHAR(255)        NOT NULL    COMMENT '사용자 아이디(UUID)(PK)',
    `POSITION_ID`               VARCHAR(255)                    COMMENT '사용자 포지션(카테고리 참조)(UUID)(FK)',
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

    -- FK 참조 : 카테고리
    CONSTRAINT `FK_USERS_POSITION_ID` FOREIGN KEY (`POSITION_ID`) REFERENCES `categories`(`CATEGORY_ID`) ON DELETE CASCADE,

    PRIMARY KEY (USER_ID)
);

-- 소셜 회원(카카오, 구글, ... 등)
DROP TABLE IF EXISTS `social_users`;
CREATE TABLE `social_users` (
    `SOCIAL_USER_ID`            VARCHAR(255)             NOT NULL    COMMENT '소셜 사용자 ID(UUID)(PK)',
    `POSITION_ID`               VARCHAR(255)                         COMMENT '사용자 포지션(카테고리 참조)(UUID)(FK)',
    `USER_EMAIL`                VARCHAR(255)             NOT NULL    COMMENT '사용자 이메일',
    `USER_INTRODUCE`            TEXT                     NULL        COMMENT '사용자 자기소개글',
    `USER_IMAGE`                VARCHAR(255)             NOT NULL    COMMENT '사용자 프로필 이미지(aws s3에 등록된 url)',
    `USER_NAME`                 VARCHAR(50)              NOT NULL    COMMENT '소셜 사용자 이름',
    `PROVIDER`                  VARCHAR(255)             NOT NULL    COMMENT '소셜 프로바이더 (구글, 카카오, ... 등)',
    `PROVIDER_ID`               VARCHAR(255)             NOT NULL    COMMENT '프로바이더 아이디(FK)',

    -- 시스템 칼럼
    `CREATED_AT`	            DATETIME	             NOT NULL    COMMENT '생성일자',
    `CREATED_BY`	            VARCHAR(50)		         NOT NULL    COMMENT '생성자',
    `MODIFIED_AT`	            DATETIME	             NOT NULL    COMMENT '수정일자',
    `MODIFIED_BY`	            VARCHAR(50)		         NOT NULL    COMMENT '수정자',

    -- FK 참조 : 카테고리
    CONSTRAINT `FK_SOCIAL_USERS_POSITION_ID` FOREIGN KEY (`POSITION_ID`) REFERENCES `categories`(`CATEGORY_ID`) ON DELETE CASCADE,


    PRIMARY KEY (SOCIAL_USER_ID)
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

-- 베스트 게시판
DROP TABLE IF EXISTS `best_articles`;
CREATE TABLE `best_articles` (
    `BEST_ARTICLE_ID`   BIGINT          NOT NULL    AUTO_INCREMENT COMMENT '베스트 게시글 아이디(PK)',
    `ARTICLE_ID`        BIGINT          NOT NULL                   COMMENT '게시글 아이디(FK)',
    `START_AT`          DATETIME        NOT NULL                   COMMENT '베스트 게시글 적용 시작 시간',
    `END_AT`            DATETIME        NOT NULL                   COMMENT '베스트 게시글 적용 종료 시간',

    -- 시스템 칼럼
    `CREATED_AT`	    DATETIME	    NOT NULL                   COMMENT '생성일자',
    `CREATED_BY`	    VARCHAR(50)		NOT NULL                   COMMENT '생성자',
    `MODIFIED_AT`	    DATETIME	    NOT NULL                   COMMENT '수정일자',
    `MODIFIED_BY`	    VARCHAR(50)		NOT NULL                   COMMENT '수정자',

    -- FK 참조 : 게시글
    CONSTRAINT `FK_BEST_ARTICLES_ARTICLE_ID` FOREIGN KEY (`ARTICLE_ID`) REFERENCES `articles`(`ARTICLE_ID`) ON DELETE CASCADE,

    PRIMARY KEY (BEST_ARTICLE_ID)
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
    `USER_DOWN_ID`                VARCHAR(255)        NOT NULL                                                          COMMENT '회원 구독자 전용 게시글 다운로드 아이디(PK)',
    `USER_ID`                     VARCHAR(255)        NOT NULL                                                          COMMENT '회원 아이디(FK)',
    `SECRETE_ID`                  BIGINT              NOT NULL                                                          COMMENT '구독자 전용 게시글 아이디(FK)',

    -- 시스템 칼럼
    `CREATED_AT`                  DATETIME            NOT NULL                    COMMENT '생성일자',
    `CREATED_BY`                  VARCHAR(50)         NOT NULL                    COMMENT '생성자',
    `MODIFIED_AT`                 DATETIME            NOT NULL                    COMMENT '수정일자',
    `MODIFIED_BY`                 VARCHAR(50)         NOT NULL                    COMMENT '수정자',

    -- FK 참조 : 시크릿, 사용자
    CONSTRAINT FK_DOWNLOAD_SECRETS_USER_ID FOREIGN KEY (`USER_ID`) REFERENCES `users`(`USER_ID`) ON DELETE CASCADE,
    CONSTRAINT FK_DOWNLOAD_SECRETS_SECRETE_ID FOREIGN KEY (`SECRETE_ID`) REFERENCES `secretes`(`SECRETE_ID`) ON DELETE CASCADE,

    PRIMARY KEY (USER_DOWN_ID)
);


-- 주문, 결제 관련 테이블

-- 주문
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
    `ORDER_ID`                   VARCHAR(255)          NOT NULL                                COMMENT '주문 아이디(UUID)(PK)',
    `USER_ID`                    VARCHAR(255)          NOT NULL                                COMMENT '회원 아이디(UUID)(FK)',
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
    `USER_ID`                    VARCHAR(255)          NOT NULL                                COMMENT '사용자 아이디(UUID)(FK)',
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







