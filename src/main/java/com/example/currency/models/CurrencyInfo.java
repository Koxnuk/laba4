package com.example.currency.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "currency_info")
@Data
public class CurrencyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer curId;

    @JsonProperty("Cur_Code")
    @jakarta.persistence.Column(name = "cur_code")
    private String curCode;

    @JsonProperty("Cur_Abbreviation")
    @jakarta.persistence.Column(name = "cur_abbreviation")
    private String curAbbreviation;

    @JsonProperty("Cur_Name")
    @jakarta.persistence.Column(name = "cur_name")
    private String curName;

    @JsonProperty("Cur_Scale")
    @jakarta.persistence.Column(name = "cur_scale")
    private Integer curScale;

    @OneToMany(mappedBy = "currency", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<CurrencyRate> rates = new ArrayList<>();
}