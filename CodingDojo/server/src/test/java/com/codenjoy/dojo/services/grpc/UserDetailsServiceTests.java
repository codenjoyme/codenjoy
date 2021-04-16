package com.codenjoy.dojo.services.grpc;

import com.codenjoy.dojo.UserDetailsRequest;
import com.codenjoy.dojo.UserDetailsResponse;
import com.codenjoy.dojo.services.dao.Registration;
import io.grpc.stub.StreamObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserDetailsServiceTests {

    private static final String USER_ID = "id";
    private static final String EMAIL = "email";

    private UserDetailsRequest request;
    private UserDetailsResponse response;

    @Mock
    private StreamObserver<UserDetailsResponse> streamObserver;

    @Mock
    private Registration registration;

    private UserDetailsService userDetailsService;

    @Before
    public void init() {
        this.request = UserDetailsRequest.newBuilder().setId(USER_ID).build();
        this.response = UserDetailsResponse.newBuilder().setId(USER_ID).setEmail(EMAIL).build();
        this.userDetailsService = new UserDetailsService(registration);
    }

    @Test
    public void getUserDetailsTest() {
        when(registration.getEmailById(USER_ID)).thenReturn(EMAIL);

        userDetailsService.getUserDetails(request, streamObserver);

        verify(streamObserver, times(1)).onNext(response);
        verify(streamObserver, times(1)).onCompleted();
    }
}
