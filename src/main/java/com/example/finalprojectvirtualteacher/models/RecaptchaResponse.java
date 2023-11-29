package com.example.finalprojectvirtualteacher.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RecaptchaResponse {
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("challenge")
    private String challenge;
    @JsonProperty("hostName")
    private String hostName;



    public RecaptchaResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public String getChallenge() {
        return challenge;
    }

    public String getHostName() {
        return hostName;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecaptchaResponse that = (RecaptchaResponse) o;
        return success == that.success &&
                Objects.equals(challenge, that.challenge) &&
                Objects.equals(hostName, that.hostName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, challenge, hostName);
    }
}


