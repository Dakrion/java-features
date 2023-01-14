package client;

import annotations.*;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestConstructor {

    private final ArrayList<SOAPElement> soapElements = new ArrayList<>();
    private SOAPEnvelope envelope;
    private SOAPBody soapBody;
    private SOAPMessage soapMessage;
    private MimeHeaders mimeHeaders;

    private String soapAction;

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

    public RequestConstructor fromObject(Object source) throws SOAPException {
        Class<?> sourceClass = source.getClass();

        if (sourceClass.isAnnotationPresent(EnvelopeProperties.class)) {
            String namespace = sourceClass.getAnnotation(EnvelopeProperties.class).namespace();
            String namespaceURI = sourceClass.getAnnotation(EnvelopeProperties.class).namespaceURI();

            createSoapEnvelope(namespace, namespaceURI);
            createSoapBody();
        } else throw new RuntimeException("No annotation");

        if (sourceClass.isAnnotationPresent(SoapAction.class)) {
            soapAction = sourceClass.getAnnotation(SoapAction.class).value();
            addHeader("SOAPAction", soapAction);
        }

        Field[] fields = sourceClass.getDeclaredFields();

        Arrays.stream(fields)
                .filter(f -> f.isAnnotationPresent(SoapElement.class))
                .forEach(f -> {
                    try {
                        f.setAccessible(true);
                        createSoapElements(f.getAnnotation(SoapElement.class)
                                .namespace(), f.getAnnotation(SoapElement.class).name());

                        if (f.isAnnotationPresent(AttributesContainer.class)) {
                            Map<String, String> attrContainer = new HashMap<>();

                            Arrays.stream(f.getAnnotationsByType(Attribute.class))
                                    .forEach(attr -> attrContainer.put(attr.name(), attr.value()));

                            addAttributesToElement(attrContainer, f.getAnnotation(SoapElement.class).name());
                        }
                    } catch (SOAPException e) {
                        throw new RuntimeException(e);
                    }
                });

        Arrays.stream(fields).filter(f -> f.isAnnotationPresent(ChildElement.class))
                .forEach(f -> {
                    try {
                        f.setAccessible(true);
                        if (f.getType() == String.class) {
                            addChildElement(f.getAnnotation(ChildElement.class).parent(), f.getName());
                            addTextToElement(f.getName(), (String) f.get(source));
                        }
                    } catch (SOAPException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });

        return this;
    }

    private SOAPElement find(String elementName) {
        for (SOAPElement el : soapElements) {
            if (el.getLocalName().equals(elementName)) return el;
        }
        throw new NullPointerException(elementName + " not find in this context!");
    }

    public RequestConstructor printRequest() throws SOAPException, IOException {
        soapMessage.writeTo(System.out);
        return this;
    }
}
