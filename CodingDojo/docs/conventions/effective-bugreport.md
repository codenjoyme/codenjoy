If your chat bugreport looks one-sided, e.g.

`git clone for some reason started to fail`

You should pay attention to the article
[Egor Egorov 'About effective bugreports'](https://egorfine.com/articles/effective-bugreports/).

After your bugreport may become more detailed:

```
when executing the command

    > git clone --recursive

the project was not cloned due to an error

    > fatal: remote error: upload-pack: not our ref ac25b66cdb4ee0b4fee633e3b91c3a0d22018256
    > fatal: the remote end hung up unexpectedly
    > Fetched in submodule path 'CodingDojo/client-runner', but it did not contain ac25b66cdb4ee0b4fee633e3b91c3a0d22018256. Direct fetching of that commit failed.
    
cloned under the console MINGW64 under windows10

all log attached (git-clone-error.txt)
```

Of course the full bugreport can be redundant and often enough
a piece of log (from the first warning/error to the last; better in text form,
but you can also use a screenshot) and launch conditions (how to achieve this).

But if you don't give details, then the potential performer will have to
ask additional clarifying questions, to which you, as the author of the request,
will definitely answer, but only when you see them.
Most likely you expected a solution, but you will only get clarifying questions -
this is all the time. And if so - you can speed up.

A good habit before you write something in any request, immediately
ask yourself the question: "what else can they ask me,
reading my request?" And immediately answer these questions in the request.

Well, and periodically reread the article
[Egor Egorov 'About effective bugreports'](https://egorfine.com/articles/effective-bugreports/).