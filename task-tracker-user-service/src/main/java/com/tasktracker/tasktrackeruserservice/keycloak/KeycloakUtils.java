package com.tasktracker.tasktrackeruserservice.keycloak;


import com.tasktracker.tasktrackeruserservice.dto.UserCreateDto;
import com.tasktracker.tasktrackeruserservice.dto.UserUpdateDto;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class KeycloakUtils {
    @Value("${keycloak.auth-server-url}")
    private String serverUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private static Keycloak keycloak;
    private static RealmResource realmResource;
    private static UsersResource usersResource;

    @PostConstruct
    public Keycloak getInstance() {
        if(keycloak == null) {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .build();

            realmResource = keycloak.realm(realm);

            usersResource = realmResource.users();
        }
        return keycloak;
    }

    // создание пользователя для KC
    public Response createKeycloakUser(UserCreateDto userCreateDto) {


        CredentialRepresentation credentialRepresentation = createPasswordCredentials(userCreateDto.password());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(userCreateDto.username());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setEmail(userCreateDto.email());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        Response response = usersResource.create(kcUser);

        return response;

    }

    public void addRoles(String userId, List<String> roles) {

        List<RoleRepresentation> kcRoles = new ArrayList<>();

        for (String role: roles) {
            RoleRepresentation roleRep = realmResource.roles().get(role).toRepresentation();
            kcRoles.add(roleRep);
        }

        UserResource uniqueUserResource = usersResource.get(userId);

        uniqueUserResource.roles().realmLevel().add(kcRoles);

    }

    public void deleteUserById(String userId) {
        UserResource userResource = usersResource.get(userId);
        userResource.remove();
    }

    public UserRepresentation findUserById(String userId) {
        return usersResource.get(userId).toRepresentation();
    }

    public void updateKeycloakUser(UserUpdateDto userUpdateDto) {
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(userUpdateDto.password());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(userUpdateDto.username());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setEmail(userUpdateDto.email());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        UserResource userResource = usersResource.get(userUpdateDto.id());
        userResource.update(kcUser);
    }



    // данные о пароле
    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false); // не нужно будет менять пароль после первого входа
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
