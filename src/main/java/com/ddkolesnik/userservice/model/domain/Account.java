package com.ddkolesnik.userservice.model.domain;

import com.ddkolesnik.userservice.enums.OwnerType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Alexandr Stegnin
 */

@Data
@Entity
@Table(name = "account")
@ToString(of = {"id", "accountNumber"})
@EqualsAndHashCode(of = {"id", "accountNumber"})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "owner_id")
    private Long ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type")
    private OwnerType ownerType;

    @Column(name = "owner_name")
    private String ownerName;
}
