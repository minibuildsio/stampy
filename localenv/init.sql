revoke all on schema public from public;

create user dbuser password 'dbuserpassword';

grant usage on schema public to dbuser;
alter default privileges in schema public grant select, insert, update, delete on tables to dbuser;
alter default privileges in schema public grant usage, select on sequences to dbuser;
