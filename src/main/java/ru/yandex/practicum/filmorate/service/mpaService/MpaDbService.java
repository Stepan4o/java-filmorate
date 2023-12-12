package ru.yandex.practicum.filmorate.service.mpaService;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDao;

import java.util.List;

@Service
public class MpaDbService implements MpaServiceInterface {

    private final MpaDao mpaDao;

    public MpaDbService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    @Override
    public Mpa getMpaById(int id) {
        return mpaDao.getMpaById(id);
    }

    @Override
    public List<Mpa> getAll() {
        return mpaDao.getAll();
    }
}
