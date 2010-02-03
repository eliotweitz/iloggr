
drop database if exists ${mysql.database};

-- unfortunately, there is no if exists syntax for revoking privileges or dropping a user
-- revoke all privileges, grant option from '${mysql.user.name}'@'localhost';
-- drop user '${mysql.user.name}'@'localhost';

delete from db where User='${mysql.user.name}';
delete from procs_priv where User='${mysql.user.name}';
delete from tables_priv where User='${mysql.user.name}';
delete from user where User='${mysql.user.name}';
--delete from user_info where User='${mysql.user.name}';

flush privileges;
