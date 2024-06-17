package com.javarush.telegram;

public enum DialogMode {
    MAIN("Главное меню бота"),
    PROFILE("Генерация Tinder-профиля \uD83D\uDE0E"),
    OPENER("Сообщение для знакомства \uD83E\uDD70"),
    MESSAGE("Переписка от вашего имени \uD83D\uDE08"),
    DATE("Переписка со звездами \uD83D\uDD25"),
    GPT("Задать вопрос чату GPT \uD83E\uDDE0");

    private final String title;

    DialogMode(String title) {
        this.title = title;
    }

    public String modeToLowerCase() {
        return name().toLowerCase();
    }

    public String getTitle() {
        return title;
    }
}
