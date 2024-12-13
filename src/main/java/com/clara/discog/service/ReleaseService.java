package com.clara.discog.service;

import com.clara.discog.dto.ArtistDto;

public interface ReleaseService {
    String saveDiscography(ArtistDto artistDto) throws Exception;

}
