INSERT INTO PRODUCT_TYPES (NAME)
VALUES ('jegy'),
       ('bérlet');

INSERT INTO PRODUCTS (NAME, PRICE, DURATION, DESCRIPTION, TYPE_ID)
VALUES ('vonaljegy', 480, 90, 'teszt1', 1),
       ('havi diák bérlet', 4000, 9000, 'teszt2', 2),
       ('havi bérlet', 9500, 9000, 'teszt3', 2);