package isu.volunteer.api.order;

import javax.persistence.Column;
import javax.persistence.Embeddable;


import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Address {
    // @Column(name = "from")
    private String from;

    // @Column(name = "to")
    private String to;
}
