/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

// так как в expansion рисуется не только спрайты (но и стрелочки),
// а так же некоторые спрайты вылазят за границы клетки,
// то надо рисовать всегда все спрайты
setup.isDrawOnlyChanges = false;

// спрайты будут рисоваться по типам
// в порядке, указанном в Elements,
// а не по xy-координатам (по-умолчанию)
setup.isDrawByOrder = true;

