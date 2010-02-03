
create database if not exists ${mysql.database};
grant all privileges on ${mysql.database}.*
    to '${mysql.user.name}'@'${app.server}'
    identified by '${mysql.user.password}';
flush privileges;
