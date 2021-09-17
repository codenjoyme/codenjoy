package com.codenjoy.dojo.services.helper;

import com.codenjoy.dojo.services.FieldService;
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.SaveService;
import com.codenjoy.dojo.services.chat.PlayersCache;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.semifinal.SemifinalService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
@AllArgsConstructor
public class CleanHelper {

    private Chat chat;
    private PlayerService players;
    private Registration registration;
    private GameService games;
    private FieldService fields;
    private PlayersCache playersCache;
    private SaveService saves;
    private SemifinalService semifinal;

    public void removeAll() {
        // order is important
        players.removeAll();
        registration.removeAll();
        semifinal.clean();
        games.removeAll(); // тут чистятся rooms и связанные с ними сеттинги
        fields.removeAll();
        playersCache.removeAll();
        saves.removeAllSaves();
        chat.removeAll();
    }
}
