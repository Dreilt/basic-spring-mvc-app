INSERT INTO `app_user_role` (`uuid`, `name`, `display_name`, `description`)
VALUES (RANDOM_UUID(), 'ADMIN', 'Administrator', 'Ma dostęp do wszystkiego'),
       (RANDOM_UUID(), 'ORGANIZER', 'Organizator', 'Może organizować wydarzenia'),
       (RANDOM_UUID(), 'USER', 'Użytkownik', 'Dostęp ograniczony');