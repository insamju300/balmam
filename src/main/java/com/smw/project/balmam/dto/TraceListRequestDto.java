package com.smw.project.balmam.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TraceListRequestDto {
	private Long lastItemTraceId;
	private Long lastItemOrderPoint;
	private Integer limit;
	private Integer tagId;
    private String cityName;
    
    public void setDefaultValues() {
    	if(tagId==null) {
    		tagId=-1;
    	}
    	
    	if(cityName==null) {
    		cityName="";
    	}
    }
}
