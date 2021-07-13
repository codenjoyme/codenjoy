package com.codenjoy.dojo.web.rest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PTeam {

    private Integer teamId;
    private List<String> players;
}
