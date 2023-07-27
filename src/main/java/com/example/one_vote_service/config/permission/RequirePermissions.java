package com.example.one_vote_service.config.permission;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermissions {
    Permission[] value();
}
