package client;


import org.aeonbits.owner.Config;

/**
 * Интерфейс с параметрами основной конфигурации
 */
@Config.Sources({"classpath:service.properties"})
public interface BaseConfig extends Config {
    /**
     * URI неймспейса
     *
     * @return String
     */
    String namespaceURI();

    /**
     * URL
     *
     * @return String
     */
    String soapUrl();

    /**
     * Имя сервиса
     *
     * @return String
     */
    String serviceName();

    /**
     * Имя неймспейса
     *
     * @return String
     */
    String namespace();
}