package com.techelevator.tenmo.services;

import com.techelevator.tenmo.auth.models.User;
import com.techelevator.tenmo.models.Account;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserService {

    private String API_BASE_URL = "";
    private String AUTH_TOKEN = "";
    private RestTemplate restTemplate = new RestTemplate();

    public UserService(String API_BASE_URL) {
        this.API_BASE_URL = API_BASE_URL;
    }

    public List<User> retrieveAllUsers() {


        User[] userArray =restTemplate.exchange(API_BASE_URL + "users/", HttpMethod.GET, this.makeAuthEntity(), User[].class).getBody();
        List<User> listOfUsers = Arrays.asList(userArray);

        return listOfUsers;
    }


    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

    public void setAUTH_TOKEN(String AUTH_TOKEN) {
        this.AUTH_TOKEN = AUTH_TOKEN;
    }

}
