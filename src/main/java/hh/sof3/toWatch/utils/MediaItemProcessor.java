package hh.sof3.toWatch.utils;
import java.util.Arrays;

import org.springframework.batch.item.ItemProcessor;

import hh.sof3.toWatch.models.Media;

public class MediaItemProcessor implements ItemProcessor<Media, Media> {

    @Override
    public Media process(Media media) throws Exception {
        // Truncate the cast list to the first 3 members
        if (media.getCast() != null) {
            String[] castMembers = media.getCast().split(",");
            if (castMembers.length > 3) {
                // Join the first 3 cast members with a comma
                String truncatedCast = String.join(",", Arrays.copyOf(castMembers, 3));
                media.setCast(truncatedCast);
            }
        }
        if (media.getDirector() != null) {
            String[] directors = media.getDirector().split(",");
            if (directors.length > 2) {
                // Join the first 2 cast members with a comma
                String truncatedCast = String.join(",", Arrays.copyOf(directors, 2));
                media.setDirector(truncatedCast);
            }
        }
        return media;
    }
}