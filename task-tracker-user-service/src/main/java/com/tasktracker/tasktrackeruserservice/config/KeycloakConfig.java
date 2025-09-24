package com.tasktracker.tasktrackeruserservice.config;

import jakarta.annotation.PostConstruct;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class KeycloakConfig {
    private final String serverUrl = "http://localhost:8080/";
    private final String realm = "master";
    private final String clientId = "admin-cli";
    private final String username = "admin";
    private final String password = "admin";

    private Keycloak keycloak;
    private RealmRepresentation taskTrackerRealm;
    private ClientRepresentation taskTrackerUserClient;

    @PostConstruct
    private void init() {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .username(username)
                .password(password)
                .build();

        List<RealmRepresentation> representations = keycloak.realms().findAll();
        boolean exists = representations.stream()
                .anyMatch(realm -> realm.getRealm().equals("task-tracker-realm"));

        if(!exists) {
            taskTrackerRealm = new RealmRepresentation();
            taskTrackerRealm.setRealm("task-tracker-realm");
            taskTrackerRealm.setEnabled(true);

            keycloak.realms().create(taskTrackerRealm);

            ClientRepresentation clientRepresentation = new ClientRepresentation();
            keycloak.realm("task-tracker-realm").clients().create(clientRepresentation);
        }

    }

}
