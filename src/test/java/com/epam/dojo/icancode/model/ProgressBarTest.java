package com.epam.dojo.icancode.model;

import com.codenjoy.dojo.services.EventListener;
import com.epam.dojo.icancode.model.interfaces.IItem;
import com.epam.dojo.icancode.model.interfaces.ILevel;
import com.epam.dojo.icancode.model.items.Start;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by oleksandr.baglai on 21.07.2016.
 */
public class ProgressBarTest {

    private ICanCode single;
    private ICanCode multiple;
    private ProgressBar progressBar;
    private Player player;

    @Before
    public void setup() {
        // given
        ILevel level1 = getLevel();
        ILevel level2 = getLevel();
        ILevel level3 = getLevel();
        ILevel level4 = getLevel();
        ILevel level5 = getLevel();

        single = new ICanCode(Arrays.asList(level1, level2, level3, level4), null, false);
        multiple = new ICanCode(Arrays.asList(level5), null, true);

        progressBar = new ProgressBar(single, multiple);

        player = new Player(mock(EventListener.class), progressBar);
        single.newGame(player);
    }

    private ILevel getLevel() {
        ILevel level1 = mock(ILevel.class);
        Start start = new Start(Elements.START);
        start.setCell(new Cell(0, 0));
        List<? extends IItem> starts = Arrays.asList(start);
        when(level1.getItems(Start.class)).thenReturn((List<IItem>) starts);
        return level1;
    }

    private void assertState(String expected) {
        assertEquals(sorting(expected), sorting(progressBar.printProgress()));
    }

    // because http://stackoverflow.com/a/17229462
    private LinkedHashMap<String, Object> sorting(String expected) {
        JSONObject object = new JSONObject(expected);
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        Iterator<String> keys = object.keys();
        TreeSet<String> set = new TreeSet<>();
        while (keys.hasNext()) {
            set.add(keys.next());
        }
        for (String key : set) {
            map.put(key, object.get(key));
        }
        return map;
    }

    @Test
    public void stateAtStart() {
        // when then
        assertState("{'total':4,'current':0,'lastPassed':-1,'multiple':false}");
    }

    @Test
    public void stateWhenFinishLevel1() {
        // when
        player.getHero().setWin();
        progressBar.tick();

        // then
        assertState("{'total':4,'current':0,'lastPassed':-1,'multiple':false}");

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':1,'lastPassed':0,'multiple':false}");
    }

    @Test
    public void stateWhenFinishLevel2() {
        // given
        stateWhenFinishLevel1();

        // when
        player.getHero().setWin();
        progressBar.tick();

        // then
        assertState("{'total':4,'current':1,'lastPassed':0,'multiple':false}");

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':2,'lastPassed':1,'multiple':false}");
    }

    @Test
    public void stateWhenFinishLevel3() {
        // given
        stateWhenFinishLevel2();

        // when
        player.getHero().setWin();
        progressBar.tick();

        // then
        assertState("{'total':4,'current':2,'lastPassed':1,'multiple':false}");

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':3,'lastPassed':2,'multiple':false}");
    }

    @Test
    public void stateWhenFinishLevel4() {
        // given
        stateWhenFinishLevel3();

        // when
        player.getHero().setWin();
        progressBar.tick();

        // then
        assertState("{'total':4,'current':3,'lastPassed':2,'multiple':false}");

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':0,'lastPassed':3,'multiple':true}");
    }

    @Test
    public void stateWhenFinishMultipleLevel5() {
        // given
        stateWhenFinishLevel4();

        // when
        player.getHero().setWin();
        progressBar.tick();

        // then
        assertState("{'total':4,'current':0,'lastPassed':3,'multiple':true}");

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':0,'lastPassed':3,'multiple':true}");
    }

    @Test
    public void stateWhenFinishMultipleLevel5Again() {
        // given
        stateWhenFinishMultipleLevel5();

        // when
        player.getHero().setWin();
        progressBar.tick();

        // then
        assertState("{'total':4,'current':0,'lastPassed':3,'multiple':true}");

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':0,'lastPassed':3,'multiple':true}");
    }

    @Test
    public void stateWhenGoFromMultipleToSingle() {
        // given
        stateWhenFinishMultipleLevel5Again();

        // when
        player.getHero().loadLevel(2);
        progressBar.tick();

        // then
        assertState("{'total':4,'current':2,'lastPassed':3,'multiple':false}");

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':2,'lastPassed':3,'multiple':false}");
    }

    @Test
    public void stateWhenNextLevelAfterGoFromMultipleToSingle() {
        // given
        stateWhenGoFromMultipleToSingle();

        // when
        player.getHero().setWin();
        progressBar.tick();

        // then
        assertState("{'total':4,'current':2,'lastPassed':3,'multiple':false}");

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':3,'lastPassed':3,'multiple':false}");
    }

    @Test
    public void stateWhenGoFromSingleToMultiple() {
        // given
        stateWhenGoFromMultipleToSingle();

        // when
        player.getHero().loadLevel(4);
        progressBar.tick();

        // then
        assertState("{'total':4,'current':0,'lastPassed':3,'multiple':true}");

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':0,'lastPassed':3,'multiple':true}");
    }

    @Test
    public void shouldNoSkipSingleLevel() {
        // given
        stateWhenFinishLevel2();

        // when
        player.getHero().loadLevel(3);
        progressBar.tick();

        // then
        assertState("{'total':4,'current':2,'lastPassed':1,'multiple':false}");

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':2,'lastPassed':1,'multiple':false}");
    }

    @Test
    public void shouldNoSelectBadLevelFromSingle() {
        // given
        stateWhenFinishLevel2();

        // when
        player.getHero().loadLevel(30);
        progressBar.tick();

        // then
        assertState("{'total':4,'current':2,'lastPassed':1,'multiple':false}");

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':2,'lastPassed':1,'multiple':false}");
    }

    @Test
    public void shouldNoSelectBadLevelFromMultiple() {
        // given
        stateWhenFinishLevel4();

        // when
        player.getHero().loadLevel(30);
        progressBar.tick();

        // then
        assertState("{'total':4,'current':0,'lastPassed':3,'multiple':true}");

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':0,'lastPassed':3,'multiple':true}");
    }

    @Test
    public void canGoToSameSingleLevel() {
        // given
        stateWhenFinishLevel2();

        // when
        player.getHero().loadLevel(2);
        progressBar.tick();

        // then
        assertState("{'total':4,'current':2,'lastPassed':1,'multiple':false}");

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':2,'lastPassed':1,'multiple':false}");
    }

    @Test
    public void shouldNoSkipMultipleLevel() {
        // given
        stateWhenFinishLevel2();

        // when
        player.getHero().loadLevel(4);
        progressBar.tick();

        // then
        assertState("{'total':4,'current':2,'lastPassed':1,'multiple':false}");

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':2,'lastPassed':1,'multiple':false}");
    }

    @Test
    public void shouldResetCurrentSingleLevel() {
        // given
        stateWhenFinishLevel2();

        // when
        player.getHero().loadLevel(-1);
        progressBar.tick();

        // then
        assertState("{'total':4,'current':2,'lastPassed':1,'multiple':false}");

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':2,'lastPassed':1,'multiple':false}");
    }

    @Test
    public void shouldResetCurrentMultipleLevel() {
        // given
        stateWhenFinishLevel4();

        // when
        player.getHero().loadLevel(-1);
        progressBar.tick();

        // then
        assertState("{'total':4,'current':0,'lastPassed':3,'multiple':true}");

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':0,'lastPassed':3,'multiple':true}");
    }

    @Test
    public void remakeHeroWhenDieOnSingle() {
        // given
        stateWhenFinishLevel1();

        // when
        player.getHero().die();

        // then
        assertFalse(player.getHero().isAlive());

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':1,'lastPassed':0,'multiple':false}");
        assertTrue(player.getHero().isAlive());

        // when
        progressBar.tick();

        // then
        assertState("{'total':4,'current':1,'lastPassed':0,'multiple':false}");
        assertTrue(player.getHero().isAlive());
    }

    @Test
    public void shouldSelectSingleIfPassed() {
        // given
        stateWhenFinishLevel2();
        assertState("{'total':4,'current':2,'lastPassed':1,'multiple':false}");

        // when
        progressBar.loadProgress("{'total':4,'current':1,'lastPassed':1,'multiple':false}");

        // then
        assertState("{'total':4,'current':1,'lastPassed':1,'multiple':false}");
    }

    @Test
    public void shouldSelectSingleIfNotPassed() {
        // given
        stateWhenFinishLevel2();
        assertState("{'total':4,'current':2,'lastPassed':1,'multiple':false}");

        // when
        progressBar.loadProgress("{'total':4,'current':3,'lastPassed':1,'multiple':false}");

        // then
        assertState("{'total':4,'current':3,'lastPassed':1,'multiple':false}");
    }

    @Test
    public void shouldSelectMultipleIfNotPassed() {
        // given
        stateWhenFinishLevel2();
        assertState("{'total':4,'current':2,'lastPassed':1,'multiple':false}");

        // when
        progressBar.loadProgress("{'total':4,'current':0,'lastPassed':3,'multiple':true}");

        // then
        assertState("{'total':4,'current':0,'lastPassed':3,'multiple':true}");
    }
}
