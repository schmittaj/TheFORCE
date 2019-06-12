CREATE TABLE City (id INTEGER PRIMARY KEY, name VARCHAR(255) NOT NULL);
CREATE TABLE Grade (id INTEGER PRIMARY KEY, name VARCHAR(255) NOT NULL);
CREATE TABLE School (id INTEGER PRIMARY KEY, name VARCHAR(255) NOT NULL);
CREATE TABLE Program (id INTEGER PRIMARY KEY, name VARCHAR(255) NOT NULL);
CREATE TABLE Children (id INTEGER PRIMARY KEY, last_name VARCHAR(255) NOT NULL, first_name VARCHAR(255) NOT NULL, date_of_birth DATE, most_recent_parent VARCHAR(255), most_recent_school INTEGER REFERENCES School(id), most_recent_email VARCHAR(255), most_recent_phone VARCHAR(255), most_recent_city VARCHAR(255) REFERENCES City(id));
CREATE TABLE "Program_Data" ( `child_id` INTEGER, `year` INTEGER NOT NULL, `grade_id` INTEGER, `school_id` INTEGER, `city_id` INTEGER, `program_id` INTEGER, `highest_level_normal` INTEGER, `extra_one` BOOLEAN, `extra_two` BOOLEAN, `extra_three` BOOLEAN, `extra_four` BOOLEAN, `extra_five` BOOLEAN, `extra_six` BOOLEAN, `extra_seven` BOOLEAN, `extra_eight` BOOLEAN, `extra_nine` BOOLEAN, `extra_ten` BOOLEAN, `pool_pass_given` BOOLEAN, `age` INTEGER, FOREIGN KEY(`school_id`) REFERENCES `School`(`id`), FOREIGN KEY(`city_id`) REFERENCES `City`(`id`), FOREIGN KEY(`child_id`) REFERENCES `Children`(`id`), FOREIGN KEY(`grade_id`) REFERENCES `Grade`(`id`), FOREIGN KEY(`program_id`) REFERENCES `Program`(`id`), PRIMARY KEY(`child_id`,`year`) );
CREATE TABLE School_Enrollment (school_id INTEGER REFERENCES School(id), grade_id INTEGER REFERENCES Grade(id), year INTEGER NOT NULL, enrollment_amount INTEGER NOT NULL, PRIMARY KEY(school_id,grade_id,year));
CREATE TABLE "Weekly_Stats" ( `record_date` DATE, `reg` INTEGER, `read` INTEGER, `finish` INTEGER, `box1` INTEGER, `box2` INTEGER, `box3` INTEGER, `box4` INTEGER, `box5` INTEGER, `box6` INTEGER, `box7` INTEGER, `box8` INTEGER, `box9` INTEGER, `box10` INTEGER, `ecbox1` INTEGER, `ecbox2` INTEGER, `ecbox3` INTEGER, `ecbox4` INTEGER, `ecbox5` INTEGER, `ecbox6` INTEGER, `ecbox7` INTEGER, `ecbox8` INTEGER, `ecbox9` INTEGER, `ecbox10` INTEGER, PRIMARY KEY(`record_date`) );