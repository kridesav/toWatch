package hh.sof3.toWatch.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import hh.sof3.toWatch.models.Movie;
import hh.sof3.toWatch.repositories.MovieRepository;
import hh.sof3.toWatch.repositories.TVShowRepository;

// http://localhost:8080/

@Controller
public class MediaController {
    @Autowired
    private MovieRepository mRepo;

    @Autowired
    private TVShowRepository tRepo;

    @RequestMapping(value ="/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = { "/", "/home" })
    public String MediaList(Model model) {
        model.addAttribute("movies", mRepo.findAll());
        model.addAttribute("tvshows", tRepo.findAll());
        return "home";
    }

    @RequestMapping(value = "/addMovie")
    public String addMovie(Model model) {
        model.addAttribute("movie", new Movie());
        return "addMovie";
    }

    @RequestMapping(value = "/saveMovie", method = RequestMethod.POST)
    public String save(Movie movie) {  
        mRepo.save(movie);
        return "redirect:/home";
    }

    @RequestMapping(value = "/editMovie/edit", method = RequestMethod.POST)
    public String saveEdit (Movie movie) {
        mRepo.save(movie);
        return "redirect:/home";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/deleteMovie/{id}", method = RequestMethod.GET)
    public String deleteMovie(@PathVariable("id") Long movieId, Model model) {
        mRepo.deleteById(movieId);
        return "redirect:/home";
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/editMovie/{id}", method = RequestMethod.GET)
    public String editMovie(@PathVariable("id") Long movieId, Model model) {
        Optional<Movie> movie = mRepo.findById(movieId);
        if (movie.isPresent()) {
            model.addAttribute("movie", movie.get());
            return "editMovie";
        } else {
            return "redirect:/home";
        }
    }

    // Hakee kaikki kirjat taulusta ja palauttaa ne koodilla 200
	@GetMapping("/Movies")
	public ResponseEntity<Iterable<Movie>> MovieListRest() {
		Iterable<Movie> movies = mRepo.findAll();
		return new ResponseEntity<Iterable<Movie>>(movies, HttpStatus.OK);
	}

	// Etsii annettulla ID:llä kirjaa, palauttaa löydetyn kirja ja koodin 200
	// tai tyhjän bodyn koodilla 404
	@GetMapping("/Movies/{id}")
	public ResponseEntity<Optional<Movie>> findMovieRest(@PathVariable("id") Long id) {
		Optional<Movie> movie = mRepo.findById(id);
		if (movie.isPresent()) {
			return new ResponseEntity<Optional<Movie>>(movie, HttpStatus.OK);
		} else {
			return new ResponseEntity<Optional<Movie>>(HttpStatus.NOT_FOUND);
		}
	}

}