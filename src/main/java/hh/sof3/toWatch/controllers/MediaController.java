package hh.sof3.toWatch.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import hh.sof3.toWatch.models.Favourite;
import hh.sof3.toWatch.models.Media;
import hh.sof3.toWatch.models.Movie;
import hh.sof3.toWatch.models.TVShow;
import hh.sof3.toWatch.models.User;
import hh.sof3.toWatch.repositories.MovieRepository;
import hh.sof3.toWatch.repositories.TVShowRepository;
import jakarta.servlet.http.HttpServletRequest;

// http://localhost:8080/

@Controller
public class MediaController {
    @Autowired
    private MovieRepository mRepo;

    @Autowired
    private TVShowRepository tRepo;

    @Autowired
    private hh.sof3.toWatch.repositories.UserRepository uRepo;

    @Autowired
    private hh.sof3.toWatch.repositories.FavouriteRepository fRepo;

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping({ "/home", "/" })
    public String home(Model model) {
        Pageable topFive = PageRequest.of(0, 5);
        List<Movie> movies = mRepo.findMostFavoritedMovies(topFive);
        List<TVShow> tvShows = tRepo.findMostFavoritedTVShows(topFive);
        model.addAttribute("movies", movies);
        model.addAttribute("tvshows", tvShows);
        return "home";
    }

    @RequestMapping(value = "/movies")
    public String MovieList(Model model, Principal principal) {
        User user = uRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("movies", mRepo.findRandomMovies());
        return "movies";
    }

    @RequestMapping(value = "/tvshows")
    public String TVShowList(Model model, Principal principal) {
        User user = uRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("tvshows", tRepo.findRandomTvShows());
        return "tvshows";
    }

    @GetMapping("/favourites")
    public String showFavourites(Principal principal, Model model) {
        User user = uRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "favourites";
    }

    @PostMapping("/favourite/{type}/{id}")
    public String favourite(@PathVariable String type, @PathVariable Long id, Principal principal,
            HttpServletRequest request) {
        User user = uRepo.findByUsername(principal.getName());
        if (type.equals("movie")) {
            Movie movie = mRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + id));
            user.getFavourites().add(new Favourite(user, movie));
        } else if (type.equals("tvshow")) {
            TVShow tvShow = tRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid TV show Id:" + id));
            user.getFavourites().add(new Favourite(user, tvShow));
        }
        uRepo.save(user);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("/unfavourite/{type}/{id}")
    public String unfavourite(@PathVariable String type, @PathVariable Long id, Principal principal,
            HttpServletRequest request) {
        User user = uRepo.findByUsername(principal.getName());
        if (type.equals("movie")) {
            Movie movie = mRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + id));
            Favourite favourite = fRepo.findByUserAndMovie(user, movie);
            user.getFavourites().remove(favourite);
            fRepo.delete(favourite);
        } else if (type.equals("tvshow")) {
            TVShow tvshow = tRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid TV show Id:" + id));
            Favourite favourite = fRepo.findByUserAndTvshow(user, tvshow);
            user.getFavourites().remove(favourite);
            fRepo.delete(favourite);
        }
        uRepo.save(user);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/search")
    public String search(@RequestParam("q") String query, Model model, Principal principal) {
        List<Movie> movies = mRepo.findByTitleContainingIgnoreCase(query);
        List<TVShow> tvShows = tRepo.findByTitleContainingIgnoreCase(query);
        User user = uRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("movies", movies);
        model.addAttribute("tvshows", tvShows);
        return "search";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/addMedia")
    public String addMovie(Model model) {
        model.addAttribute("media", new Media());
        return "addMedia";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/saveMovie", method = RequestMethod.POST)
    public String save(Movie movie) {
        mRepo.save(movie);
        return "redirect:/home";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/edit/{type}/{id}")
    public String edit(@PathVariable String type, @PathVariable Long id, @ModelAttribute Media media, Model model, HttpServletRequest request) {
        if (type.equals("movie")) {
            Movie movie = mRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + id));
            movie.setTitle(media.getTitle());
            movie.setDescription(media.getDescription());
            mRepo.save(movie);
        } else if (type.equals("tvshow")) {
            TVShow tvshow = tRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid TV show Id:" + id));
            tvshow.setTitle(media.getTitle());
            tvshow.setDescription(media.getDescription());
            tRepo.save(tvshow);
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/delete/{type}/{id}")
    public String delete(@PathVariable String type, @PathVariable Long id, Model model, HttpServletRequest request) {
        if (type.equals("movie")) {
            Movie movie = mRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + id));
            mRepo.delete(movie);
        } else if (type.equals("tvshow")) {
            TVShow tvshow = tRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid TV show Id:" + id));
            tRepo.delete(tvshow);
        }
        return "redirect:" + request.getHeader("Referer");
    }

    // Hakee kaikki kirjat taulusta ja palauttaa ne koodilla 200
    @GetMapping("/api/movies")
    public ResponseEntity<Iterable<Movie>> MovieListRest() {
        Iterable<Movie> movies = mRepo.findAll();
        return new ResponseEntity<Iterable<Movie>>(movies, HttpStatus.OK);
    }

    // Etsii annettulla ID:llä kirjaa, palauttaa löydetyn kirja ja koodin 200
    // tai tyhjän bodyn koodilla 404
    @GetMapping("/api/Movies/{id}")
    public ResponseEntity<Optional<Movie>> findMovieRest(@PathVariable("id") Long id) {
        Optional<Movie> movie = mRepo.findById(id);
        if (movie.isPresent()) {
            return new ResponseEntity<Optional<Movie>>(movie, HttpStatus.OK);
        } else {
            return new ResponseEntity<Optional<Movie>>(HttpStatus.NOT_FOUND);
        }
    }

}