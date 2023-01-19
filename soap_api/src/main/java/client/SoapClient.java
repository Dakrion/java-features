package client;

import org.aeonbits.owner.ConfigFactory;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;

/**
 * SOAP-клиент для отправки сообщений
 */
@SuppressWarnings("ALL")
public class SoapClient {

    private static final BaseConfig config = ConfigFactory.create(BaseConfig.class, System.getenv());
    private final String namespaceURI;
    private final String soapUrl;
    private final String serviceName;
    private final String namespace;
    private final String soapAction;

    private SOAPConnectionFactory factory;

    private SOAPConnection connection;

    private SOAPMessage response;

    public SoapClient() {
        namespaceURI = config.namespaceURI();
        soapUrl = config.soapUrl();
        serviceName = config.serviceName();
        namespace = config.namespace();
        soapAction = namespaceURI + "/" + serviceName;
    }

    /**
     * Создает connection и отправляет сообщение
     * @param request
     * @return
     * @throws SOAPException
     */
    public SoapClient call(SOAPMessage request) throws SOAPException {
        factory = SOAPConnectionFactory.newInstance();
        connection = factory.createConnection();

        response = connection.call(request, soapUrl);

        return this;
    }

    /**
     * Возвращает ответ
     * @return {@link javax.xml.soap.SOAPMessage}
     */
    public SOAPMessage getResponse() {
        return response;
    }

    /**
     * Печать ответа в консоль
     * @return this
     * @throws SOAPException
     * @throws IOException
     */
    public SoapClient printResponse() throws SOAPException, IOException {
        response.writeTo(System.out);

        return this;
    }
}
