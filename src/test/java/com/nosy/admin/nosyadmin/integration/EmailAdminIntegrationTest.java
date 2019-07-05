package com.nosy.admin.nosyadmin.integration;

import com.nosy.admin.nosyadmin.NosyAdminApplication;
import com.nosy.admin.nosyadmin.config.H2Config;
import com.nosy.admin.nosyadmin.model.InputSystem;
import com.nosy.admin.nosyadmin.model.User;
import com.nosy.admin.nosyadmin.repository.UserRepository;
import com.nosy.admin.nosyadmin.service.InputSystemService;
import com.nosy.admin.nosyadmin.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { NosyAdminApplication.class, H2Config.class })
public class EmailAdminIntegrationTest {

    @LocalServerPort
    private int port;
    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InputSystemService inputSystemService;

    @Autowired
    private UserService userService;

    @Test
    public void getInputSystems() throws Exception {
        userRepository.deleteAll();
        User user = new User();
        user.setEmail("test1@user.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password");
        InputSystem inputSystem = new InputSystem();
        inputSystem.setInputSystemId("InputSystemId");
        inputSystem.setInputSystemName("Test InputSystem");
        Set<InputSystem> inputSystems = new HashSet<>();
        inputSystems.add(inputSystem);
        user.setInputSystem(inputSystems);
//        EmailCollection emailCollection = new EmailCollection();
//        emailCollection.setEmailCollectionId("EmailCollectionId");
//        emailCollection.setEmailCollectionEmails(Arrays.asList("hej@mail.com", "hej2@mail.com"));
//        user.getEmailCollections().add(emailCollection);
        Optional<User> optUser = Optional.of(user);

//        userService.addUser(user);
        userRepository.save(user);
        inputSystemService.saveInputSystem(inputSystem, "test1@user.com");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/nosy/inputsystems"), HttpMethod.GET, entity, String.class);

//        when(userRepository.findById(anyString())).thenReturn(optUser);
//        when(inputSystemService.getListOfInputSystems(anyString())).thenReturn(optUser.get().getInputSystem());

        String expected = "{\"id\":\"InputSystemId\",\"name\":\"Test InputSystem\"}";

        System.out.println(response.getBody());
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
