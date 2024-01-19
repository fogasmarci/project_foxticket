INSERT INTO PRODUCT_TYPES (NAME)
VALUES ('jegy'),
       ('bérlet');

INSERT INTO PRODUCTS (NAME, PRICE, DURATION, DESCRIPTION, TYPE_ID)
VALUES ('vonaljegy', 480, 90, 'teszt1', 1),
       ('havi diák bérlet', 4000, 9000, 'teszt2', 2),
       ('havi bérlet', 9500, 9000, 'teszt3', 2);

INSERT INTO news (TITLE, CONTENT, PUBLISH_DATE)
VALUES ('barmi', 'Ipsum Lorum', '2023-12-13');
INSERT INTO news (TITLE, CONTENT, PUBLISH_DATE)
VALUES ('Road block', 'akarmi, anything, fdgjkfdkjfdkjfdkjfdkjfdkjfdkj', '2023-12-13');

INSERT INTO roles (authority)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN');

INSERT INTO users (name, email, password, is_admin, is_verified)
VALUES ('Admin', 'admin@admin.com', '$2a$10$x44csP50u/GqqeNtLW/44OLrzGq0taFv7nIb86aUw2gvrEJqiR8By', TRUE, TRUE);

INSERT INTO user_role_junction (user_id, role_id)
VALUES ((SELECT id FROM users WHERE email = 'admin@admin.com'),
        (SELECT role_id FROM roles WHERE authority = 'ROLE_ADMIN'));