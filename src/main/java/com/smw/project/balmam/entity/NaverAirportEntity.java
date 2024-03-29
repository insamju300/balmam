package com.smw.project.balmam.entity;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NaverAirportEntity {

    private Long id;
    private Timestamp regDate;
    private Timestamp updateDate;
    private String region;
    private String city;
    private String country;
    private String code;
    private Boolean isDeparture;
    private Integer version;
	    
}
