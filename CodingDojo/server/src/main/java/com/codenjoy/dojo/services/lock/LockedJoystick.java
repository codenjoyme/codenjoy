package com.codenjoy.dojo.services.lock;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.services.Joystick;

import java.util.concurrent.locks.ReadWriteLock;

public class LockedJoystick implements Joystick {

    private ReadWriteLock lock;
    private Joystick joystick;

    public LockedJoystick(ReadWriteLock lock) {
        this.lock = lock;
    }

    public Joystick wrap(Joystick joystick) {
        this.joystick = joystick;
        return this;
    }

    @Override
    public void down() {
        lock.writeLock().lock();
        try {
            joystick.down();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void up() {
        lock.writeLock().lock();
        try {
            joystick.up();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void left() {
        lock.writeLock().lock();
        try {
            joystick.left();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void right() {
        lock.writeLock().lock();
        try {
            joystick.right();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void act(int... p) {
        lock.writeLock().lock();
        try {
            joystick.act(p);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void message(String command) {
        lock.writeLock().lock();
        try {
            joystick.message(command);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Joystick getWrapped() {
        return joystick;
    }
}
