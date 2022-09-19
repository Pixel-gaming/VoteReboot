package com.c0d3m4513r.voterebootapi;

import lombok.Getter;

public abstract class RSMCPerm {
    @Getter
    protected String readPermission;
    @Getter
    protected String startPermission;
    @Getter
    protected String modifyPermission;
    @Getter
    protected String cancelPermission;
}
