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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class KeycloakConfig {
    @Value("${keycloak.auth-server-url}")
    private String serverUrl;
    @Value("${keycloak.root.realm}")
    private String realm;
    @Value("${keycloak.root.client}")
    private String clientId;
    @Value("${keycloak.root.username}")
    private String username;
    @Value("${keycloak.root.password}")
    private String password;
    @Value("${keycloak.credentials.secret}")
    private String secret;
    @Value("${keycloak.realm}")
    private String appRealm;

    private Keycloak keycloak;
    private RealmResource realmResource;

    @PostConstruct
    private void init() {
        initKeycloak();

        if (!isAppRealmExists()) {
            createKeycloakEnv();
        }
    }

    private void initKeycloak() {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .username(username)
                .password(password)
                .build();
    }

    private boolean isAppRealmExists() {
        return keycloak.realms().findAll().stream()
                .anyMatch(realmRepresentation ->
                        realmRepresentation.getRealm().equals(appRealm));
    }

    private void createKeycloakEnv() {
        configureRealm();
        configureClients();
    }

    private void configureRealm() {
        createRealm();
        createRealmRoles();
    }

    private void configureClients() {
        createAuthClient();
        createManageUsersClient();
    }

    private void createRealm() {
        RealmRepresentation taskTrackerRealm = new RealmRepresentation();
        taskTrackerRealm.setRealm(appRealm);
        taskTrackerRealm.setEnabled(true);
        keycloak.realms().create(taskTrackerRealm);
        realmResource = keycloak.realm(appRealm);
    }

    private void createRealmRoles() {
        createRealmRole("user");
        createRealmRole("admin");
    }

    private void createRealmRole(String name) {
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName(name);

        realmResource
                .roles()
                .create(roleRepresentation);
    }

    private Response createAuthClient() {
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId("task-tracker-auth-client");
        clientRepresentation.setStandardFlowEnabled(true);
        clientRepresentation.setPublicClient(false);
        clientRepresentation.setRedirectUris(List.of("http://localhost:8080/*"));

        return realmResource
                .clients()
                .create(clientRepresentation);
    }

    private void createManageUsersClient() {
        ClientRepresentation manageRepresentation = new ClientRepresentation();
        manageRepresentation.setClientId("task-tracker-manage-client");
        manageRepresentation.setStandardFlowEnabled(true);
        manageRepresentation.setPublicClient(false);
        manageRepresentation.setServiceAccountsEnabled(true);
        manageRepresentation.setSecret(secret);
        manageRepresentation.setRedirectUris(List.of("http://localhost:8080/*"));

        Response response = realmResource
                .clients()
                .create(manageRepresentation);

        configureManageUsersClient(response);
    }

    private void configureManageUsersClient(Response response) {
        String manageClientUUID = CreatedResponseUtil.getCreatedId(response);
        UserRepresentation serviceAccountUser = getServiceAccountUser(manageClientUUID);
        addRolesToServiceUser(serviceAccountUser);
    }

    private UserRepresentation getServiceAccountUser(String manageClientUUID) {
        return realmResource
                .clients()
                .get(manageClientUUID)
                .getServiceAccountUser();
    }

    private void addRolesToServiceUser(UserRepresentation serviceAccountUser) {
        addClientLvlRoles(serviceAccountUser);
        addRealmLvlRoles(serviceAccountUser);
    }

    private void addClientLvlRoles(UserRepresentation serviceAccountUser) {
        addRealmMgmntRole(serviceAccountUser);
        addViewRealmRole(serviceAccountUser);
    }

    private void addRealmLvlRoles(UserRepresentation serviceAccountUser) {
        addAdminRole(serviceAccountUser);
    }

    private void addRealmMgmntRole(UserRepresentation serviceAccountUser) {
        String realmMgmntId = getRealmMgmntId();
        RoleRepresentation manageRealmRole = getManageRealmRole(realmMgmntId);
        RoleRepresentation manageUsersRole = getManageUsersRole(realmMgmntId);


        realmResource.users()
                .get(serviceAccountUser.getId())
                .roles()
                .clientLevel(realmMgmntId)
                .add(List.of(manageRealmRole, manageUsersRole));
    }

    private void addViewRealmRole(UserRepresentation serviceAccountUser) {
        String realmMgmntId = getRealmMgmntId();
        RoleRepresentation viewRealmRole = getViewRealmRole(realmMgmntId);

        realmResource.users()
                .get(serviceAccountUser.getId())
                .roles()
                .clientLevel(realmMgmntId)
                .add(List.of(viewRealmRole));
    }

    private void addAdminRole(UserRepresentation serviceAccountUser) {
        RoleRepresentation adminRole = getAdminRole();

        realmResource.users()
                .get(serviceAccountUser.getId())
                .roles()
                .realmLevel()
                .add(List.of(adminRole));
    }

    private String getRealmMgmntId() {
        return realmResource.clients()
                .findByClientId("realm-management")
                .getFirst().getId();
    }

    private RoleRepresentation getManageRealmRole(String realMgmntId) {
        return realmResource
                .clients()
                .get(realMgmntId)
                .roles()
                .get("manage-realm")
                .toRepresentation();
    }

    private RoleRepresentation getManageUsersRole(String realMgmntId) {
        return realmResource
                .clients()
                .get(realMgmntId)
                .roles()
                .get("manage-users")
                .toRepresentation();
    }

    private RoleRepresentation getViewRealmRole(String realMgmntId) {
        return realmResource
                .clients()
                .get(realMgmntId)
                .roles()
                .get("view-realm")
                .toRepresentation();
    }

    private RoleRepresentation getAdminRole() {
        return realmResource.roles()
                .get("admin")
                .toRepresentation();
    }


}
