package com.codenjoy.dojo.web.controller;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
@AllArgsConstructor
public class GithubUsernamesCache {

    private final Set<String> githubUsernames = ConcurrentHashMap.newKeySet();
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    public boolean containsGithubUsername(String githubUsername) {
        try {
            readWriteLock.readLock().lock();
            return githubUsernames.contains(githubUsername);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public boolean isUniqueGithubUsername(String githubUsername) {
        try {
            readWriteLock.writeLock().lock();
            if (!githubUsernames.contains(githubUsername)) {
                githubUsernames.add(githubUsername);
                return true;
            }
            return false;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public void removeByGithubUsername(String githubUsername) {
        try {
            readWriteLock.writeLock().lock();
            Optional<String> usernameToBeRemoved = githubUsernames.stream()
                    .filter(username -> username.equals(githubUsername))
                    .findAny();
            usernameToBeRemoved.ifPresent(githubUsernames::remove);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public void clearCache() {
        try {
            readWriteLock.writeLock().lock();
            githubUsernames.clear();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}