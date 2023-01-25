DELETE FROM events_like WHERE events_like.event_id>0;
DELETE FROM events WHERE events.id>0;
DELETE FROM events_type WHERE events_type.id>0;
DELETE FROM users_info WHERE users_info.id>0;
DELETE FROM users WHERE users.id>0;
DELETE FROM roles WHERE roles.id>0;
