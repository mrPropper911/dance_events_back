# Insert data for testing database only on test profile
DELETE FROM roles WHERE roles.id>0;
DELETE FROM events_like WHERE events_like.event_id>0;
DELETE FROM events WHERE events.id>0;
DELETE FROM events_type WHERE events_type.id>0;
DELETE FROM users_info WHERE users_info.id>0;
DELETE FROM users WHERE users.id>0;

ALTER TABLE roles AUTO_INCREMENT=1;
ALTER TABLE users AUTO_INCREMENT=1;
ALTER TABLE events_type AUTO_INCREMENT=1;
ALTER TABLE events AUTO_INCREMENT=1;

INSERT INTO roles(roleTitle)
VALUES  ('Administrator'),
        ('Organizer'),
        ('Member');

INSERT INTO events_type(type)
VALUES  ('Urban'),
        ('Regional'),
        ('Republican'),
        ('International');

INSERT INTO users(login, password, active, role_id)
VALUES  ('vadik05', '$2y$10$4HBKgn/t6Un7SgEd6UOF4.sT0qNBTeWAwPEeHCSOlvD2tNRFVlU9G', 1, 1),
        ('veronika44', '$2y$10$ybyXjlkHB5/VFXG1zIGwCe5KXwNKQjvRyHfK0JzlGxP7MIOOtsFna', 1, 2),
        ('igor88', '$2y$10$4HBKgn/t6Un7SgEd6UOF4.sT0qNBTeWAwPEeHCSOlvD2tNRFVlU9G', 1, 3),
        ('sergey13', '$2y$10$4HBKgn/t6Un7SgEd6UOF4.sT0qNBTeWAwPEeHCSOlvD2tNRFVlU9G', 0, 3);

INSERT INTO users_info(id, name, surname, phone, email)
VALUES (1, 'Vadia', 'Belynovcki', '+375331231232', 'vadia123bel@gmail'),
       (2, 'Veron', 'Cherepash', '+751223224114', 'veron11lose@mail.ru'),
       (3, 'Igor', 'Lihten', '+453221322221', 'igoridze@gmail.com'),
       (4, 'Sergey', 'Pogorelov', '+353221322221', 'sergeygey@gmail.com');

INSERT INTO events(title, startDate, endDate, description, active, eventsByType)
VALUES ('Baranovivhi-2023', '2023-07-11', '2023-07-13', 'Biggest championship in the world', 1, 1),
       ('Berestie-XOX', '2023-08-11', '2023-09-13', 'Sake vs Cortana', 0, 2),
       ('MINSK-666', '2024-08-11', '2024-09-13', 'Open champ for amatory', 1, 3),
       ('Hogvarts Championship', '2025-03-11', '2025-03-01', 'Olympic dancer open qualification', 1, 4);

INSERT INTO events_like(user_id, event_id)
VALUES (2, 1),
       (2, 2),
       (3, 3),
       (3, 4),
       (4, 1),
       (4, 2),
       (4, 3),
       (4, 4);