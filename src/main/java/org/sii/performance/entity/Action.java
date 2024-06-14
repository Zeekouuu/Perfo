package org.sii.performance.entity;
import lombok.*;
import javax.persistence.*;
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;
    private  String nomAction;
    private String commentaireCollab;
    private String commentaireManager;
    @ManyToOne
    @JoinColumn(name = "id_bilan")
    private Bilan bilan;
}
