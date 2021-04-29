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


import com.codenjoy.dojo.UserDetailsIdRequest;
import com.codenjoy.dojo.UserDetailsResponse;
import com.codenjoy.dojo.UserDetailsServiceGrpc;
import com.codenjoy.dojo.UserDetailsUsernameRequest;
import com.codenjoy.dojo.services.dao.Registration;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService extends UserDetailsServiceGrpc.UserDetailsServiceImplBase {

    private final Registration registration;

    @Autowired
    public UserDetailsService(Registration registration) {
        this.registration = registration;
    }

    @Override
    public void getUserDetailsById(UserDetailsIdRequest request, StreamObserver<UserDetailsResponse> responseObserver) {
        String id = request.getId();
        String email = this.registration.getEmailById(id);

        responseObserver.onNext(UserDetailsResponse.newBuilder().setId(id).setEmail(email).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getUserDetailsByUsername(UserDetailsUsernameRequest request, StreamObserver<UserDetailsResponse> responseObserver) {
        String username = request.getUsername();
        String id = this.registration.getIdByGitHubUsername(username);
        String email = this.registration.getEmailById(id);

        responseObserver.onNext(UserDetailsResponse.newBuilder().setId(id).setEmail(email).build());
        responseObserver.onCompleted();
    }
}
