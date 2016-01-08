create table `tmysql_test_person_info` (
  `id` int(11) not null,
  `first_name` varchar(30)  not null,
  `second_name` varchar(30)  not null,
  `birthday` datetime not null,
  `gender` char(1)  default null,
  `marital_status` char(1)  default null,
  `work_date` datetime default null,
  `hobby` varchar(40)  default null,
  primary key (`id`)
) engine=innodb default charset=utf8