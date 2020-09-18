CREATE TABLE users (
	id int identity primary key,
	name varchar(50) not null,
	birthdate date
);

CREATE TABLE addresses (
	user_id int not null,
	id int identity primary key,
	street varchar(50) not null,
	number integer not null,
	zip_code varchar(50) not null,
	city varchar(50),
	FOREIGN KEY (user_id) REFERENCES users(id)
);