package org.sii.performance.callBack.services;

import org.sii.performance.callBack.entities.EmailDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class EmailService {
    private final RestTemplate restTemplate;

    public EmailService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public void sendToManager(EmailDTO emailDTO,Integer id_sender ) {
        HttpHeaders headers = createHeader(id_sender);
        HttpEntity<EmailDTO> requestEntity = new HttpEntity<>(emailDTO, headers);
        restTemplate.postForObject("http://NOTIFICATION/api/notification/send/manager", requestEntity, Void.class);
    }



    public void sendToCollab(EmailDTO emailDTO,Integer id_sender ) {
        HttpHeaders headers = createHeader(id_sender);
        HttpEntity<EmailDTO> requestEntity = new HttpEntity<>(emailDTO, headers);
        restTemplate.postForObject("http://NOTIFICATION/api/notification/send/collab", requestEntity, Void.class);
    }

    private HttpHeaders createHeader(Integer id_user){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("id_user", String.valueOf(id_user));
        return headers;
    }

}
