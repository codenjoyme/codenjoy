During development, run all tests of the module you are working with now.
This will quickly detect errors that you have inadvertently introduced into the code.
If changes were made in a few lines and the tests broke -
it's easier for you to understand where the error is without resorting to debug.

If you postpone running the tests "later", then errors will 
accumulate. Before preparing a pull request, when you think that
all the work is already done, running all the tests you will be surprised -
the tests broke and in other places. And since there are many changes -
you will have to figure out for a long time where exactly the errors were, were these errors in
code before your changes or you introduced them. Here you will have from the tools
only debug - an expensive tool, because you practically do not
control it. You can only hope that you can reproduce
error and continue to monotonously press the Step Over key. Error for
error - all that you have accumulated during the development of the feature.

In contrast to debug, you can use the baby steps approach and all types of tests.

We try to make the tests so that the time to execute them is minimal.
But there are also tests that take longer to run. Their accordingly
you will want to run less often. Because the compromise can be offered
such a sequence (from faster but less informative to longer):
- \[milliseconds\] Run the unit tests of the class you are currently working on.
- \[seconds\] Run the unit tests of the package containing the mutable class.
- \[tens of seconds\] Run all tests of the module (project).
- \[minutes\] Run all tests of dependent modules (for example, java-client
  used in the engine, which in turn is used in each game,
  which are added as a plugin to the server).
- \[10 minutes\] Run all project tests via maven command
  `mvn clean install --fail-at-end`.

It is also important to note that there is a difference in running tests via maven and in IDE.
The intellij idea plugin runs tests so that each time a new test is run
the test context (class) is recreated. Maven runs tests in one
context. Therefore, if you run tests via maven, you can
get an error that you won't get if you run tests via IDE.
This is due to the fact that tests can change the state of the class, and other tests
may not expect this. And this "pops up" at the very end, which does not add
pleasant emotions. Here you are ready to commit - and it turns out that you need to debug the tests.
Therefore, run as many tests as you can as often as you can -
the rest is postponed for later, but not for long.

It is also a good recommendation to run all tests before starting work on a feature,
as well as before git pull and immediately after it. So you will definitely know that the breakdowns
that appeared are not yours - and then you can highlight it to the team.

It is important to understand that a broken test is your quest if:
- it happened to you locally, even if it is not your test or functionality,
  find the author and figure it out with him together - your task.
- it happened to someone on the team, but in your PR.