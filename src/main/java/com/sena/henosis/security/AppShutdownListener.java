package com.sena.henosis.security;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.event.ContextClosedEvent;

@Component
public class AppShutdownListener implements ApplicationListener<ContextClosedEvent> {

    private final SessionRegistry sessionRegistry;

    @Autowired
    public AppShutdownListener(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // Al detener la aplicación, invalidar todas las sesiones activas
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            for (SessionInformation session : sessionRegistry.getAllSessions(principal, false)) {
                session.expireNow();
            }
        }
    }
}

