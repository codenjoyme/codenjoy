package com.codenjoy.dojo.services.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
@RequiredArgsConstructor
public class Helpers {

    public final CleanHelper clean;
    public final ChatHelper chat;
    public final LoginHelper login;
    public final RoomHelper rooms;
    public final TimeHelper time;

}
