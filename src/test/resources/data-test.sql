INSERT INTO PRODUCT_TYPES (NAME)
VALUES ('jegy'),
       ('bérlet');

INSERT INTO PRODUCTS (NAME, PRICE, DURATION, DESCRIPTION, TYPE_ID)
VALUES ('teszt jegy 1', 480, 90, 'teszt1', 1),
       ('teszt bérlet 1', 4000, 9000, 'teszt2', 2),
       ('teszt bérlet 2', 9500, 9000, 'teszt3', 2);