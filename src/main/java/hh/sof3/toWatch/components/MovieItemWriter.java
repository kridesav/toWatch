package hh.sof3.toWatch.components;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import hh.sof3.toWatch.models.Media;

@Component
public class MovieItemWriter implements ItemWriter<Media> {
    private final JdbcTemplate jdbcTemplate;

    public MovieItemWriter(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void write(@NonNull Chunk<? extends Media> items) throws Exception {
        for (Media item : items) {
            jdbcTemplate.update(
                    "INSERT INTO movie (show_id, show_type, title, director, starring, country, date_added, release_year, rating, duration, listed_in, description) "
                            +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    item.getShowId(), item.getType(), item.getTitle(), item.getDirector(), item.getCast(),
                    item.getCountry(),
                    item.getDateAdded(), item.getReleaseYear(), item.getRating(), item.getDuration(),
                    item.getListedIn(), item.getDescription());
        }
    }
}
