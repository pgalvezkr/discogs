package com.clara.discog.repository;

import com.clara.discog.entity.Release;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReleaseRepository extends JpaRepository<Release, Long> {

    @Query("SELECT COUNT(r) FROM Release r WHERE r.artist.id = :artistId")
    int countReleasesByArtistId(@Param("artistId") Integer artistId);

    @Query("SELECT MIN(r.releaseYear) FROM Release r WHERE r.artist.id = :artistId")
    Integer findFirstReleaseYearByArtistId(@Param("artistId") Integer artistId);

    @Query("SELECT MAX(r.releaseYear) FROM Release r WHERE r.artist.id = :artistId")
    Integer findLastReleaseYearByArtistId(@Param("artistId") Integer artistId);

}
