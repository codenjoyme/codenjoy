package com.codenjoy.dojo.services.grpc;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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


import com.dojo.notifications.Query;
import com.dojo.notifications.QueryRequest;
import com.dojo.notifications.QueryResponse;
import com.dojo.notifications.QueryServiceGrpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryClient {

    private final QueryServiceGrpc.QueryServiceBlockingStub queryServiceBlockingStub;

    @Autowired
    public QueryClient(QueryServiceGrpc.QueryServiceBlockingStub queryServiceBlockingStub) {
        this.queryServiceBlockingStub = queryServiceBlockingStub;
    }

    public List<Query> getQueriesForContest(String game) {
        QueryRequest request = QueryRequest.newBuilder().setContestId(game).build();
        QueryResponse response = queryServiceBlockingStub.getQueryRequestsForContest(request);

        return response.getQueryList();
    }
}
