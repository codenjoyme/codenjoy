package net.tetris.online.service;

import java.io.FilePermission;
import java.lang.reflect.ReflectPermission;
import java.net.SocketPermission;
import java.security.Permission;
import java.util.PropertyPermission;

/**
 * User: serhiy.zelenin
 * Date: 11/11/12
 * Time: 6:56 PM
 */
public class RestrictedPermissions {
    private Permission[] restrictedPermissions = new Permission[]{
            new FilePermission("/-", "read"),
            new FilePermission("c:\\-", "read"),
            new SocketPermission("127.0.0.1", "accept,listen,resolve,connect"),
            new PropertyPermission("*", "read,write"),
            new PropertyPermission("*", "read,write"),
            new RuntimePermission("accessDeclaredMembers"),
            new RuntimePermission("getClassLoader"),
            new ReflectPermission("suppressAccessChecks"),
    };

    public boolean implyAny(Permission permission) {
        for (Permission allowedPermission : restrictedPermissions) {
            if (allowedPermission.implies(permission)) {
                return true;
            }
        }
        return false;
    }

}
