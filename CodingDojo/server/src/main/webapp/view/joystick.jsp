<%--
  #%L
  Codenjoy - it's a dojo-like platform from developers to developers.
  %%
  Copyright (C) 2016 Codenjoy
  %%
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as
  published by the Free Software Foundation, either version 3 of the
  License, or (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public
  License along with this program.  If not, see
  <http://www.gnu.org/licenses/gpl-3.0.html>.
  #L%
  --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<div id="joystick">
    <table>
        <tr>
             <td></td>
             <td>
                <div id="up" class="joystick_button">up</div>
             </td>
             <td></td>
        </tr>
        <tr>
             <td>
                <div id="left" class="joystick_button">left</div>
             </td>
             <td>
                <div id="act" class="joystick_button">act</div><input type="text" id="act_params" class="joystick_act_params"/>
             </td>
             <td>
                <div id="right" class="joystick_button">right</div>
             </td>
        </tr>
        <tr>
             <td></td>
             <td>
                <div id="down" class="joystick_button">down</div>
             </td>
             <td></td>
        </tr>
    </table>
</div>