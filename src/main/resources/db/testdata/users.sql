INSERT INTO `app_user` (`first_name`, `last_name`, `email`, `password`, `enabled`, `account_non_locked`)
VALUES
    -- admin@example.com / admin
    ('Admin', 'Admin', 'admin@example.com', '{bcrypt}$2a$10$1rXMx0b4caUy/SN3Xg4j4u43gDqVJO/R.zXGCGWc/wr7bsmmSEk2C', 1, 1),
    -- jankowalski@example.com / user
    ('Jan', 'Kowalski', 'jankowalski@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 1, 1),
    -- patrykkowalski@example.com / user
    ('Patryk', 'Kowalski', 'patrykkowalski@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 1, 1),
    -- jannowak@example.com / user
    ('Jan', 'Nowak', 'jannowak@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 1, 1),
    -- patryknowak@example.com / user
    ('Patryk', 'Nowak', 'patryknowak@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 1, 1),
    -- piotrwysocki@example.com / user
    ('Piotr', 'Wysocki', 'piotrwysocki@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 1, 1),
    -- dawidpolak@example.com / user
    ('Dawid', 'Polak', 'dawidpolak@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 1, 1),
    -- zuzannakowalska@example.com / user
    ('Zuzanna', 'Kowalska', 'zuzannakowalska@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 1, 1),
    -- piotrmichalik@example.com / user
    ('Piotr', 'Michalik', 'piotrmichalik@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 1, 1),
    -- dawiddąbrowski@example.com / user
    ('Dawid', 'Dąbrowski', 'dawiddabrowski@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 1, 1),
    -- danieldąbrowski@example.com / user
    ('Daniel', 'Dąbrowski', 'danieldabrowski@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 1, 1),
    -- marianowak@example.com / user
    ('Maria', 'Nowak', 'marianowak@example.com', '{bcrypt}$2a$10$HdTWfiLZkzlT9JXh/q3OhuPMsp972q0rp9oHba0wgaI0P.zRsLfb6', 1, 1);

INSERT INTO `app_user_role` (`name`, `visible_name`, `description`)
VALUES ('ADMIN', 'Administrator', 'Ma dostęp do wszystkiego'),
       ('USER', 'Użytkownik', 'Dostęp ograniczony');

INSERT INTO `user_roles` (`user_id`, `role_id`)
VALUES (1, 1),
       (2, 2),
       (3, 2),
       (4, 2),
       (5, 2),
       (6, 2),
       (7, 2),
       (8, 2),
       (9, 2),
       (10, 2),
       (11, 2),
       (12, 2);