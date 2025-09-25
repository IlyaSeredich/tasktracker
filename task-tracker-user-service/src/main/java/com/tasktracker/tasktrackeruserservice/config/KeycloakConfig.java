package com.tasktracker.tasktrackeruserservice.config;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
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

            RoleRepresentation userRoleRepresentation = new RoleRepresentation();
            userRoleRepresentation.setName("user");

            RoleRepresentation adminRoleRepresentation = new RoleRepresentation();
            adminRoleRepresentation.setName("admin");

            keycloak.realm("task-tracker-realm")
                    .roles()
                    .create(userRoleRepresentation);

            keycloak.realm("task-tracker-realm")
                    .roles()
                    .create(adminRoleRepresentation);

            ClientRepresentation manageRepresentation = new ClientRepresentation();
            manageRepresentation.setClientId("task-tracker-manage-client");
            manageRepresentation.setStandardFlowEnabled(true);
            manageRepresentation.setPublicClient(false);
            manageRepresentation.setServiceAccountsEnabled(true);
            manageRepresentation.setRedirectUris(List.of("http://localhost:8080/*"));

            ClientRepresentation clientRepresentation = new ClientRepresentation();
            clientRepresentation.setClientId("task-tracker-auth-client");
            clientRepresentation.setStandardFlowEnabled(true);
            clientRepresentation.setPublicClient(false);
            clientRepresentation.setRedirectUris(List.of("http://localhost:8080/*"));

            keycloak.realm("task-tracker-realm")
                    .clients()
                    .create(clientRepresentation)
            ;

            Response response = keycloak.realm("task-tracker-realm")
                    .clients()
                    .create(manageRepresentation);

            String manageClientUUID = CreatedResponseUtil.getCreatedId(response);

            UserRepresentation serviceAccountUser = keycloak.realm("task-tracker-realm")
                    .clients()
                    .get(manageClientUUID)
                    .getServiceAccountUser();

            RealmResource realmResource = keycloak.realm("task-tracker-realm");
            ClientRepresentation realmMgmntRepresentation = realmResource.clients()
                    .findByClientId("realm-management")
                    .get(0);

            List<RoleRepresentation> roles = List.of(
                    keycloak.realm("task-tracker-realm")
                            .clients()
                            .get(realmMgmntRepresentation.getId())
                            .roles()
                            .get("manage-users")
                            .toRepresentation()
            );

            realmResource.users()
                    .get(serviceAccountUser.getId())
                    .roles()
                    .clientLevel(realmMgmntRepresentation.getId())
                    .add(roles);

            List<RoleRepresentation> roles2 = List.of(
                    realmResource.roles()
                            .get("user")
                            .toRepresentation()
            );

            realmResource.users()
                    .get(serviceAccountUser.getId())
                    .roles()
                    .realmLevel()
                    .add(roles2);
        }

    }

}
