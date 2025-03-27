DELETE FROM `categories`;

INSERT INTO `categories` (`CATEGORY_ID`, `TOP_CATEGORY_ID`, `CATEGORY_NAME`, `CATEGORY_ORD`, `VALID_YN`, `CREATED_AT`, `CREATED_BY`, `MODIFIED_AT`, `MODIFIED_BY`)
VALUES
    ('cate_000', '-', '전체', 1, 0, NOW(), 'system', NOW(), 'system'),   -- '전체' 카테고리
    (UUID(), 'cate_000', '일상', 2, 0, NOW(), 'system', NOW(), 'system'),   -- '일상' 카테고리
    (UUID(), 'cate_000', '꿀 팁', 3, 0, NOW(), 'system', NOW(), 'system'),   -- '꿀 팁' 카테고리
    (UUID(), 'cate_000', 'IT 커리어', 4, 0, NOW(), 'system', NOW(), 'system'),  -- 'IT 커리어' 카테고리
    (UUID(), 'cate_000', '공부', 5, 0, NOW(), 'system', NOW(), 'system'),   -- '공부' 카테고리
    (UUID(), 'cate_000', '취미', 6, 0, NOW(), 'system', NOW(), 'system');  -- '취미' 카테고리


INSERT INTO `categories` (`CATEGORY_ID`, `TOP_CATEGORY_ID`, `CATEGORY_NAME`, `CATEGORY_ORD`, `VALID_YN`, `CREATED_AT`, `CREATED_BY`, `MODIFIED_AT`, `MODIFIED_BY`)
VALUES
    -- 중분류 카테고리
    ('CT0001', '-', '게시글', 1, 0, NOW(), 'system', NOW(), 'system'),
    ('CT0002', '-', '프로필', 2, 0, NOW(), 'system', NOW(), 'system'),
    ('CT0003', '-', '시크릿', 3, 0, NOW(), 'system', NOW(), 'system'),

    -- 게시글 소분류
    (UUID(), 'CT0001', '전체', 1, 0, NOW(), 'system', NOW(), 'system'),
    (UUID(), 'CT0001', '일상', 2, 0, NOW(), 'system', NOW(), 'system'),
    (UUID(), 'CT0001', '꿀팁', 3, 0, NOW(), 'system', NOW(), 'system'),
    (UUID(), 'CT0001', '커리어', 4, 0, NOW(), 'system', NOW(), 'system'),
    (UUID(), 'CT0001', '학습법', 5, 0, NOW(), 'system', NOW(), 'system'),
    (UUID(), 'CT0001', '프로젝트 모집', 6, 0, NOW(), 'system', NOW(), 'system'),

    -- 프로필 소분류
    (UUID(), 'CT0002', '작성한 글', 1, 0, NOW(), 'system', NOW(), 'system'),
    (UUID(), 'CT0002', '좋아요', 2, 0, NOW(), 'system', NOW(), 'system'),
    (UUID(), 'CT0002', '프로젝트 일정', 3, 0, NOW(), 'system', NOW(), 'system'),

    -- 시크릿 소분류
    (UUID(), 'CT0003', '프론트엔드', 1, 0, NOW(), 'system', NOW(), 'system'),
    (UUID(), 'CT0003', '백엔드', 2, 0, NOW(), 'system', NOW(), 'system'),
    (UUID(), 'CT0003', 'DevOps', 3, 0, NOW(), 'system', NOW(), 'system'),
    (UUID(), 'CT0003', '인프라', 4, 0, NOW(), 'system', NOW(), 'system'),
    (UUID(), 'CT0003', 'ML/DL', 5, 0, NOW(), 'system', NOW(), 'system'),
    (UUID(), 'CT0003', 'AWS', 6, 0, NOW(), 'system', NOW(), 'system');
