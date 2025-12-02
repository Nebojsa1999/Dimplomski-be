DROP TABLE IF EXISTS `center_account`;

CREATE TABLE `center_account`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT,
    `date_created`   datetime   NOT NULL,
    `date_updated`   datetime DEFAULT NULL,
    `deleted`        bit(1)     NOT NULL,
    `name`           varchar(255),
    `description`    varchar(255),
    `average_rating` decimal(19, 4),
    `address`        varchar(255),
    `city`           varchar(255),
    `country`        varchar(255),
    `longitude`      decimal(19, 4),
    `latitude`       decimal(19, 4),
    `start_time`     time,
    `end_time`       time,
    PRIMARY KEY (id)
);

INSERT INTO `center_account`(date_created, date_updated, deleted, name, description, average_rating, address, city,
                             country, start_time, `end_time`, `latitude`, `longitude`)
values ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 'Center1', 'Description', 12.5, 'Address1', 'City1',
        'Country1', '10:34:21', '18:34:23', 45, 41);

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user`
(
    `id`                bigint(20)   NOT NULL AUTO_INCREMENT,
    `date_created`      datetime     NOT NULL,
    `date_updated`      datetime     DEFAULT NULL,
    `deleted`           bit(1)       NOT NULL,
    `email`             varchar(255) NOT NULL,
    `first_name`        varchar(255) NOT NULL,
    `password`          varchar(255) NOT NULL,
    `address`           varchar(255) NOT NULL,
    `country`           varchar(255) NOT NULL,
    `city`              varchar(255) NOT NULL,
    `longitude`         decimal(19, 4),
    `latitude`          decimal(19, 4),
    `phone`             varchar(255) NOT NULL,
    `role`              varchar(255) DEFAULT NULL,
    `last_name`         varchar(255) NOT NULL,
    `first_login`       bit(1)       NOT NULL,
    `personal_id`       varchar(255),
    `gender`            varchar(255),
    `occupation`        varchar(255),
    `occupation_info`   varchar(255),
    `center_account_id` bigint(20),
    `points`            decimal(19, 4),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`center_account_id`) REFERENCES center_account (id)
);

DROP TABLE IF EXISTS `poll`;
CREATE TABLE `poll`
(
    `id`                  bigint(20) NOT NULL AUTO_INCREMENT,
    `date_created`        datetime   NOT NULL,
    `date_updated`        datetime DEFAULT NULL,
    `deleted`             bit(1)     NOT NULL,
    `blood_id`            bigint(20),
    `weight`              bit(1),
    `sickness`            bit(1),
    `infection`           bit(1),
    `pressure`            bit(1),
    `therapy`             bit(1),
    `cycle`               bit(1),
    `dental_intervention` bit(1),
    `piercing`            bit(1),
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `appointment`;
CREATE TABLE `appointment`
(
    `id`                    bigint(20) NOT NULL AUTO_INCREMENT,
    `date_created`          datetime   NOT NULL,
    `date_updated`          datetime DEFAULT NULL,
    `deleted`               bit(1)     NOT NULL,
    `date_and_time`         datetime,
    `admin_id`              bigint(20),
    `duration`              bigint(20),
    `patient_id`            bigint(20),
    `poll_id`               bigint(20),
    `center_account_id`     bigint(20),
    `completed_appointment` bit(1),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`admin_id`) REFERENCES User (id),
    FOREIGN KEY (`patient_id`) REFERENCES User (id),
    FOREIGN KEY (`poll_id`) REFERENCES Poll (id),
    FOREIGN KEY (`center_account_id`) REFERENCES center_account (id)
);

DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT,
    `date_created`   datetime   NOT NULL,
    `date_updated`   datetime DEFAULT NULL,
    `deleted`        bit(1)     NOT NULL,
    `grade`          bigint(20),
    `comment`        varchar(255),
    `user_id`        bigint(20),
    `appointment_id` bigint(20),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES User (id),
    FOREIGN KEY (`appointment_id`) REFERENCES Appointment (id)
);

DROP TABLE IF EXISTS `blood`;
CREATE TABLE `blood`
(
    `id`                bigint(20) NOT NULL AUTO_INCREMENT,
    `date_created`      datetime   NOT NULL,
    `date_updated`      datetime DEFAULT NULL,
    `deleted`           bit(1)     NOT NULL,
    `blood_type`        varchar(255),
    `amount`            decimal(19, 4),
    `center_account_id` bigint(20),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`center_account_id`) REFERENCES center_account (id)
);

DROP TABLE IF EXISTS `equipment`;
CREATE TABLE `equipment`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT,
    `date_created`   datetime   NOT NULL,
    `date_updated`   datetime DEFAULT NULL,
    `deleted`        bit(1)     NOT NULL,
    `equipment_type` varchar(255),
    `amount`         bigint(20),
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `appointment_report`;
CREATE TABLE `appointment_report`
(
    `id`                                                 bigint(20) NOT NULL AUTO_INCREMENT,
    `date_created`                                       datetime   NOT NULL,
    `date_updated`                                       datetime DEFAULT NULL,
    `deleted`                                            bit(1)     NOT NULL,
    `blood_type`                                         varchar(255),
    `blood_amount`                                       decimal(19, 4),
    `note_to_doctor`                                     varchar(255),
    `copper_sulfate`                                     varchar(255),
    `hemoglobinometer`                                   varchar(255),
    `lungs`                                              varchar(255),
    `heart`                                              varchar(255),
    `TA`                                                 varchar(255),
    `TT`                                                 varchar(255),
    `TV`                                                 varchar(255),
    `bag_type`                                           varchar(255),
    `note`                                               varchar(255),
    `puncture_site`                                      varchar(255),
    `start_of_giving`                                    varchar(255),
    `end_of_giving`                                      varchar(255),
    `reason_for_premature_termination_of_blood_donation` varchar(255),
    `equipment_id`                                       bigint(20),
    `equipment_amount`                                   decimal(19, 4),
    `denied`                                             bit(1),
    `reason_for_denying`                                 varchar(255),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`equipment_id`) REFERENCES equipment (id)
);


insert into `user`(date_created, date_updated, deleted, email, first_name, password, address, country, city, phone,
                   role, last_name, first_login, personal_id, gender, occupation, occupation_info, center_account_id,
                   points, latitude, longitude)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 'nebojsa@gmail.com', 'Nebojsa',
        '$2a$10$36dVOozCi/zxI01Lph5KVODLdutdC7LKbRj/YHU7uz23eRxgxM.na', 'a', 'c', 'c', 'p', 'ADMIN_CENTER',
        'Bogosavljev', false, '3213213', 'MALE', '', '', 1, 0, 45, 44);

insert into `user`(date_created, date_updated, deleted, email, first_name, password, address, country, city, phone,
                   role, last_name, first_login, personal_id, gender, occupation, occupation_info, center_account_id,
                   points, latitude, longitude)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 'test@gmail.com', 'Marko',
        '$2a$10$36dVOozCi/zxI01Lph5KVODLdutdC7LKbRj/YHU7uz23eRxgxM.na', 'a', 'c', 'c', 'p', 'USER',
        'Markovic', false, '3213213', 'MALE', '', '', null, 0, 41, 41);

insert into `user`(date_created, date_updated, deleted, email, first_name, password, address, country, city, phone,
                   role, last_name, first_login, personal_id, gender, occupation, occupation_info, center_account_id,
                   points, latitude, longitude)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 'test2@gmail.com', 'Bojan',
        '$2a$10$36dVOozCi/zxI01Lph5KVODLdutdC7LKbRj/YHU7uz23eRxgxM.na', 'a', 'c', 'c', 'p', 'USER',
        'Braun', false, '3213213', 'MALE', '', '', null, 0, 33, 43);

insert into `user`(date_created, date_updated, deleted, email, first_name, password, address, country, city, phone,
                   role, last_name, first_login, personal_id, gender, occupation, occupation_info, center_account_id,
                   points, latitude, longitude)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 'test3@gmail.com', 'Igor',
        '$2a$10$36dVOozCi/zxI01Lph5KVODLdutdC7LKbRj/YHU7uz23eRxgxM.na', 'a', 'c', 'c', 'p', 'USER',
        'igic', false, '3213213', 'MALE', '', '', null, 0, 41, 41);

insert into `blood`(date_created, date_updated, deleted, blood_type, amount, center_account_id)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 'A', 11, 1);

insert into `blood`(date_created, date_updated, deleted, blood_type, amount, center_account_id)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 'A', 2, 1);

insert into `blood`(date_created, date_updated, deleted, blood_type, amount, center_account_id)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 'B', 3, 1);

insert into `blood`(date_created, date_updated, deleted, blood_type, amount, center_account_id)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 'B', 4, 1);

insert into `blood`(date_created, date_updated, deleted, blood_type, amount, center_account_id)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 'AB', 1, 1);

insert into `blood`(date_created, date_updated, deleted, blood_type, amount, center_account_id)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 'AB', 2, 1);

insert into `blood`(date_created, date_updated, deleted, blood_type, amount, center_account_id)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 'ZERO', 2, 1);

insert into `blood`(date_created, date_updated, deleted, blood_type, amount, center_account_id)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 'ZERO', 3, 1);

insert into `poll`(date_created, date_updated, deleted, blood_id, weight, sickness, infection,
                   pressure, therapy, cycle, dental_intervention, piercing)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 1, true, true, true, true, true, true, true, true);

insert into `poll`(date_created, date_updated, deleted, blood_id, weight, sickness, infection,
                   pressure, therapy, cycle, dental_intervention, piercing)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 2, false, false, false, false, false, false,
        false, false);

insert into `poll`(date_created, date_updated, deleted, blood_id, weight, sickness, infection,
                   pressure, therapy, cycle, dental_intervention, piercing)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 3, true, true, true, true, true, true, false, true);

insert into `appointment`(date_created, date_updated, deleted, date_and_time, admin_id, duration, patient_id, poll_id,
                          center_account_id, completed_appointment)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, '2023-04-22 10:34:23', 1, 12, 2, 1, 1, true);

insert into `appointment`(date_created, date_updated, deleted, date_and_time, admin_id, duration, patient_id, poll_id,
                          center_account_id, completed_appointment)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, '2023-06-22 10:34:23', 1, 12, 3, 2, 1, true);

insert into `appointment`(date_created, date_updated, deleted, date_and_time, admin_id, duration, patient_id, poll_id,
                          center_account_id, completed_appointment)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, '2023-05-22 10:34:23', 1, 12, 4, 3, 1, true);

insert into `appointment`(date_created, date_updated, deleted, date_and_time, admin_id, duration, patient_id, poll_id,
                          center_account_id, completed_appointment)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, '2023-05-22 10:34:23', 1, 12, 4, 1, 1, false);

insert into `appointment`(date_created, date_updated, deleted, date_and_time, admin_id, duration, patient_id, poll_id,
                          center_account_id, completed_appointment)
VALUES ('2022-04-22 10:34:23', '2022-04-11 10:34:23	', false, '2023-05-22 10:34:23', 1, 12, 4, 2, 1, false);

insert into `feedback`(date_created, date_updated, deleted, grade, comment, user_id, appointment_id)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 5, 'bla', 2, 1);

insert into `feedback`(date_created, date_updated, deleted, grade, comment, user_id, appointment_id)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false, 11, 'bla', 2, 1);

insert into `equipment`(date_created, date_updated, deleted, equipment_type, amount)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false,'NEEDLE',2);
insert into `equipment`(date_created, date_updated, deleted, equipment_type, amount)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false,'COTTON_WOOL',4);
insert into `equipment`(date_created, date_updated, deleted, equipment_type, amount)
VALUES ('2022-04-22 10:34:23', '2022-04-22 10:34:23	', false,'SYRINGE',5);


