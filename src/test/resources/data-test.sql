INSERT INTO news (TITLE, CONTENT, PUBLISHDATE)
VALUES ('News about tickets', 'Ipsum Lorum', '2023-12-11');
INSERT INTO news (TITLE, CONTENT, PUBLISHDATE)
VALUES ('Test Title', 'Test Content', '2023-12-11');

INSERT INTO USERS (NAME, EMAIL, PASSWORD, ROLES)
VALUES ('TestUser', 'user@user.user', '12345678', 'ROLE_USER');

INSERT INTO USERS (NAME, EMAIL, PASSWORD, ROLES)
VALUES ('TestAdmin', 'admin@admin.admin', 'adminadmin', 'ROLE_USER,ROLE_ADMIN');