package ru.yandex.practicum.filmorate.model;

import org.springframework.validation.Errors;

public interface GenericValidator<T> {
    void validate(T target, Errors errors);  // Метод для валидации объектов типа T
}