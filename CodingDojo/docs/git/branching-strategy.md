The project consists of a main repository and a series of sub-repositories. Sub-repositories are created
for individual games, clients for playing in a particular programming language, and individual applications
and their components. This is done for the convenience of the contribute, because the project is open source and different
people can contribute different parts of the project.

In the main repository, we use a lightweight version of git-flow. And in all other submodules
trunk-based development is used. Why is that? To simplify the operation of pouring changes.
We tried git-flow everywhere and it led to more confusion than we felt from this benefit.

In the main repository, we use the following branches: master, develop, dojorena-release, feature/*.
master - a branch that contains the latest stable version of the project.
develop - a branch that contains the latest changes that developers are working on.
dojorena-release - a branch that contains changes ready for release on dojorena.io.
There we also mark the tags of the versions that went to production (dojorena.io).
feature /* - branches that contain changes that developers are working on now.

Submodules use trunk-based development. I.e. the master branch is the main development branch.
It contains the latest stable version. The develop branch is not used.
The feature/* branches are also not used. You should be extremely careful when working with submodules,
because your breakdown can affect the work of other developers who decided to pull the master to themselves.
Do not worry that the changes sent will affect the master in the main repository.
Since he knows nothing about your changes. To make your changes appear in the main repository
you need to commit new hashes of sub-repositories in the main one.

When proposing a pull-request to the main repository, you must make sure that all tests in all submodules
pass successfully. pull-request is made with the feature/* branch in the develop branch of the main repository.
It should also include all the necessary changes in the submodules (in master). Also
it is worth linking the task from the kanban board on the GitHub project in the pull-request.

After code review, the Project Leader can accept changes to the develop branch of the main repository.
After that, the developer can delete the feature/* branch from the main repository and close the task
moving it to the "Done" column on the kanban board.

For convenience of working with submodules, we use a script that allows you to do this automatically.
The script is located in `./build/git-push-develop-as-branch.sh`. It allows you to offer the current branch
(usually this is develop) as feature/*, as well as send it to the server and all submodules (in their master
branches). At the same time, the password for the key will be asked only once. We use the git:// protocol.

To pull all changes across all submodules, you can use the script
`./build/git-pull-submodules.sh`.

Commits in all repositories must contain a component prefix. For example, if you make
changes to the csharp client, then the commit must have the prefix `[csharp] [client]`.
If you make changes to the server admin, the engine project and the java client, then in the commit
must have the prefix `[server] [admin] [engine] [client] [java]`. The descriptive part of the commit
should be in English and explain in detail what was done and why.
You can use multiple lines for this.

Do not use amend, squash, rebase and other git options that can change the commit history.
The commit history is important for future analysis of what exactly happened in the project.

Commit always changes in sub-modules simultaneously with hash-commits of these changes
in the main repository with other changes in the main repository. So it's easier to do
code review - it is always done in the main repository. It also makes it easier to track
changes in submodules.

Try to make commits as atomic (frequent) as possible.

In working on the locale, try to keep the green bar in the tests - so that at any time
your changes could be offered as a pull-request and everything worked. Do not keep the branch
with changes for a long time on the locale. Try to offer PR every day (and it's better several).
A large task should be divided into subtasks and make PR on them.