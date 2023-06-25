package com.psw.lab4.configuration;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.hibernate.proxy.HibernateProxy;

public class HibernateProxyModule extends SimpleModule {
    public HibernateProxyModule() {
        setMixInAnnotation(HibernateProxy.class, HibernateProxyMixin.class);
    }
}
