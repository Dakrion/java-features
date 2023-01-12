package client;

import org.aeonbits.owner.ConfigFactory;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;

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

    public SoapClient call(SOAPMessage request) throws SOAPException {
        factory = SOAPConnectionFactory.newInstance();
        connection = factory.createConnection();

        response = connection.call(request, soapUrl);

        return this;
    }

    public SOAPMessage getResponse() {
        return response;
    }

    public SoapClient printResponse() throws SOAPException, IOException {
        response.writeTo(System.out);

        return this;
    }
}
