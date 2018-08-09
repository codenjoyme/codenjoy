---
-- #%L
-- Codenjoy - it's a dojo-like platform from developers to developers.
-- %%
-- Copyright (C) 2018 Codenjoy
-- %%
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as
-- published by the Free Software Foundation, either version 3 of the
-- License, or (at your option) any later version.
-- 
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
-- 
-- You should have received a copy of the GNU General Public
-- License along with this program.  If not, see
-- <http://www.gnu.org/licenses/gpl-3.0.html>.
-- #L%
---

-- CREATE DATABASE tetrisj_codenjoy_contest;
CREATE USER tetrisj_application WITH PASSWORD 'password';
-- ALTER USER "tetrisj_application" WITH PASSWORD 'new_password';
SET search_path = public;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO tetrisj_application;
