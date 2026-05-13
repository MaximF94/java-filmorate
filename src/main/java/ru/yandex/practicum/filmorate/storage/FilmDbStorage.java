package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final FilmRepository filmRepository;

    public FilmDbStorage(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public Film save(Film film) {
        if (film.getId() == null) {
            return filmRepository.save(film);
        }
        return filmRepository.update(film);
    }

    @Override
    public boolean delete(Long id) {
        filmRepository.deleteFilm(id);
        return true;
    }

    @Override
    public Collection<Film> findAll() {
        return filmRepository.findAll();
    }

    @Override
    public Optional<Film> findById(Long id) {
        return filmRepository.findById(id);
    }

    @Override
    public void addLike(Film film, User user) {
        filmRepository.addLike(film, user);
    }

    @Override
    public void deleteLike(Film film, User user) {
        filmRepository.deleteLike(film, user);
    }

    @Override
    public Collection<Film> getTopFilmsByLikes(Integer count) {
        return filmRepository.getTopFilmsByLikes(count);
    }
}
