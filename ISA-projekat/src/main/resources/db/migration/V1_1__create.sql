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

INSERT INTO hospital(name, description, average_rating, address, city,
                     country, start_time, `end_time`, `latitude`, `longitude`)
values ('Serbmedik', 'Serbmedik hospital in Belgrade', 4.7, 'Beograski kej 12', 'Beograd',
        'Srbija', '10:34:21', '18:34:23', 45, 41);

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user`
(
    `id`              bigint(20)   NOT NULL AUTO_INCREMENT,
    `email`           varchar(255) NOT NULL UNIQUE,
    `first_name`      varchar(255) NOT NULL,
    `password`        varchar(255) NOT NULL,
    `address`         varchar(255) NOT NULL,
    `country`         varchar(255) NOT NULL,
    `city`            varchar(255) NOT NULL,
    `longitude`       decimal(19, 4),
    `latitude`        decimal(19, 4),
    `phone`           varchar(255) NOT NULL,
    `role`            varchar(255) DEFAULT NULL,
    `last_name`       varchar(255) NOT NULL,
    `first_login`     bit(1)       NOT NULL,
    `personal_id`     varchar(255),
    `gender`          varchar(255),
    `occupation`      varchar(255),
    `occupation_info` varchar(255),
    hospital_id       bigint(20),
    `points`          decimal(19, 4),
    PRIMARY KEY (`id`),
    FOREIGN KEY (hospital_id) REFERENCES hospital (id)
);

DROP TABLE IF EXISTS `appointment`;
CREATE TABLE `appointment`
(
    `id`                 bigint(20) NOT NULL AUTO_INCREMENT,
    `date_and_time`      datetime,
    `doctor_id`          bigint(20),
    `duration`           bigint(20),
    `patient_id`         bigint(20),
    hospital_id          bigint(20),
    `appointment_status` varchar(255),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`doctor_id`) REFERENCES user (id),
    FOREIGN KEY (`patient_id`) REFERENCES user (id),
    FOREIGN KEY (hospital_id) REFERENCES hospital (id)
);

DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `grade`     bigint(20),
    `comment`   varchar(255),
    `user_id`   bigint(20),
    appointment_id bigint(20),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES user (id),
    FOREIGN KEY (appointment_id) REFERENCES appointment (id)
);

DROP TABLE IF EXISTS `equipment`;
CREATE TABLE `equipment`
(
    `id`     bigint(20) NOT NULL AUTO_INCREMENT,
    `name`   varchar(255),
    `amount` bigint(20),
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `appointment_report`;
CREATE TABLE `appointment_report`
(
    `id`                 bigint(20) NOT NULL AUTO_INCREMENT,
    `blood_type`         varchar(255),
    `past_medical_history` varchar(255),
    `allergies`          varchar(255),
    `family_history`      varchar(255),
    `blood_pressure`      varchar(255),
    `hearth_rate`         varchar(255),
    `diagnosis`          varchar(255),
    `appointment_id`     bigint(20),
    PRIMARY KEY (`id`),
    FOREIGN KEY (appointment_id) REFERENCES appointment (id)
);

insert into `user`(email, first_name, password, address, country, city, phone,
                   role, last_name, first_login, personal_id, gender, occupation, occupation_info, hospital_id,
                   points, latitude, longitude)
VALUES ('nebojsa@gmail.com', 'Nebojsa',
        '$2a$10$36dVOozCi/zxI01Lph5KVODLdutdC7LKbRj/YHU7uz23eRxgxM.na', 'a', 'c', 'c', 'p', 'ADMIN_SYSTEM',
        'Bogosavljev', false, '3213213', 'MALE', '', '', 1, 0, 45, 44);


