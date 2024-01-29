SET
REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE CART_PRODUCTS;
TRUNCATE TABLE CARTS;
TRUNCATE TABLE NEWS;
TRUNCATE TABLE ORDERED_ITEMS;
TRUNCATE TABLE PRODUCTS;
TRUNCATE TABLE PRODUCT_TYPES;
TRUNCATE TABLE ROLES;
TRUNCATE TABLE USER_ROLE;
TRUNCATE TABLE USERS;
SET
REFERENTIAL_INTEGRITY TRUE;

INSERT INTO PRODUCT_TYPES (NAME)
VALUES ('jegy'),
       ('bérlet');

INSERT INTO PRODUCTS (NAME, PRICE, DURATION, DESCRIPTION, TYPE_ID)
VALUES ('teszt jegy 1', 480, 90, 'teszt1', (SELECT id from PRODUCT_TYPES WHERE name LIKE 'jegy')),
       ('teszt bérlet 1', 4000, 9000, 'teszt2', (SELECT id from PRODUCT_TYPES WHERE name LIKE 'bérlet')),
       ('teszt bérlet 2', 9500, 9000, 'teszt3', (SELECT id from PRODUCT_TYPES WHERE name LIKE 'bérlet'));

INSERT INTO NEWS (TITLE, CONTENT, PUBLISHDATE)
VALUES ('News about tickets', 'Ipsum Lorum', '2023-12-11');
INSERT INTO NEWS (TITLE, CONTENT, PUBLISHDATE)
VALUES ('Test Title', 'Test Content', '2023-12-11');

INSERT INTO CARTS (ID)
VALUES (default),
       (default),
       (default),
       (default);

INSERT INTO CART_PRODUCTS(CART_ID, PRODUCT_ID, QUANTITY)
VALUES ((SELECT ID FROM CARTS LIMIT 1 OFFSET 2),
       (SELECT id FROM PRODUCTS WHERE name LIKE 'teszt bérlet 1'), 2),
       ((SELECT ID FROM CARTS LIMIT 1 OFFSET 2), (SELECT id FROM PRODUCTS WHERE name LIKE 'teszt jegy 1'), 1);

INSERT INTO ROLES (AUTHORITY)
VALUES ('USER'),
       ('ADMIN');

INSERT INTO USERS (NAME, EMAIL, PASSWORD, CART_ID, ISADMIN, ISVERIFIED)
VALUES ('TestUser', 'user@user.user', '$2a$10$n.AMx5SrMrlOnJSmsTrgU.rvT4GFKsBFFaGJ8W3JjB8JNcroGx5ga',
        (SELECT ID FROM CARTS LIMIT 1), false, false );

INSERT INTO USERS (NAME, EMAIL, PASSWORD, CART_ID, ISADMIN, ISVERIFIED)
VALUES ('TestAdmin', 'admin@admin.admin', '$2a$10$YpqEwYeCugXTRDRtC1RnAuRsBIDgSzot30jRp1B5HZzn6j5drpJeO',
        (SELECT ID FROM CARTS LIMIT 1 OFFSET 1), true, true );

INSERT INTO USERS (NAME, EMAIL, PASSWORD, CART_ID)
VALUES ('CartTestUser', 'cica@cartuser.ab', '$2a$12$RDfNaPDsUMJWlZgdvbR0JOXhPRAfD6ptrPx/4q8oDoQMHJ2AEvVdO',
        (SELECT ID FROM CARTS LIMIT 1 OFFSET 2) );

INSERT INTO USERS (NAME, EMAIL, PASSWORD, CART_ID)
VALUES ('OrderTestUser', 'something@orderuser.xy', '$2a$12$6MLSdozTf7Bddnyrd9tMIekHsbeS.YlkxH//wbRoc8T8wiNTl3Sru',
        (SELECT ID FROM CARTS LIMIT 1 OFFSET 3) );

INSERT INTO USER_ROLE (USER_ID, ROLE_ID)
SELECT u.id, r.role_id
FROM USERS u,
     ROLES r
WHERE u.name = 'TestUser' AND r.authority = 'USER'
   OR u.name = 'TestAdmin' AND r.authority = 'ADMIN'
   OR u.name = 'CartTestUser' AND r.authority = 'USER'
   OR u.name = 'OrderTestUser' AND r.authority = 'USER';

INSERT INTO ORDERED_ITEMS (STATUS, PRODUCT_ID, USER_ID)
SELECT 'Not_active', p.id, u.id
FROM PRODUCTS p,
     USERS u
WHERE p.name = 'teszt bérlet 1'
  AND u.name = 'OrderTestUser';

INSERT INTO ORDERED_ITEMS (STATUS, PRODUCT_ID, USER_ID)
SELECT 'Not_active', p.id, u.id
FROM PRODUCTS p,
     USERS u
WHERE p.name = 'teszt bérlet 1' AND u.name = 'OrderTestUser'
   OR p.name = 'teszt jegy 1' AND u.name = 'OrderTestUser'
   OR p.name = 'teszt bérlet 2' AND u.name = 'OrderTestUser';