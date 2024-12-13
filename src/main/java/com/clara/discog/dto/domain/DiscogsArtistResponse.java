package com.clara.discog.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.List;

@Data
public class DiscogsArtistResponse {

    @Data
    public static class Pagination {
        private int page;
        private int pages;
        @JsonProperty("per_page")
        private int perPage;
        private int items;
        private Urls urls;
    }

    public static class Urls {
    }

    @Data
    public static class Result {
        private int id;
        private String type;
        @JsonProperty("user_data")
        private UserData userData;
        @JsonProperty("master_id")
        private Object masterId;
        @JsonProperty("master_url")
        private Object masterUrl;
        private String uri;
        private String title;
        private String thumb;
        @JsonProperty("cover_image")
        private String coverImage;
        @JsonProperty("resource_url")
        private String resourceUrl;

    }

    @Data
    public static class UserData {
        @JsonProperty("in_wantlist")
        private boolean inWantlist;

        @JsonProperty("in_collection")
        private boolean inCollection;
    }
    private Pagination pagination;
    private List<Result> results;

    public static DiscogsArtistResponse fromJson(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, DiscogsArtistResponse.class);
    }

    public static String toJson(DiscogsArtistResponse response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(response);
    }
}