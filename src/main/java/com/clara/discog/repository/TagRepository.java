package com.clara.discog.repository;

import com.clara.discog.dto.MostCriteria;
import com.clara.discog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("select new com.clara.discog.dto.MostCriteria(a.name, count (t), t.name) \n" +
            "from Tag t inner join Release r on (t.release.id = r.id) \n" +
            "join Artist a on (r.artist.id = a.id)\n" +
            "where a.id =:artistId \n" +
            "group by r.artist.id,  a.name, t.name\n" +
            "order by count (t) desc\n")
    List<MostCriteria> findMostCommonTagsByArtistId(@Param("artistId") Integer artistId);
}
