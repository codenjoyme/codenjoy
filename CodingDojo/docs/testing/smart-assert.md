In the Unit testing methodology, the approach of atomicity of testing is accepted, when
1 test tests 1 aspect of the system and preferably contains 1 assert, the breakdown of which
naturally leads to the termination of the execution of this test.

We left this recommendation for the ideal world and deviate from it in our tests.
In the real world, where tests are not always lightning fast,
represent not only unit, but also integration/system (which requires for setting up
the system until the very moment of testing a lot of actions and time),
often requires more than 1 assert per 1 test.

On the project, we use several approaches to speed up interaction with tests.

Firstly, we check not specific fields of the tested
object one by one, but its toString representation, checking immediately
all the contents of the object. This approach is partially borrowed from
`approvals` approach of the eponymous library for testing.

For example, this is how we used to write tests:

```java
    @Test
    public void shouldTeleport_whenTurnLeft() {
        Walls walls = new Walls();
        Knibert board = new Knibert(generator, walls, BOARD_SIZE, printerFactory);
        hero = board.getHero();
        stone = board.getStone();

        hero.left();
        assertEquals("позиция X змейки", 4, hero.getX());
        assertEquals("позиция Y змейки", 4, hero.getY());
    
        for (int count = 0; count < BOARD_SIZE; count++) {
            board.tick();
        }
    
        assertEquals("позиция X змейки", 4, hero.getX());
        assertEquals("позиция Y змейки", 4, hero.getY());
        assertHeroDirection(Direction.LEFT);
    }
```

If it breaks on any of the assert's you'll see only:

```
позиция X змейки
Expected: 4
But was:  5 
```

And this will tell you almost nothing about what's wrong with the snake in the field.

Now we write our tests this way:

```java
    @Test
    public void shouldTeleport_whenTurnLeft() {
        // given
        givenBoardWithoutWalls();
    
        hero.left();
        board.tick();
        board.tick();
        board.tick();
        board.tick();
    
        // then
        asrtBrd("         \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "◄╕       \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n");
    
        // when
        board.tick();
    
        // then
        asrtBrd("         \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "╕       ◄\n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n");
    
        assertEquals(true, hero.isAlive());
        assertEquals(LEFT, hero.getDirection());
    
        // when
        board.tick();
    
        // then
        asrtBrd("         \n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "       ◄╕\n" +
                "         \n" +
                "         \n" +
                "         \n" +
                "         \n");
    
        assertEquals(true, hero.isAlive());
        assertEquals(LEFT, hero.getDirection());
    }
```

Secondly (another approach) such tests look like documentation.
We make considerable efforts for this, maintaining the test framework in
the proper level. Each time, highlighting (extract method) low-level
functionality from the test itself, increasing readability and eliminating
duplication. We know that the tests of the system are a separate project
testing the production part, with its own domain model, services,
utilities and of course OOP/Refactoring and even tests. Yes, yes, very rarely,
but still sometimes there are tests for tests - more often of course tests are tested in conjunction
with production code, by breaking it in the place that is tested. This is one of
recommendations of the TDD approach, which you can read in another article.

Thirdly, each assert in case of fail is maximally informative -
you will see the whole situation in the field, and not some small aspect.
And your favorite ide will help you and conveniently show you the diff of two string representations:
expected and actual.

Fourthly, the execution of the test will not stop at the broken assert for the reason that
we do not use Assert.assertEquals from the jUnit set, but a wrapper over it -
SmartAssert.assertEquals. The latter accumulates all errors and flies off when 
we ask - we do this in the @After block of the test.

```java
    @After
    public void after() {
        SmartAssert.checkResult();
    }
```

If there are more than 1 broken assert, you will see several blocks
`Expected: smth But was: smthOther` in a row. For each of them you can see
diff using your favorite ide. This greatly speeds up when you are actively working with tests.

We also use the notation `shouldWhatWill_whenCertainActions`.
Modifications are possible `shouldWhatWill_whenCertainActions_caseCondition1_ofSomethingElseThere`.

And inside the test we highlight several blocks, marking them with comments
`// given`, `// when` and `// then`.

You may notice that our tests are often also functional, 
that is, they test several `// when // then` parts that go in a row - they test not
a specific state, but a story. This makes the tests more fragile and inconvenient
in use, unless you use all of the above approaches.

Another approach in testing we call `paranoic mode` which says,
write tests not to confirm that you did everything right, but to
find another bug from the many that you left in the code. We believe that
programmers write bugs. If the programmer is more experienced, this does not mean that the bugs
he has less than a less experienced colleague - the frequency of bug generation is approximately
the same. A more experienced colleague just writes more evil bugs.
They are harder to catch. And if so - we often write redundant tests, a grid of tests of different types
unit/integration/system/smoke/acceptance, each of which is only an excuse to look at the code
from a different point of view with one goal - to find a bug. After this, all tests remain
and help in regression - we actively refactor our code, and worry less
for possible breakage during reorganization.