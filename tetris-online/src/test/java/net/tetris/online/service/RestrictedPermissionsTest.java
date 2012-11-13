package net.tetris.online.service;

import org.junit.Test;

import java.io.FilePermission;
import java.lang.reflect.ReflectPermission;
import java.net.SocketPermission;
import java.util.PropertyPermission;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * User: serhiy.zelenin
 * Date: 11/11/12
 * Time: 11:29 PM
 */
public class RestrictedPermissionsTest {

    private final RestrictedPermissions restrictions;

    public RestrictedPermissionsTest() {
        restrictions = new RestrictedPermissions();
    }

    @Test
    public void shouldRestrictRuntimePermission() {
        assertFalse(restrictions.implyAny(new RuntimePermission("exitVM")));
        assertTrue(restrictions.implyAny(new RuntimePermission("getClassLoader")));
        assertTrue(restrictions.implyAny(new RuntimePermission("accessDeclaredMembers")));
        assertTrue(restrictions.implyAny(new ReflectPermission("suppressAccessChecks")));
    }

    @Test
    public void shouldRestrictFilePermissions() {
        assertTrue(restrictions.implyAny(new FilePermission("/user/home/.tetris/score.txt", "read")));
        assertTrue(restrictions.implyAny(new FilePermission("C:\\Users\\serhiy.zelenin\\.tetris\\scores.txt", "read")));
        assertFalse(restrictions.implyAny(new FilePermission("/user/home/.tetris/score.txt", "write,execute")));
    }

    @Test
    public void shouldAllowSocketToLocalhostOnly() {
        assertTrue(restrictions.implyAny(new SocketPermission("127.0.0.1", "accept,listen,resolve,connect")));
        assertFalse(restrictions.implyAny(new SocketPermission("google.com", "connect")));
    }

    @Test
    public void shouldAllowPropertyPermissions() {
        assertTrue(restrictions.implyAny(new PropertyPermission("*", "read")));
    }
}
