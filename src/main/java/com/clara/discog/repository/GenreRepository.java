package com.clara.discog.repository;

import com.clara.discog.dto.MostCriteria;
import com.clara.discog.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Query("select new com.clara.discog.dto.MostCriteria(a.name , count (g) , g.name) \n" +
            "from Genre g join Release r on (g.release.id = r.id) \n" +
            "join Artist a on (r.artist.id = a.id)\n" +
            "where a.id =:artistId \n" +
            "group by r.artist.id, a.name, g.name\n" +
            "order by count (g) desc   ")
    List<MostCriteria> findMostCommonGenresByArtistId(@Param("artistId") Integer artistId);

}
