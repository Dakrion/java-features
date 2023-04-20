package database.extensions.logging;

import io.qameta.allure.attachment.AttachmentData;

/**
 * Класс для реализации объекта с аттачем
 */
public class SqlQueryAttachment implements AttachmentData {

    private final String value;

    private final String name;

    public SqlQueryAttachment(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
