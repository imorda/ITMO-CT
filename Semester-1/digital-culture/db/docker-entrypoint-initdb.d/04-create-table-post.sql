USE bloggle;

CREATE TABLE `Post` (
`id` BIGINT NOT NULL AUTO_INCREMENT,
`userId` BIGINT NOT NULL,
`title` VARCHAR(255) NOT NULL,
`text` LONGTEXT NOT NULL,
`creationTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (`id`),
INDEX `index_Post_creationTime` (`creationTime`)
) ENGINE = InnoDB;

ALTER TABLE `Post` ADD CONSTRAINT `fk_Post_userId` FOREIGN KEY (`userId`) REFERENCES `User` (`id`);
