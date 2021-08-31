---
-- #%L
-- Codenjoy - it's a dojo-like platform from developers to developers.
-- %%
-- Copyright (C) 2018 - 2021 Codenjoy
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
ALTER TABLE public.saves
    ADD COLUMN team_id integer;
	
CREATE INDEX team_id_saves_index
    ON public.saves USING btree
    (team_id ASC NULLS LAST)
    TABLESPACE pg_default;

ALTER TABLE public.saves
    ALTER COLUMN score
        TYPE character varying(255);