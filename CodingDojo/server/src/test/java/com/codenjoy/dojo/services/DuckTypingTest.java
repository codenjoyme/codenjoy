package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.ProxyFactory;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DuckTypingTest {

    interface Quackable {
        String quack();
    }

    interface SomeOtherQuackable {
        String quack();
    }

    interface Mooable {
        String moo();
    }

    class GumDuck {
        public String quack() {
            return "Eeeeky! Eeeeky!";
        }
    }

    class RealDuck {
        public String quack() {
            return "Quack! Quack!";
        }
    }

    interface Monster {
        String quack();
        String moo();
    }

    interface SomeOtherMonster extends Monster {
        String bowWof();
    }

    class RealMonster {
        public String quack() {
            return "Quack! Quack!";
        }

        public String moo() {
            return "Moo! Moo!";
        }
    }

    class SomeOtherRealMonster extends RealMonster  {
        public String bowWof() {
            return "Bow Wof!";
        }
    }

    interface ParametrizedMonster {
        String quack(Object object);
        String moo(Object object);
    }

    interface OtherLanguageMonster {
        Object quack();
        Object moo();
    }

    interface PrimitiveMonster {
        int quack();
        int moo();
    }

    class RealPrimitiveMonster {
        public int quack() {
            return 0;
        }

        public int moo() {
            return 0;
        }
    }

    interface PrimitiveWrapperMonster {
        Integer quack();
        Integer moo();
    }

    class RealPrimitiveWrapperMonster {
        public Integer quack() {
            return 0;
        }

        public Integer moo() {
            return 0;
        }
    }


    @Test
    public void shouldCallRealMethodWhenCallItAtProxy(){
        // given
        GumDuck duck = mock(GumDuck.class);
        Quackable quackable = ProxyFactory.object(duck).getAs(Quackable.class);

        // when
        quackable.quack();

        // then
        verify(duck).quack();
    }

    @Test
    public void shouldWorkWithAnyObjects(){
        // given
        RealDuck duck = mock(RealDuck.class);
        Quackable quackable = ProxyFactory.object(duck).getAs(Quackable.class);

        // when
        quackable.quack();

        // then
        verify(duck).quack();
    }

    @Test
    public void shouldWorkWithAnyInterfaces(){
        // given
        RealDuck duck = mock(RealDuck.class);
        SomeOtherQuackable quackable =
                ProxyFactory.object(duck).getAs(SomeOtherQuackable.class);

        // when
        quackable.quack();

        // then
        verify(duck).quack();
    }

    @Test
    public void shouldExceptionIfMethodNotFound(){
        try {
            ProxyFactory.object(new RealDuck()).getAs(Mooable.class);
            fail("Expected exception");
        } catch (Exception e) {
            assertMessageContains(e, "Unable to find method 'moo' in " +
                    "com.codenjoy.dojo.services.DuckTypingTest$RealDuck with parameter type(s) []");
        }
    }

    @Test
    public void shouldWorkForAllMethods(){
        // given
        RealMonster realMonster = mock(RealMonster.class);
        Monster monster = ProxyFactory.object(realMonster).getAs(Monster.class);

        monster.quack(); // when
        verify(realMonster).quack(); // then

        monster.moo(); // when
        verify(realMonster).moo(); // then
    }

    @Test
    public void shouldExceptionIfSomeOfMethodsNotFound(){
        try {
            ProxyFactory.object(new RealMonster()).getAs(SomeOtherMonster.class);
            fail("Expected exception");
        } catch (Exception e) {
            assertMessageContains(e, "Unable to find method 'bowWof' in " +
                    "com.codenjoy.dojo.services.DuckTypingTest$RealMonster with parameter type(s) []");
        }
    }

    @Test
    public void shouldExceptionIfSomeOfMethodsHasDifferentParameters(){
        try {
            ProxyFactory.object(new RealMonster()).getAs(ParametrizedMonster.class);
            fail("Expected exception");
        } catch (Exception e) {
            assertMessageContains(e, "Unable to find method '*' in " +
                    "com.codenjoy.dojo.services.DuckTypingTest$RealMonster with parameter type(s) [class java.lang.Object]");
        }
    }

    private void assertMessageContains(Exception exception, String messageMask) {
        String[] split = StringUtils.split(messageMask, "*");
        for (String string : split) {
            if (!exception.getMessage().contains(string)) {
                fail(String.format("\nExpected: \"%s\"\n But was: \"%s\"",
                        messageMask, exception.getMessage()));
            }
        }
    }

    @Test
    public void shouldExceptionIfSomeOfMethodsHasDifferentResultType(){
        try {
            ProxyFactory.object(new RealMonster()).getAs(OtherLanguageMonster.class);
            fail("Expected exception");
        } catch (Exception e) {
            assertMessageContains(e, "Unable to find method '*' in " +
                    "com.codenjoy.dojo.services.DuckTypingTest$RealMonster with parameter type(s) [] " +
                    "with return type java.lang.Object");
        }
    }

    @Test
    public void shouldWorkForAllPrimitiveMethods(){
        // given
        RealPrimitiveMonster realMonster = mock(RealPrimitiveMonster.class);
        PrimitiveMonster monster =
                ProxyFactory.object(realMonster).getAs(PrimitiveMonster.class);

        monster.quack(); // when
        verify(realMonster).quack(); // then

        monster.moo(); // when
        verify(realMonster).moo(); // then
    }

    @Test
    public void shouldExceptionIfSomeOfMethodsHasPrimitiveWrapperResultType(){
        try {
            ProxyFactory.object(new RealPrimitiveMonster()).getAs(PrimitiveWrapperMonster.class);
            fail("Expected exception");
        } catch (Exception e) {
            assertMessageContains(e, "Unable to find method '*' in " +
                    "com.codenjoy.dojo.services.DuckTypingTest$RealPrimitiveMonster with parameter type(s) [] " +
                    "with return type java.lang.Integer");
        }
    }

    @Test
    public void shouldExceptionIfSomeOfMethodsHasPrimitiveWrapperResultTypeViceVersa(){
        try {
            ProxyFactory.object(new RealPrimitiveWrapperMonster()).getAs(PrimitiveMonster.class);
            fail("Expected exception");
        } catch (Exception e) {
            assertMessageContains(e, "Unable to find method '*' in " +
                    "com.codenjoy.dojo.services.DuckTypingTest$RealPrimitiveWrapperMonster with parameter type(s) [] " +
                    "with return type int");
        }
    }

    @Test
    public void shouldWorkForAllInterfaceInheritance(){
        // given
        SomeOtherRealMonster realMonster = mock(SomeOtherRealMonster.class);
        SomeOtherMonster monster =
                ProxyFactory.object(realMonster).getAs(SomeOtherMonster.class);

        monster.quack(); // when
        verify(realMonster).quack(); // then

        monster.moo(); // when
        verify(realMonster).moo(); // then

        monster.bowWof(); // when
        verify(realMonster).bowWof(); // then
    }

    @Test
    public void shouldWorkForAllInterfaceInheritance_forSuperInterface(){
        // given
        SomeOtherRealMonster realMonster = mock(SomeOtherRealMonster.class);
        Monster monster =
                ProxyFactory.object(realMonster).getAs(Monster.class);

        monster.quack(); // when
        verify(realMonster).quack(); // then

        monster.moo(); // when
        verify(realMonster).moo(); // then

//        monster.bowWof(); // when
//        verify(realMonster).bowWof(); // then
    }
}
