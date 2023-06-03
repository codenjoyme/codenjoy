Recommendations for CodeReview:

1. Run all the tests of the module.
1. Run all system tests (even if I fixed one module, which
   definitely won't affect anything).
1. Do a self-core review before committing (put on my hat  
   troll hat and look for what's bad).
1. Remove, if possible, all your TODOs and a few of those that were there before  
   (or immediately after this commit work on TODOs separately).
1. Think about how to improve code coverage, including running  
   mutation tests.
1. Look at the static code analyzer.
1. Look at IDE warnings, if you manage to bring up Warning to Error  
   at that - super). 
1. When changing something in a file, think about all the places in the 
   project where you can change it so that you can see the evolution of the 
   system not from commit to commit, rather than from file to file. 
1. Make sure you read all the commits - to learn something new. 
1. Make sure you review what you did the next day - you will have second
   new ideas on how to make this code better. Use ChatGPT to see how else you 
   could have done this piece of code - you'll often find interesting things 
   in it that you hadn't thought about before.
1. If you have time - don't be afraid to experiment and make the solution  
   in more than one way. It seems counterintuitive, but it's a contribution to your  
   future intuition when you have a sense of how to do better and how to
   definitely NOT to do.
1. When changing anything - try to follow the principle of baby steps - no more than  
   one simple fix (refactoring) per commit. This will also help the  
   reviewer when we look at the code and for you when we need to roll back an unsuccessful   
   experiment (not by Сtrl-Z, but by git rollback).
1. Good commit descriptions. One complete sentence with a description of  
   "what and why" is better than 2 words. 2 sentences are better than 1. 3 is better  
   than 2.
1. Don't use the git amendment - there aren't many reasons to justify  
   complicating the coderviewer's job. The only one that comes up is after
   you've made a commit, if you find a typo or a broken test
   (for being inattentive) - quickly refill it. But this only makes sense if it's been
   a couple of minutes since you commit and what you've done is an integral part of it.
1. Propose PR and give it for review, rather than merge, and then say:  
   "look at my code, I uploaded it there today".
1. Have the habit of team members taking new tasks in the area where  
   recently did a review. This way you can make your contribution to the code not only  
   theoretically, but also practically. Usually there is always something to fix after  
   any commit and subsequent review with fixes.
1. Don't do a review after a review, after a review, after a review. If you're not  
   understood immediately, it's better to call and work and work in pairs. 
   The recommendation on collective code ownership also applies, but only if the author  
   won't be offended that his implementation will be fixed later.
1. The reviewer should be able to easily view all the commits that went to  
   review. If he can miss something - most likely he will miss it.
1. When discussing code review comments, it is better to call than to chat in the  
   tool for code review.
1. Avoid holivars. If holivars start - it's better to call or attract
   a third party. Holivars are strategically bad for the team. Share
   the reasons that prompted you to choose this style of code writing, not the
   that the method you have chosen is the only correct one.
   There are always alternatives.
1. Be prepared that during the review you as a reviewer will also learn something new. Code review
   this is a process of knowledge exchange more than a code check process.
1. Each code review comment is better put into the knowledge base.
1. The code review knowledge base should be discussed with the team on a weekly basis.
1. All newcomers to the project read the project code review conventions at the start.
1. If you can "tweak the nuts and bolts" of static analyzers or warnings
   IDE for a specific code review comment - it should be done immediately.
1. If the author wants to be praised, he should tell the team about it immediately
   and do not use the code review process for praise. More often
   code review will show where it's not so good and how you can do better. This
   a tool for gaining new knowledge.
1. The reviewer should also learn to mark places that
   were done well. Especially if last time there was a comment to fix on
   this topic. In our culture, more criticism than praise. Need to force yourself
   praise for something, even the most insignificant. If something seems insignificant to you
   for you, it doesn't mean it's just as insignificant for the author.
1. It is worth looking at not only what is fixed, but also the general context - class,
   in which the correction was made, the package in which the class is located. Knowing more about
   existing API can pick up more recommendations. Code review this is not
   so much about what is done - how much about what is not done yet.
1. Besides the selected implementation style may differ from the usual in the project  
   (even if outdated by industry standards). It is important that  
   the overall style is preserved - this will reduce the amount of entropy (measure of chaos  
   system). In the future, you can decide that something should be  
   fixed throughout the project - and after implementing this task, the general  
   recommendations will be supplemented with new ones that are "OK" for the industry.
1. Often, during a comprehensive review (the entire module), the idea arises that  
   can be additionally improved - but at the same time the recommendation  
   does not lie as a remark to the Author. It is worth taking out such ideas in the issue  
   tracking system.

Checklist for CodeReview borrowed from ChatGPT (with which there is an agreement):

1. Spend time studying the code before you start reviewing.
1. Be attentive to the author's comments and try to understand their intention.
1. Does the code comply with the established design standards?
1. Do all variable and function names clearly reflect their purpose?
1. Is there any code duplication? If so, recommend using functions  
   or classes for reuse.
1. Does the code read and understand without the need for additional comments?
1. Are exceptions and errors handled correctly?
1. Contains the code "magic numbers" and "magic strings"? Instead of this  
   the code should use constants or variables.
1. Pay attention to the efficiency of the code and possible performance improvements.
1. Is the code safe, does it contain vulnerabilities?
1. Are all unused variables and imports removed?
1. Is the code well structured, is it divided into functions or modules?
1. Are all comments in the code relevant and informative?
1. Is the code covered by a sufficient number of tests?
1. Does the code contain redundant or unreachable logic?
1. Does the length of functions and classes exceed a reasonable limit?
1. Does the code contain a "hard" dependency on a specific implementation or platform?
1. Does the code contain unnecessary operations that can slow it down?
1. Does the code use outdated or unreliable methods and functions?
1. Does the readability and style of the code comply with established guidelines  
   code design (Java Code conventions).
1. Does the code contain errors such as typos, incorrect use
   operators, etc.?
1. Does the code contain unnecessary or non-working parts?
1. Does the code cause blocking, deadlock or other parallel problems?
1. Does the code correctly handle and validate input data?
1. Are all functions and methods well documented?
1. Does the code contain inefficient or unnecessary operations?
1. Are all resources, such as files or network connections, properly
   managed and released after use?
1. Does the code contain unreachable code or unreachable branches?
1. Does the code use outdated libraries or APIs?
1. Does the code contain too complex or confusing constructs?
1. Does the code contain commented code or debug outputs?
1. Does the code contain "magic" operations that can be difficult
   to understand without comments?
1. Does the code contain implicit dependencies or side effects?
1. Does the code contain unnecessary or redundant conditional operators?
1. Does the code contain repeated blocks of code that can be extracted  
   into separate functions or methods?
1. Does the code contain errors in stream processing or parallel operations?
1. Does the code contain unprotected access to confidential data or  
   personal information?
1. Does the code contain incorrect use of APIs or libraries?
1. Does the code contain potentially infinite loops or recursive  
   calls without a base case?
1. Does the code contain "hardcoded" paths or values that can
    change in the future?
1. Does the code contain inefficient use of memory or resources?
1. Does the code contain unclear or ambiguous comments?
1. Does the code contain potentially dangerous operations, such as execution  
   external commands or SQL injections?
1. Does the code contain incorrect or inadequate error handling?
1. Does the code contain problems with multithreading, such as state
1. Does the code use object-oriented principles correctly, such as encapsulation, 
   inheritance and polymorphism?
1. Are SOLID principles observed?
1. Is the KISS principle observed?
1. Is the DRY principle observed?
1. Is the YAAGNI principle observed?
1. Does the code contain unnecessary or unused: variables, functions
   or methods, classes, modules, packages, libraries, dependencies, resources
   or files?
1. Does the code contain unnecessary complexity or redundant details?
1. Does the code contain "code smells", such as long
   methods, a large number of parameters or poor variable naming?
1. Does the code meet the goals of the project and solve the problem efficiently?