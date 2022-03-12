package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TransferService {

    private RestTemplate restTemplate = new RestTemplate();

    private String baseUrl = "";
    private AuthenticatedUser user;

    public TransferService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setUser(AuthenticatedUser user) {
        this.user = user;
    }

    public Transfer addNewTransfer(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<Transfer>(transfer, headers);

        Transfer response = restTemplate.exchange(baseUrl + "transfer", HttpMethod.POST, entity, Transfer.class).getBody();
        return response;
    }

    public List<Transfer> getAllTransfers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        Transfer[] transfers = restTemplate.exchange(baseUrl + "/account/" + user.getUser().getUsername() +"/transfer", HttpMethod.GET, entity, Transfer[].class).getBody();
        return Arrays.asList(transfers);
    }
}

