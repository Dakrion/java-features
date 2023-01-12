package client;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.util.ArrayList;
import java.util.Map;

public class RequestConstructor {

    private final ArrayList<SOAPElement> soapElements = new ArrayList<>();
    private SOAPEnvelope envelope;
    private SOAPBody soapBody;
    private SOAPMessage soapMessage;
    private MimeHeaders mimeHeaders;

    public RequestConstructor createSoapEnvelope(String namespace, String namespaceURI) throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        // SOAP Envelope
        envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(namespace, namespaceURI);

        return this;
    }

    public RequestConstructor createSoapBody() throws SOAPException {
        soapBody = envelope.getBody();

        return this;
    }

    public RequestConstructor addHeaders(Map<String, String> headers) {
        mimeHeaders = soapMessage.getMimeHeaders();

        headers
                .forEach((key, value) -> mimeHeaders.addHeader(key, value));

        return this;
    }

    public RequestConstructor addHeader(String key, String value) {
        mimeHeaders = soapMessage.getMimeHeaders();
        mimeHeaders.addHeader(key, value);

        return this;
    }

    public RequestConstructor createSoapElements(String namespace, String... localNames) throws SOAPException {
        for (String name : localNames) {
            soapElements.add(soapBody.addChildElement(name, namespace));
        }

        return this;
    }

    public RequestConstructor addChildElement(String elementName, String childElementName) throws SOAPException {
        soapElements.add(find(elementName).addChildElement(childElementName));

        return this;
    }

    public RequestConstructor addAttributesToElement(Map<String, String> attributes, String elementName) {
        attributes
                .forEach((key, value) -> {
                    try {
                        find(elementName).addAttribute(new QName(key), value);
                    } catch (SOAPException e) {
                        throw new RuntimeException(e);
                    }
                });

        return this;
    }

    public RequestConstructor addTextToElement(String elementName, String text) {
        try {
            find(elementName).addTextNode(text);
        } catch (SOAPException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public RequestConstructor saveRequest() throws SOAPException {
        soapMessage.saveChanges();

        return this;
    }

    public SOAPMessage getSoapMessage() {
        return soapMessage;
    }

    // TODO: 12.01.2023 сделать метод создания конверта из объекта
//    public RequestConstructor fromObject(Object source) {
//
//    }

    private SOAPElement find(String elementName) {
        for (SOAPElement el : soapElements) {
            if (el.getLocalName().equals(elementName)) return el;
        }
        throw new NullPointerException(elementName + " not find in this context!");
    }
}
