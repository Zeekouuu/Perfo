package org.sii.performance.callBack.services;

import lombok.AllArgsConstructor;
import org.sii.performance.callBack.entities.UtilisateurDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class UserService {
    private final RestTemplate restTemplate;


    public UtilisateurDTO getUser(Integer id) {
        UtilisateurDTO utilisateurDTO = restTemplate.getForObject("http://AUTHENTIFICATION/auth/api/user/"+id, UtilisateurDTO.class);
        return utilisateurDTO;
    }
}
