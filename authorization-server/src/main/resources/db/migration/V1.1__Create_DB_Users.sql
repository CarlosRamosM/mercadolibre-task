CREATE TABLE user (
    id integer NOT NULL AUTO_INCREMENT,
    name varchar(20) NOT NULL,
    family_name varchar(50) NOT NULL,
    birthday date NOT NULL,
    email text NOT NULL,
    password text NOT NULL,
    PRIMARY KEY(id)
);