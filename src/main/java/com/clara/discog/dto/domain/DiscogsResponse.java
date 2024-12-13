package com.clara.discog.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.List;

@Data
public class DiscogsResponse {

    @Data
    public static class Pagination {
        private int page;
        private int pages;
        @JsonProperty("per_page")
        private int perPage;
        private int items;
        private Urls urls;
    }

    @Data
    public static class Urls {
        private String last;
        private String next;
    }

    @Data
    public static class Result {
        private String country;
        private String year;
        private List<String> format;
        private List<String> label;
        private String type;
        private List<String> genre;
        private List<String> style;
        private int id;
        private List<String> barcode;
        @JsonProperty("user_data")
        private UserData userData;
        @JsonProperty("master_id")
        private int masterId;
        @JsonProperty("master_url")
        private String masterUrl;
        private String uri;
        private String catno;
        private String title;
        private String thumb;
        @JsonProperty("cover_image")
        private String coverImage;
        @JsonProperty("resource_url")
        private String resourceUrl;
        private Community community;
        @JsonProperty("format_quantity")
        private int formatQuantity;
        private List<Format> formats;

        @Data
        public static class UserData {
            @JsonProperty("in_wantlist")
            private boolean inWantlist;

            @JsonProperty("in_collection")
            private boolean inCollection;
        }

        @Data
        public static class Community {
            private int want;
            private int have;
        }

        @Data
        public static class Format {
            private String name;
            private String qty;
            private String text;
            private List<String> descriptions;
        }
    }
    private Pagination pagination;
    private List<Result> results;

    public static DiscogsResponse fromJson(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, DiscogsResponse.class);
    }

    public static String toJson(DiscogsResponse response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(response);
    }
}