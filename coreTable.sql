CREATE TABLE `leaf`.`<table_name>` (
	`id` bigint(19) NOT NULL AUTO_INCREMENT,
	`app_name` varchar(256) NOT NULL,
	`key` varchar(256) NOT NULL,
	`now_max_id` bigint(19) NOT NULL,
	`create_time` datetime NOT NULL,
	`modify_time` datetime NOT NULL,
	`step` int(12) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE `key_unique` USING BTREE (`key`)
) ENGINE=`InnoDB` AUTO_INCREMENT=3 DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ROW_FORMAT=DYNAMIC CHECKSUM=0 DELAY_KEY_WRITE=0;