CREATE TABLE `ARTICLES`
(
    `ID`           int NOT NULL AUTO_INCREMENT,
    `TITLE`        varchar(100) DEFAULT NULL,
    `CONTENT`      varchar(100) DEFAULT NULL,
    `PUBLISH_DATE` date         DEFAULT NULL,
    PRIMARY KEY (`ID`)
);