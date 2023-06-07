When cloning repository with submodules with ssh, then only main repository is configured to use ssh.
All submodules are configured to use http. It is possible to check in `.git/modules/{module_path}/config`.
In this case push to submodule won't work using scripts from `./build` folder. To fix submodule repo url
you can run script from `./build/git-modules-http-to-ssh.sh`. This script will go through all submodules
and replace url from http to ssh.