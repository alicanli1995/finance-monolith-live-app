package com.bist.api.runner;

import com.bist.api.security.WebSecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class KeycloakInitializerRunner implements CommandLineRunner {

    private final Keycloak keycloakAdmin;

    @Override
    public void run(String... args) {
        log.info("Initializing '{}' realm in Keycloak ...", COMPANY_SERVICE_REALM_NAME);

        Optional<RealmRepresentation> representationOptional = keycloakAdmin.realms()
                .findAll()
                .stream()
                .filter(r -> r.getRealm().equals(COMPANY_SERVICE_REALM_NAME))
                .findAny();
        if (representationOptional.isPresent()) {
            log.info("Realm '{}' already exists, skipping initialization", COMPANY_SERVICE_REALM_NAME);
            return;
        }

        // Realm
        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setRealm(COMPANY_SERVICE_REALM_NAME);
        realmRepresentation.setEnabled(true);
        realmRepresentation.setRegistrationAllowed(true);

        // Client
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(FINANCE_APP_CLIENT_ID);
        clientRepresentation.setDirectAccessGrantsEnabled(true);
        clientRepresentation.setPublicClient(true);
        clientRepresentation.setRedirectUris(Collections.singletonList(FINANCE_APP_REDIRECT_URL));
        clientRepresentation.setDefaultRoles(new String[]{WebSecurityConfig.USER});
        realmRepresentation.setClients(Collections.singletonList(clientRepresentation));

        // Users
        List<UserRepresentation> userRepresentations = FINANCE_APP_USERS.stream()
                .map(userPass -> {
                    // User Credentials
                    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                    credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
                    credentialRepresentation.setValue(userPass.password());

                    // User
                    UserRepresentation userRepresentation = new UserRepresentation();
                    userRepresentation.setUsername(userPass.username());
                    userRepresentation.setEnabled(true);
                    userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
                    userRepresentation.setClientRoles(getClientRoles(userPass));

                    return userRepresentation;
                }).toList();
        realmRepresentation.setUsers(userRepresentations);

        // Create Realm
        keycloakAdmin.realms().create(realmRepresentation);

        // Testing
        UserPass admin = FINANCE_APP_USERS.get(0);
        log.info("Testing getting token for '{}' ...", admin.username());

        Keycloak keycloak = KeycloakBuilder.builder().serverUrl(KEYCLOAK_SERVER_URL)
                .realm(COMPANY_SERVICE_REALM_NAME).username(admin.username()).password(admin.password())
                .clientId(FINANCE_APP_CLIENT_ID).build();

        log.info("'{}' token: {}", admin.username(), keycloak.tokenManager().grantToken().getToken());
        log.info("'{}' initialization completed successfully!", COMPANY_SERVICE_REALM_NAME);
    }

    private Map<String, List<String>> getClientRoles(UserPass userPass) {
        List<String> roles = new ArrayList<>();
        roles.add(WebSecurityConfig.USER);
        if ("admin".equals(userPass.username())) {
            roles.add(WebSecurityConfig.FINANCE_MANAGER);
        }
        return Map.of(FINANCE_APP_CLIENT_ID, roles);
    }

    private static final String KEYCLOAK_SERVER_URL = "https://dene231.herokuapp.com/";
    private static final String COMPANY_SERVICE_REALM_NAME = "company-services";
    private static final String FINANCE_APP_CLIENT_ID = "finance-app";
    private static final String FINANCE_APP_REDIRECT_URL = "http://localhost:3000/*";
    private static final List<UserPass> FINANCE_APP_USERS = List.of(
            new UserPass("user", "user"));

    private record UserPass(String username, String password) {
    }
}