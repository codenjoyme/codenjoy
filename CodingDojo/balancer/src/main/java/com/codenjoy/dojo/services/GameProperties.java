package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("game")
public class GameProperties {
  
  private String type;
  private int room;
  private String startDay;
  private String endDay;
  private int finalistsCount;
  private String finaleTime;
  
  @Value("#{'${game.servers}'.split(',')}")
  private List<String> servers;
  
  private String schema;
  private String basicAuthUser;
  private String basicAuthPassword;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
  
  public List<String> getServers() {
    return servers;
  }

  public void setServers(List<String> servers) {
    this.servers = servers;
  }

  public int getRoom() {
    return room;
  }

  public void setRoom(int room) {
    this.room = room;
  }

  public String getStartDay() {
    return startDay;
  }

  public void setStartDay(String startDay) {
    this.startDay = startDay;
  }

  public String getEndDay() {
    return endDay;
  }

  public void setEndDay(String endDay) {
    this.endDay = endDay;
  }

  public int getFinalistsCount() {
    return finalistsCount;
  }

  public void setFinalistsCount(int finalistsCount) {
    this.finalistsCount = finalistsCount;
  }

  public String getFinaleTime() {
    return finaleTime;
  }

  public void setFinaleTime(String finaleTime) {
    this.finaleTime = finaleTime;
  }

  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public String getBasicAuthUser() {
    return basicAuthUser;
  }

  public void setBasicAuthUser(String basicAuthUser) {
    this.basicAuthUser = basicAuthUser;
  }

  public String getBasicAuthPassword() {
    return basicAuthPassword;
  }

  public void setBasicAuthPassword(String basicAuthPassword) {
    this.basicAuthPassword = basicAuthPassword;
  }
}
