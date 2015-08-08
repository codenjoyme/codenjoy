package com.codenjoy.dojo.football;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.codenjoy.dojo.football.client.*;
import com.codenjoy.dojo.football.model.*;
import com.codenjoy.dojo.football.services.*;

@RunWith(Suite.class)
@SuiteClasses({ 
	SolverTest.class,
	FootballPerormanceTest.class,
	FootballTest.class,
	SingleTest.class,
	ScoresTest.class
})
public class AllTests {

}
