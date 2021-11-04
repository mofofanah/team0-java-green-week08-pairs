package com.techelevator.tenmo.auth.services;

import com.techelevator.tenmo.models.Account;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private String API_BASE_URL = "";
    private String AUTH_TOKEN = "";
    private RestTemplate restTemplate = new RestTemplate();

    public AccountService(String API_BASE_URL) {
        this.API_BASE_URL = API_BASE_URL;
    }

    public Account retrieveAccountBalance(int userId) {
//        BigDecimal balance = null;
//
//        balance = restTemplate.exchange(API_BASE_URL + "accounts/", HttpMethod.GET, this.makeAuthEntity(), BigDecimal.class).getBody();

        return restTemplate.exchange(API_BASE_URL + "accounts/" + userId, HttpMethod.GET, this.makeAuthEntity(), Account.class).getBody();
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
