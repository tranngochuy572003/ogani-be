package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity(name = "billDetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDetail extends BaseEntity{
    private String urlImg;
    private String nameProduct ;
    private Long price ;
    @ManyToOne
    @JoinColumn(name = "billId")
    private Bill bills;
}
