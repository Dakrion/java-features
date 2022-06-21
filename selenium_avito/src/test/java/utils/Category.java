package utils;

public enum Category {
    CONSUMABLES("Оргтехника и расходники"),
    PHONES("Телефоны"),
    AUTO("Автомобили");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}