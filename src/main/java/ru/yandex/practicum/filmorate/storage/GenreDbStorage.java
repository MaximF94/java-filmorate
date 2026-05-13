package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Component
public class GenreDbStorage implements GenreStorage {

    private final GenreRepository genreRepository;

    public GenreDbStorage(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Collection<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Optional<Genre> findById(Long id) {
        return genreRepository.findById(id);
    }
}
