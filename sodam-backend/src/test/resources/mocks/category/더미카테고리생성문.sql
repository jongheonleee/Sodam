INSERT INTO `categories` (`CATEGORY_ID`, `TOP_CATEGORY_ID`, `CATEGORY_NAME`, `CATEGORY_ORD`, `VALID_YN`, `CREATED_AT`, `CREATED_BY`, `MODIFIED_AT`, `MODIFIED_BY`)
VALUES
    ('cate_000', '-', '전체', 1, 0, NOW(), 'system', NOW(), 'system'),   -- '전체' 카테고리
    (UUID(), 'cate_000', '일상', 2, 0, NOW(), 'system', NOW(), 'system'),   -- '일상' 카테고리
    (UUID(), 'cate_000', '꿀 팁', 3, 0, NOW(), 'system', NOW(), 'system'),   -- '꿀 팁' 카테고리
    (UUID(), 'cate_000', 'IT 커리어', 4, 0, NOW(), 'system', NOW(), 'system'),  -- 'IT 커리어' 카테고리
    (UUID(), 'cate_000', '공부', 5, 0, NOW(), 'system', NOW(), 'system'),   -- '공부' 카테고리
    (UUID(), 'cate_000', '취미', 6, 0, NOW(), 'system', NOW(), 'system');  -- '취미' 카테고리