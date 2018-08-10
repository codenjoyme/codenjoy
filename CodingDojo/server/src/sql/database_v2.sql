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

-- CREATE PRIMARY KEYS
-- TODO continue with PK - надо как-то сделать чтобы INSERT работал как апдейт, если появляются дубликаты, с другой стороны чего они появляются?
DELETE FROM public.player_boards;
ALTER TABLE public.player_boards ADD PRIMARY KEY (time, player_name);

DELETE FROM public.saves;
ALTER TABLE public.saves ADD PRIMARY KEY (time, name);
