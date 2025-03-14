INSERT INTO subscriptions
VALUES (UUID(), '무료 구독권', '모든 회원에게 제공되는 무료 구독권, 구독 서비스 이용 혜택 없음',
        0, 0, NOW(), 'system', NOW(), 'system');

INSERT INTO subscriptions
VALUES (UUID(), '브론즈 구독권', '브론즈 등급 회원을 위한 구독권, 기본적인 혜택 제공',
        15, 15, NOW(), 'system', NOW(), 'system');

INSERT INTO subscriptions
VALUES (UUID(), '실버 구독권', '실버 등급 회원을 위한 구독권, 추가적인 혜택 제공',
        30, 30, NOW(), 'system', NOW(), 'system');

INSERT INTO subscriptions
VALUES (UUID(), '골드 구독권', '골드 등급 회원을 위한 구독권, 다양한 혜택 제공',
        50, 50, NOW(), 'system', NOW(), 'system');

INSERT INTO subscriptions
VALUES (UUID(), '플래티넘 구독권', '플래티넘 등급 회원을 위한 구독권, 최상의 혜택 제공',
        100, 100, NOW(), 'system', NOW(), 'system');