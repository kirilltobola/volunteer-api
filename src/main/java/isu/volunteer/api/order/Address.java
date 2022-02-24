package isu.volunteer.api.order;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class Address {
    @Column(name = "from_address")
    private String from;

    @Column(name = "to_address")
    private String to;
}
