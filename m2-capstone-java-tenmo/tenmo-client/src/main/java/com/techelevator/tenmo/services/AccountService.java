package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public class AccountService {

    private RestTemplate restTemplate = new RestTemplate();

    private String baseUrl = "";
    private AuthenticatedUser user;

    public AccountService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setUser(AuthenticatedUser user) {
        this.user = user;
    }

    public BigDecimal viewAccountBalance() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        BigDecimal balance = restTemplate.exchange(baseUrl + "username/balance", HttpMethod.GET, entity, BigDecimal.class).getBody();

        return balance;
    }

}
