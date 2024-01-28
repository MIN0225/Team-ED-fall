package com.hapjusil.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true) // 모르는 속성은 무시하고 가져온다.
public class CrawlerResultDto {
    private boolean success;
    private Long prId;
    private Long roomId;
    private List<String> data;


    @JsonProperty("success")
    public boolean isSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @JsonProperty("prId")
    public Long getPrId() {
        return prId;
    }

    @JsonProperty("prId")
    public void setPrId(Long prId) {
        this.prId = prId;
    }

    @JsonProperty("roomId")
    public Long getRoomId() {
        return roomId;
    }

    @JsonProperty("roomId")
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @JsonProperty("data")
    public List<String> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CrawlerResultDto{" +
                "success=" + success +
                ", prId='" + prId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", data=" + data +
                '}';
    }
}
