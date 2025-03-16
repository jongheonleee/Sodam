INSERT INTO subscriptions
VALUES (UUID(), 'FREE', '모든 회원에게 제공되는 무료 구독권, 구독 서비스 이용 혜택 없음',
        0, 0, NOW(), 'system', NOW(), 'system');

INSERT INTO subscriptions
VALUES (UUID(), 'BRONZE', '브론즈 등급 회원을 위한 구독권, 기본적인 혜택 제공',
        15, 15, NOW(), 'system', NOW(), 'system');

INSERT INTO subscriptions
VALUES (UUID(), 'SILVER', '실버 등급 회원을 위한 구독권, 추가적인 혜택 제공',
        30, 30, NOW(), 'system', NOW(), 'system');

INSERT INTO subscriptions
VALUES (UUID(), 'GOLD', '골드 등급 회원을 위한 구독권, 다양한 혜택 제공',
        50, 50, NOW(), 'system', NOW(), 'system');

INSERT INTO subscriptions
VALUES (UUID(), 'PLATINUM', '플래티넘 등급 회원을 위한 구독권, 최상의 혜택 제공',
        100, 100, NOW(), 'system', NOW(), 'system');