import client.BaseConfig;
import client.RequestConstructor;
import client.SoapClient;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Test;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoapTests {

    RequestConstructor constructor = new RequestConstructor();

    SoapClient client = new SoapClient();

    private static final BaseConfig config = ConfigFactory.create(BaseConfig.class, System.getenv());

    @Test
    void soapTest() throws SOAPException, IOException {
        Map<String, String> attr = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        headers.put("SOAPAction", config.namespaceURI() + "/" + config.serviceName());
//        headers.put("Host", "speller.yandex.net");
//        headers.put("Content-Length", "442");

        attr.put("lang", "ru");
        attr.put("options", "0");
        attr.put("format", "");
        SOAPMessage message = constructor
                .createSoapEnvelope(config.namespace(), config.namespaceURI())
                .addHeaders(headers)
                .createSoapBody()
                .createSoapElements(config.namespace(), "CheckTextRequest")
                .addAttributesToElement(attr, "CheckTextRequest")
                .addChildElement("CheckTextRequest", "text")
                .addTextToElement("text", "Смищно")
                .saveRequest()
                .getSoapMessage();

//        message.writeTo(System.out);

        client.call(message)
                .printResponse();
    }
}
