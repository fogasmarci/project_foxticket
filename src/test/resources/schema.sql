CREATE TABLE `news`
(
    `ID`           int NOT NULL AUTO_INCREMENT,
    `TITLE`        varchar(100) DEFAULT NULL,
    `CONTENT`      varchar(100) DEFAULT NULL,
    `publishDate` date         DEFAULT NULL,
    PRIMARY KEY (`ID`)
);