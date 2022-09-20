package com.c0d3m4513r.votereboot;

/***
 * This Class holds permissions to read, start, modify and cancel an action
 */
public interface ActionPerm {
    String getPermission(Action action);
}
