package ru.yandex.practicum.filmorate.model;

public class ErrorResponse {
    private String message;

    // Конструктор
    public ErrorResponse(String message) {
        this.message = message;
    }

    // Геттер и сеттер
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}