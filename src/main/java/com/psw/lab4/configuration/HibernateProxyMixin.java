package com.psw.lab4.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class HibernateProxyMixin {
    @JsonIgnore
    public abstract Object getHibernateLazyInitializer();
}
