# 구독권, 가격 더미 데이터 생성
-- 1~3월
INSERT INTO subscription_price (
    SUBSCRIPTION_ID, PRICE, DISC_RATE, SALE_PRICE, VALID_YN,
    START_AT, END_AT, CREATED_AT, CREATED_BY, MODIFIED_AT, MODIFIED_BY
) VALUES (
             '21094738-94af-4739-abe1-6cf9f9773e38', 7000, 0.1, 6300, 1,
             '2025-01-01 00:00:00', '2025-03-31 23:59:59',
             NOW(), 'system', NOW(), 'system'
         );

-- 3~6월
INSERT INTO subscription_price (
    SUBSCRIPTION_ID, PRICE, DISC_RATE, SALE_PRICE, VALID_YN,
    START_AT, END_AT, CREATED_AT, CREATED_BY, MODIFIED_AT, MODIFIED_BY
) VALUES (
             '21094738-94af-4739-abe1-6cf9f9773e38', 7000, 0.2, 5600, 1,
             '2025-04-01 00:00:00', '2025-06-30 23:59:59',
             NOW(), 'system', NOW(), 'system'
         );

-- 6~9월
INSERT INTO subscription_price (
    SUBSCRIPTION_ID, PRICE, DISC_RATE, SALE_PRICE, VALID_YN,
    START_AT, END_AT, CREATED_AT, CREATED_BY, MODIFIED_AT, MODIFIED_BY
) VALUES (
             '21094738-94af-4739-abe1-6cf9f9773e38', 7000, 0.1, 6300, 1,
             '2025-07-01 00:00:00', '2025-09-30 23:59:59',
             NOW(), 'system', NOW(), 'system'
         );

-- 9~12월
INSERT INTO subscription_price (
    SUBSCRIPTION_ID, PRICE, DISC_RATE, SALE_PRICE, VALID_YN,
    START_AT, END_AT, CREATED_AT, CREATED_BY, MODIFIED_AT, MODIFIED_BY
) VALUES (
             '21094738-94af-4739-abe1-6cf9f9773e38', 7000, 0.2, 5600, 1,
             '2025-10-01 00:00:00', '2025-12-31 23:59:59',
             NOW(), 'system', NOW(), 'system'
         );

-- 구독권 상태 -> 상태 부분
-- 1~3월
INSERT INTO subscription_status (
    SUBSCRIPTION_ID, SUBSCRIPTION_STATUS, START_AT, END_AT, VALID_YN,
    CREATED_AT, CREATED_BY, MODIFIED_AT, MODIFIED_BY
) VALUES (
             '21094738-94af-4739-abe1-6cf9f9773e38', 1,
             '2025-01-01 00:00:00', '2025-03-31 23:59:59', 1,
             NOW(), 'system', NOW(), 'system'
         );

-- 4~6월
INSERT INTO subscription_status (
    SUBSCRIPTION_ID, SUBSCRIPTION_STATUS, START_AT, END_AT, VALID_YN,
    CREATED_AT, CREATED_BY, MODIFIED_AT, MODIFIED_BY
) VALUES (
             '21094738-94af-4739-abe1-6cf9f9773e38', 1,
             '2025-04-01 00:00:00', '2025-06-30 23:59:59', 1,
             NOW(), 'system', NOW(), 'system'
         );

-- 7~9월
INSERT INTO subscription_status (
    SUBSCRIPTION_ID, SUBSCRIPTION_STATUS, START_AT, END_AT, VALID_YN,
    CREATED_AT, CREATED_BY, MODIFIED_AT, MODIFIED_BY
) VALUES (
             '21094738-94af-4739-abe1-6cf9f9773e38', 1,
             '2025-07-01 00:00:00', '2025-09-30 23:59:59', 1,
             NOW(), 'system', NOW(), 'system'
         );

-- 10~12월
INSERT INTO subscription_status (
    SUBSCRIPTION_ID, SUBSCRIPTION_STATUS, START_AT, END_AT, VALID_YN,
    CREATED_AT, CREATED_BY, MODIFIED_AT, MODIFIED_BY
) VALUES (
             '21094738-94af-4739-abe1-6cf9f9773e38', 0,
             '2025-10-01 00:00:00', '2025-12-31 23:59:59', 1,
             NOW(), 'system', NOW(), 'system'
         );
