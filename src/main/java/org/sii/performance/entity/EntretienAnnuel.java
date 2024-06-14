package org.sii.performance.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntretienAnnuel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private Integer managerId;
    private LocalDate dateCreation;
    @Enumerated(EnumType.STRING)
    private StatutEntretienAnnuel status;
    @OneToMany(mappedBy = "entretienAnnuel")
    private List<Bilan> bilan;
}
