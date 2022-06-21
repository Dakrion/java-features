package utils;

public enum Sort {
    DEFAULT("По умолчанию"),
    CHEAPER("Дешевле"),
    EXPENSIVE("Дороже"),
    DATA("По дате");

    private final String name;

    Sort(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}