package com.intercom.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect
public class Customer {
    private String name;
    @JsonProperty("user_id")
    private Integer userId;
    private Double latitude;
    private Double longitude;
}
