package com.example.finalprojectvirtualteacher.models.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class RateDto {
    private int id;
    @Min(value = 1, message = "Rate value must be at least 1")
    @Max(value = 5, message = "Rate value must be at most 5")
    private int rateValue;
    @Size(max = 75, message = "Comment should be up to 75 symbols.")
    private String comment;

    public RateDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRateValue() {
        return rateValue;
    }

    public void setRateValue(int rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
