create database agenda;

CREATE TABLE persons (
id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
firstname VARCHAR(30) NOT NULL,
lastname VARCHAR(30) NOT NULL,
phone VARCHAR(30) NOT NULL,
birthday DATE NOT NULL,
email VARCHAR(50)
);