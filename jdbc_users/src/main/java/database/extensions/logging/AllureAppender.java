package database.extensions.logging;

import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;

/**
 * Класс с расширением для Allure, добавляет метод с прикреплением логов в отчет
 */
public class AllureAppender {

    private final AttachmentProcessor<AttachmentData> processor = new DefaultAttachmentProcessor();

    /**
     * Метод, прикрепляющий аттач к шагам в отчете
     * @param name имя аттача
     * @param value объект с информацией
     */
    public void log(String name, String value) {
        SqlQueryAttachment attachment = new SqlQueryAttachment(
                name,
                value);

        processor.addAttachment(attachment, new FreemarkerAttachmentRenderer("result.ftl"));
    }
}
