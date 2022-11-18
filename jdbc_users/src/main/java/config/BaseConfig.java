package config;

import org.aeonbits.owner.Config;

/**
 * Интерфейс с параметрами основной конфигурации
 */
@Config.Sources({"classpath:${env}/database.properties"})
public interface BaseConfig extends Config {
    /**
     * Адрес базы данных
     *
     * @return String
     */
    String databaseUrl();

    /**
     * Пользователь базы данных
     *
     * @return String
     */
    String databaseUser();

    /**
     * Пароль базы данных
     *
     * @return String
     */
    String databasePassword();
}