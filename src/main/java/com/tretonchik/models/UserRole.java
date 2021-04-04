package com.tretonchik.models;

import io.javalin.core.security.Role;

public enum UserRole implements Role {
    ADMIN,
    USER
}
