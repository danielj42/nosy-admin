package com.nosy.admin.nosyadmin.controller;

import com.nosy.admin.nosyadmin.service.EmailCollectionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class EmailCollectionControllerTest {

    @InjectMocks
    EmailCollectionController emailCollectionController;

    @Mock
    EmailCollectionService emailCollectionService;

    @Test
    public void emailCollectionParse() {
        String file = "hey";
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.CREATED, emailCollectionController.
                uploadMultipart(file, "dasda", principal).getStatusCode());
    }

    @Test
    public void emailCollectionCreate() {
        List<String> emails = Collections.singletonList("dasda");
        Principal principal=mock(Principal.class);
        assertEquals(HttpStatus.CREATED, emailCollectionController.
                createGroup(emails, "dasda", principal).getStatusCode());
    }

    @Test
    public void getAllEmailCollections() {
        assertEquals(HttpStatus.OK, emailCollectionController.
                getAllEmailCollections().getStatusCode());
    }

    @Test
    public void getAllEmailCollectionsBySystemId() {
        assertEquals(HttpStatus.OK, emailCollectionController.
                getEmailCollectionById("emailCollectionId").getStatusCode());
    }

    @Test
    public void deleteEmailCollectionById() {
        assertEquals(HttpStatus.NO_CONTENT, emailCollectionController.
                deleteEmailCollectionById("emailCollectionId").getStatusCode());
    }
}