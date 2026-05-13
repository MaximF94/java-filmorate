package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Component
public class MpaDbStorage implements MpaStorage {

    private final MpaRepository mpaRepository;

    public MpaDbStorage(MpaRepository mpaRepository) {
        this.mpaRepository = mpaRepository;
    }

    @Override
    public Collection<Mpa> findAll() {
        return mpaRepository.findAll();
    }

    @Override
    public Optional<Mpa> findById(Long id) {
        return mpaRepository.findById(id);
    }
}
