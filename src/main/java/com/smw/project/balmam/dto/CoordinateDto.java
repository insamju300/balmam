package com.smw.project.balmam.dto;

import java.math.BigDecimal;
import java.util.Objects;

import com.smw.project.balmam.entity.MediaFileEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CoordinateDto {

	public CoordinateDto(MediaFileEntity entity) {
		this.lat = entity.getExtra__lat();
		this.lng = entity.getExtra__lng();
	}
	
	private BigDecimal lat; // 위도
	private BigDecimal lng; // 경도
	
	
	//bicdecimal의 사이즈 관계 없이 비교하기 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordinateDto that = (CoordinateDto) o;
        return lat != null && lng != null && that.lat != null && that.lng != null
                && lat.compareTo(that.lat) == 0
                && lng.compareTo(that.lng) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat.stripTrailingZeros(), lng.stripTrailingZeros());
    }
}
