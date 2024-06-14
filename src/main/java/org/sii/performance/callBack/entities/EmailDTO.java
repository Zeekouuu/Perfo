package org.sii.performance.callBack.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailDTO {
    private Integer id_receiver;
    private String content;
    private Boolean withButton;
    private String url;
}
