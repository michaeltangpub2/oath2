package com.michaeltang.OAuth.model;

import java.util.Set;

public class ClientInfo {
    Set<String> scope;

    public Set<String> getScope() {
        return scope;
    }

    public void setScope(Set<String> scope) {
        this.scope = scope;
    }
}
