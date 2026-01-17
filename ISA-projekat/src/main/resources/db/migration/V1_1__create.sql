DROP TABLE IF EXISTS hospital;

CREATE TABLE hospital
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT,
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

INSERT INTO hospital( name, description, average_rating, address, city,
                     country, start_time, `end_time`, `latitude`, `longitude`)
values ( 'Serbmedik', 'Serbmedik hospital in Belgrade', 4.7, 'Beograski kej 12', 'Beograd',
        'Srbija', '10:34:21', '18:34:23', 45, 41);

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user`
(
    `id`                bigint(20)   NOT NULL AUTO_INCREMENT,
    `email`             varchar(255) NOT NULL UNIQUE,
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
    hospital_id bigint(20),
    `points`            decimal(19, 4),
    PRIMARY KEY (`id`),
    FOREIGN KEY (hospital_id) REFERENCES hospital (id)
);

DROP TABLE IF EXISTS `appointment`;
CREATE TABLE `appointment`
(
    `id`                    bigint(20) NOT NULL AUTO_INCREMENT,
    `date_and_time`         datetime,
    `doctor_id`              bigint(20),
    `duration`              bigint(20),
    `patient_id`            bigint(20),
    `poll_id`               bigint(20),
    hospital_id     bigint(20),
    `appointment_status` varchar(255),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`doctor_id`) REFERENCES user (id),
    FOREIGN KEY (`patient_id`) REFERENCES user (id),
    FOREIGN KEY (hospital_id) REFERENCES hospital (id)
);

DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT,
    `grade`          bigint(20),
    `comment`        varchar(255),
    `user_id`        bigint(20),
    hospital_id bigint(20),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES user (id),
    FOREIGN KEY (hospital_id) REFERENCES appointment (id)
);

DROP TABLE IF EXISTS `equipment`;
CREATE TABLE `equipment`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(255),
    `amount`         bigint(20),
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `appointment_report`;
CREATE TABLE `appointment_report`
(
    `id`                                                 bigint(20) NOT NULL AUTO_INCREMENT,
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

insert into `user`( email, first_name, password, address, country, city, phone,
                   role, last_name, first_login, personal_id, gender, occupation, occupation_info, hospital_id,
                   points, latitude, longitude)
VALUES ( 'nebojsa@gmail.com', 'Nebojsa',
        '$2a$10$36dVOozCi/zxI01Lph5KVODLdutdC7LKbRj/YHU7uz23eRxgxM.na', 'a', 'c', 'c', 'p', 'ADMIN_SYSTEM',
        'Bogosavljev', false, '3213213', 'MALE', '', '', 1, 0, 45, 44);

insert into `user`( email, first_name, password, address, country, city, phone,
                   role, last_name, first_login, personal_id, gender, occupation, occupation_info, hospital_id,
                   points, latitude, longitude)
VALUES ( 'test@gmail.com', 'Marko',
        '$2a$10$36dVOozCi/zxI01Lph5KVODLdutdC7LKbRj/YHU7uz23eRxgxM.na', 'a', 'c', 'c', 'p', 'PATIENT',
        'Markovic', false, '3213213', 'MALE', '', '', null, 0, 41, 41);

insert into `user`( email, first_name, password, address, country, city, phone,
                   role, last_name, first_login, personal_id, gender, occupation, occupation_info, hospital_id,
                   points, latitude, longitude)
VALUES ( 'test2@gmail.com', 'Bojan',
        '$2a$10$36dVOozCi/zxI01Lph5KVODLdutdC7LKbRj/YHU7uz23eRxgxM.na', 'a', 'c', 'c', 'p', 'PATIENT',
        'Braun', false, '3213213', 'MALE', '', '', null, 0, 33, 43);

insert into `user`( email, first_name, password, address, country, city, phone,
                   role, last_name, first_login, personal_id, gender, occupation, occupation_info, hospital_id,
                   points, latitude, longitude)
VALUES ( 'test3@gmail.com', 'Igor',
        '$2a$10$36dVOozCi/zxI01Lph5KVODLdutdC7LKbRj/YHU7uz23eRxgxM.na', 'a', 'c', 'c', 'p', 'PATIENT',
        'igic', false, '3213213', 'MALE', '', '', null, 0, 41, 41);

insert into `appointment`( date_and_time, doctor_id, duration, patient_id, poll_id,
                          hospital_id, appointment_status)
VALUES ( '2023-04-22 10:34:23', 1, 12, 2, 1, 1, 'COMPLETED');

insert into `appointment`( date_and_time, doctor_id, duration, patient_id, poll_id,
                          hospital_id, appointment_status)
VALUES ( '2023-06-22 10:34:23', 1, 12, 3, 2, 1, 'COMPLETED');

insert into `appointment`( date_and_time, doctor_id, duration, patient_id, poll_id,
                          hospital_id, appointment_status)
VALUES ( '2023-05-22 10:34:23', 1, 12, 4, 3, 1, 'COMPLETED');

insert into `appointment`( date_and_time, doctor_id, duration, patient_id, poll_id,
                          hospital_id, appointment_status)
VALUES ( '2023-05-22 10:34:23', 1, 12, 4, 1, 1, 'COMPLETED');

insert into `appointment`( date_and_time, doctor_id, duration, patient_id, poll_id,
                          hospital_id, appointment_status)
VALUES ('2023-05-22 10:34:23', 1, 12, 4, 2, 1, 'COMPLETED');

insert into `feedback`( grade, comment, user_id, hospital_id)
VALUES ( 5, 'bla', 2, 1);

insert into `feedback`( grade, comment, user_id, hospital_id)
VALUES ( 11, 'bla', 2, 1);

insert into `equipment`( name, amount)
VALUES ('Needle',2);
insert into `equipment`( name, amount)
VALUES ('Cotton wool',4);
insert into `equipment`( name, amount)
VALUES ('Syringe',5);


