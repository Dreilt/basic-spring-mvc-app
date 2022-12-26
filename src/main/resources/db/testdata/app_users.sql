INSERT INTO `app_user` (`uuid`, `first_name`, `last_name`, `email`, `password`, `bio`, `city`, `enabled`, `account_non_locked`)
VALUES
    -- admin@example.com / admin
    (RANDOM_UUID(), 'Admin', 'Admin', 'admin@example.com', '{bcrypt}$2a$10$1rXMx0b4caUy/SN3Xg4j4u43gDqVJO/R.zXGCGWc/wr7bsmmSEk2C', 'Cześć! Jestem administratorem tego serwisu.', 'Serwerownia', 1, 1),
    -- jankowalski@example.com / user
    (RANDOM_UUID(), 'Jan', 'Kowalski', 'jankowalski@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 'Cześć! Nazywam się Jan Kowalski i jestem z Rzeszowa. Obecnie jestem na trzecim roku informatyki na Politechnice Rzeszowskiej.', 'Rzeszów', 1, 1),
    -- patrykkowalski@example.com / user
    (RANDOM_UUID(), 'Patryk', 'Kowalski', 'patrykkowalski@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 'Cześć! Nazywam się Patryk Kowalski i jestem z Krakowa. Obecnie jestem na trzecim roku informatyki na Politechnice Krakowskiej.', 'Kraków', 1, 1),
    -- jannowak@example.com / user
    (RANDOM_UUID(), 'Jan', 'Nowak', 'jannowak@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 'Cześć! Nazywam się Jan Nowak i jestem z Rzeszowa. Obecnie jestem na trzecim roku informatyki w Wyższej Szkole Informatyki i Zarządzania w Rzeszowie.', 'Rzeszów', 1, 1),
    -- patryknowak@example.com / user
    (RANDOM_UUID(), 'Patryk', 'Nowak', 'patryknowak@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 'Cześć! Nazywam się Patryk Nowak i jestem z Krakowa. Obecnie jestem na trzecim roku informatyki na Uniwersytecie Jagiellońskim.', 'Kraków', 1, 1),
    -- piotrwysocki@example.com / user
    (RANDOM_UUID(), 'Piotr', 'Wysocki', 'piotrwysocki@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 'Cześć! Nazywam się Piotr Wysocki i jestem z Rzeszowa. Obecnie jestem na czwartym roku informatyki na Politechnice Rzeszowskiej.', 'Rzeszów', 1, 1),
    -- dawidpolak@example.com / user
    (RANDOM_UUID(), 'Dawid', 'Polak', 'dawidpolak@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 'Cześć! Nazywam się Dawid Polak i jestem z Krakowa. Obecnie jestem na czwartym roku informatyki na Politechnice Krakowskiej.', 'Kraków', 1, 1),
    -- zuzannakowalska@example.com / user
    (RANDOM_UUID(), 'Zuzanna', 'Kowalska', 'zuzannakowalska@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 'Cześć! Nazywam się Zuzanna Kowalska i jestem z Rzeszowa. Obecnie jestem na trzecim roku informatyki w Wyższej Szkole Informatyki i Zarządzania w Rzeszowie.', 'Rzeszów', 1, 1),
    -- piotrmichalik@example.com / user
    (RANDOM_UUID(), 'Piotr', 'Michalik', 'piotrmichalik@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 'Cześć! Nazywam się Piotr Michalik i jestem z Krakowa. Obecnie jestem na czwartym roku informatyki na Uniwersytecie Jagiellońskim.', 'Kraków', 1, 1),
    -- dawiddabrowski@example.com / user
    (RANDOM_UUID(), 'Dawid', 'Dąbrowski', 'dawiddabrowski@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 'Cześć! Nazywam się Dawid Dąbrowski i jestem z Rzeszowa. Obecnie jestem na piątym roku informatyki na Politechnice Rzeszowskiej.', 'Rzeszów', 1, 1),
    -- danieldabrowski@example.com / user
    (RANDOM_UUID(), 'Daniel', 'Dąbrowski', 'danieldabrowski@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 'Cześć! Nazywam się Daniel Dąbrowski i jestem z Krakowa. Obecnie jestem na piątym roku informatyki na Politechnice Krakowskiej.', 'Kraków', 1, 1),
    -- marianowak@example.com / user
    (RANDOM_UUID(), 'Maria', 'Nowak', 'marianowak@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 'Cześć! Nazywam się Maria Nowak i jestem z Rzeszowa. Obecnie jestem na trzecim roku informatyki w Wyższej Szkole Informatyki i Zarządzania w Rzeszowie.', 'Rzeszów', 1, 1);