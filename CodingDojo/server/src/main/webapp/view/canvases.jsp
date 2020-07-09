<%--
  #%L
  Codenjoy - it's a dojo-like platform from developers to developers.
  %%
  Copyright (C) 2018 Codenjoy
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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<div id="glasses" class="glasses game" zoom-on-wheel>
    <span class="score-info width-calculator" id="width_calculator_container"></span>
    <div id="showdata"></div>
    <div id="players_container">
        <script template type="text/x-jquery-tmpl">
            <div id="div_{%= id %}" class="player-canvas">
                <table>
                    <tr style="display: {%= playerVisible %}">
                        <td>
                            <div class="player_info">
                                <h2>
                                    <span id="player_name" class="label label-primary">{%= name %}</span> :
                                    <span class="label label-primary label-value" id="score_{%= id %}"></span>
                                </h2>
                            </div>
                        </td>
                    </tr>
                    <tr style="display: {%= levelVisible %}">
                        <td>
                            <div class="player_info">
                                <h4>
                                    <span class="label label-default">Level</span> :
                                    <span class="label label-default" id="level_{%= id %}"></span>
                                </h4>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <canvas id="{%= id %}">
                                Your browser does not support the canvas element.
                            </canvas>

                            <span class="score-info" id="score_info_{%= id %}">+200</span>
                        </td>
                    </tr>
                </table>
            </div>
        </script>
    </div>

    <div id="systemCanvas" style="display: none">
        <canvas id="_system" width="168" height="24">
            Your browser does not support the canvas element.
        </canvas>
    </div>
</div>
