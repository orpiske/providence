CREATE TABLE IF NOT EXISTS `shared` (
  `shared_id` INT NOT NULL AUTO_INCREMENT,
  `shared_source` VARCHAR(128) NOT NULL,
  `shared_format` VARCHAR(128) NOT NULL,
  `shared_date` TIMESTAMP NOT NULL DEFAULT NOW(),
  `shared_text` VARCHAR(4096) NULL,
PRIMARY KEY (`shared_id`));
