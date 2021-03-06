
-- -----------------------------------------------------
-- Table `Account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Account` (
                                         `id` INT NOT NULL AUTO_INCREMENT,
                                         `IBAN` VARCHAR(34) NULL DEFAULT NULL,
                                         `name` VARCHAR(45) NULL DEFAULT NULL,
                                         PRIMARY KEY (`id`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Label`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Label` (
                                       `id` INT NOT NULL AUTO_INCREMENT,
                                       `name` VARCHAR(45) NULL DEFAULT NULL,
                                       `description` VARCHAR(255) NULL DEFAULT NULL,
                                       PRIMARY KEY (`id`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Payment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Payment` (
                                         `id` INT NOT NULL AUTO_INCREMENT,
                                         `IBAN` VARCHAR(255) NOT NULL DEFAULT NULL,
                                         `date` DATETIME NULL DEFAULT NULL,
                                         `amount` FLOAT NULL DEFAULT NULL,
                                         `currency` VARCHAR(45) NULL DEFAULT NULL,
                                         `detail` VARCHAR(255) NULL DEFAULT NULL,
                                         `accountId` INT NOT NULL,
                                         `counterAccountId` INT NOT NULL,
                                         `labelId` INT NULL,
                                         PRIMARY KEY (`id`),
                                         INDEX `fk_Payment_Account_idx` (`accountId` ASC),
                                         INDEX `fk_Payment_Counter_Account_idx` (`counterAccountId` ASC),
                                         INDEX `fk_Payment_Label1_idx` (`labelId` ASC),
                                         CONSTRAINT `fk_Payment_Account_Id`
                                             FOREIGN KEY (`accountId`)
                                                 REFERENCES `Account` (`id`)
                                                 ON DELETE NO ACTION
                                                 ON UPDATE NO ACTION,
                                         CONSTRAINT `fk_Payment_CounterAccount_Id`
                                             FOREIGN KEY (`counterAccountId`)
                                                 REFERENCES `Account` (`id`)
                                                 ON DELETE NO ACTION
                                                 ON UPDATE NO ACTION,
                                         CONSTRAINT `fk_Payment_Label1`
                                             FOREIGN KEY (`labelId`)
                                                 REFERENCES `Label` (`id`)
                                                 ON DELETE NO ACTION
                                                 ON UPDATE NO ACTION)
    ENGINE = InnoDB;

INSERT INTO Account VALUES(1, 'DummyIBAN', 'DummyName');
INSERT INTO Account VALUES(2, 'DummyIBAN2', 'DummyName2');
INSERT INTO Account VALUES(3, 'DummyIBAN3', 'DummyName3');

INSERT INTO Payment (`id`, `IBAN`, `amount`, `currency`, `detail`, `accountId`, `counterAccountId`)
VALUES(1, 'DummyIBANx', 150.55, 'EUR', 'detailsabc', 1, 2);

INSERT INTO Payment (`id`, `IBAN`, `amount`, `currency`, `detail`, `accountId`, `counterAccountId`)
VALUES(2, 'DummyIBANy', 40.23, 'EUR', 'detailfjdqme', 1, 2);
