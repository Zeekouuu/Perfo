package org.sii.performance.callBack.entities;

public record UtilisateurDTO(
        Integer id,
        String firstName,
        String lastName,
        String email,
        Role role,
        UtilisateurDTO manager,
        boolean firstTimeLogin ,
        Double unusedVacationDays
) {
}
