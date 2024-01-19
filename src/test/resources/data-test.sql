DROP
ALL OBJECTS;

CREATE TABLE product_types
(
    ID   INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(100) DEFAULT NULL
);

INSERT INTO product_types (NAME)
VALUES ('jegy'),
       ('bérlet');

CREATE TABLE products
(
    ID          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    NAME        VARCHAR(100) DEFAULT NULL,
    PRICE       INT          DEFAULT NULL,
    DURATION    INT          DEFAULT NULL,
    DESCRIPTION VARCHAR(100) DEFAULT NULL,
    TYPE_ID     INT          DEFAULT NULL,
    CONSTRAINT FK_TYPE
        FOREIGN KEY (TYPE_ID)
            REFERENCES product_types (ID)
);

INSERT INTO products (NAME, PRICE, DURATION, DESCRIPTION, TYPE_ID)
VALUES ('teszt jegy 1', 480, 90, 'teszt1', 1),
       ('teszt bérlet 1', 4000, 9000, 'teszt2', 2),
       ('teszt bérlet 2', 9500, 9000, 'teszt3', 2);

CREATE TABLE news
(
    ID          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    TITLE       VARCHAR(100) DEFAULT NULL,
    CONTENT     VARCHAR(100) DEFAULT NULL,
    PUBLISHDATE DATE         DEFAULT NULL
);

INSERT INTO news (TITLE, CONTENT, PUBLISHDATE)
VALUES ('News about tickets', 'Ipsum Lorum', '2023-12-11'),
       ('Test Title', 'Test Content', '2023-12-11');

CREATE TABLE carts
(
    ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY
);

INSERT INTO carts (ID)
VALUES (default),
       (default),
       (default);

CREATE TABLE cart_product
(
    CART_ID    INT NOT NULL,
    PRODUCT_ID INT NOT NULL
);

INSERT INTO cart_product (CART_ID, PRODUCT_ID)
VALUES ((SELECT ID FROM carts LIMIT 1 OFFSET 2), 2),
       ((SELECT ID FROM carts LIMIT 1 OFFSET 2), 2),
       ((SELECT ID FROM carts LIMIT 1 OFFSET 2), 1);

CREATE TABLE roles
(
    ROLE_ID   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    AUTHORITY VARCHAR(100) NOT NULL
);

INSERT INTO roles (AUTHORITY)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN');

CREATE TABLE users
(
    ID         INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    NAME       VARCHAR(100) DEFAULT NULL,
    PASSWORD   VARCHAR(100) DEFAULT NULL,
    EMAIL      VARCHAR(100) DEFAULT NULL UNIQUE,
    ISADMIN    BOOLEAN      DEFAULT false,
    ISVERIFIED BOOLEAN      DEFAULT false,
    CART_ID    INT NOT NULL,
    CONSTRAINT FK_CART
        FOREIGN KEY (CART_ID)
            REFERENCES carts (ID)
);

INSERT INTO users (NAME, EMAIL, PASSWORD, CART_ID, ISADMIN, ISVERIFIED)
VALUES ('TestUser', 'user@user.user', '$2a$10$n.AMx5SrMrlOnJSmsTrgU.rvT4GFKsBFFaGJ8W3JjB8JNcroGx5ga',
        (SELECT ID FROM carts LIMIT 1), false, false ),
        ('TestAdmin', 'admin@admin.admin', '$2a$10$YpqEwYeCugXTRDRtC1RnAuRsBIDgSzot30jRp1B5HZzn6j5drpJeO',
        (SELECT ID FROM carts LIMIT 1 OFFSET 1), true, true ),
        ('CartTestUser', 'cica@cartuser.ab', '$2a$12$RDfNaPDsUMJWlZgdvbR0JOXhPRAfD6ptrPx/4q8oDoQMHJ2AEvVdO',
        (SELECT ID FROM carts LIMIT 1 OFFSET 2) );

CREATE TABLE user_role_junction
(
    ROLE_ID INT NOT NULL,
    USER_ID INT NOT NULL
);

INSERT INTO user_role_junction (USER_ID, ROLE_ID)
VALUES (1, 1),
       (2, 2),
       (1, 3);

CREATE TABLE ordered_items
(
    ID         INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    EXPIRY     TIMESTAMP(6) DEFAULT NULL,
    STATUS     VARCHAR(50) CHECK (STATUS IN ('Not_active', 'Active', 'Expired')),
    PRODUCT_ID INT NOT NULL,
    USER_ID    INT NOT NULL,
    CONSTRAINT FK_PROD
        FOREIGN KEY (PRODUCT_ID)
            REFERENCES products (ID),
    CONSTRAINT FK_USER
        FOREIGN KEY (USER_ID)
            REFERENCES users (ID)
);