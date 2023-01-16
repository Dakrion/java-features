package client;

import annotations.*;
import exceptions.AnnotationException;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Конструктор для создания конверта для SOAP
 */
public class RequestConstructor {

    private final ArrayList<SOAPElement> soapElements = new ArrayList<>();
    private SOAPEnvelope envelope;
    private SOAPBody soapBody;
    private SOAPMessage soapMessage;
    private MimeHeaders mimeHeaders;

    /**
     * Создает оболочку с конвертом
     * @param namespace название неймспейса
     * @param namespaceURI адрес неймспейса
     * @return this
     * @throws SOAPException
     */
    public RequestConstructor createSoapEnvelope(String namespace, String namespaceURI) throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        // SOAP Envelope
        envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(namespace, namespaceURI);

        return this;
    }

    /**
     * Создает оболочку Body в конверте
     * @return this
     * @throws SOAPException
     */
    public RequestConstructor createSoapBody() throws SOAPException {
        soapBody = envelope.getBody();

        return this;
    }

    /**
     * Добавляет хэдеры в soapMessage
     * @param headers Карта с хэдерами
     * @return this
     */
    public RequestConstructor addHeaders(Map<String, String> headers) {
        mimeHeaders = soapMessage.getMimeHeaders();

        headers
                .forEach((key, value) -> mimeHeaders.addHeader(key, value));

        return this;
    }

    /**
     * Добавляет хэдер в soapMessage
     * @param key название хэдера
     * @param value значение хэдера
     * @return this
     */
    public RequestConstructor addHeader(String key, String value) {
        mimeHeaders = soapMessage.getMimeHeaders();
        mimeHeaders.addHeader(key, value);

        return this;
    }

    /**
     * Создает soap-элементы в оболочке Body
     * @param namespace неймспейс элементов
     * @param localNames названия элементов
     * @return this
     * @throws SOAPException
     */
    public RequestConstructor createSoapElements(String namespace, String... localNames) throws SOAPException {
        for (String name : localNames) {
            soapElements.add(soapBody.addChildElement(name, namespace));
        }

        return this;
    }

    /**
     * Добавляет дочерний элемент к другому элементу
     * @param elementName наименование элемента-родителя
     * @param childElementName наименование дочернего элемента
     * @return this
     * @throws SOAPException
     */
    public RequestConstructor addChildElement(String elementName, String childElementName) throws SOAPException {
        soapElements.add(find(elementName).addChildElement(childElementName));

        return this;
    }

    /**
     * Добавляет аттрибуты к элементу
     * @param attributes карта с аттрибутами
     * @param elementName наименование элемента
     * @return this
     */
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

    /**
     * Добавляет значение в элемент
     * @param elementName наименование элемента
     * @param text значение в виде строки
     * @return this
     */
    public RequestConstructor addTextToElement(String elementName, String text) {
        try {
            find(elementName).addTextNode(text);
        } catch (SOAPException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    /**
     * Сохраняет все изменения в soapMessage
     * @return this
     * @throws SOAPException
     */
    public RequestConstructor saveRequest() throws SOAPException {
        soapMessage.saveChanges();

        return this;
    }

    /**
     * Возвращает soapMessage
     * @return {@link javax.xml.soap.SOAPMessage}
     */
    public SOAPMessage getSoapMessage() {
        return soapMessage;
    }

    /**
     * Создает готовое soapMessage из переданного объекта со специальными аннотациями
     * @param source объект
     * @param headers карта хэдеров
     * @return this
     * @throws SOAPException
     * @throws AnnotationException в случае если нет аннотации на объекте
     */
    public RequestConstructor fromObject(Object source, Map<String, String> headers) throws SOAPException {
        Class<?> sourceClass = source.getClass();

        if (sourceClass.isAnnotationPresent(EnvelopeProperties.class)) {
            String namespace = sourceClass.getAnnotation(EnvelopeProperties.class).namespace();
            String namespaceURI = sourceClass.getAnnotation(EnvelopeProperties.class).namespaceURI();

            createSoapEnvelope(namespace, namespaceURI);
            createSoapBody();
        } else throw new AnnotationException(sourceClass.getSimpleName() +
                " is not annotated with EnvelopeProperties.class!");

        if (sourceClass.isAnnotationPresent(SoapAction.class)) {
            String soapAction = sourceClass.getAnnotation(SoapAction.class).value();
            addHeader("SOAPAction", soapAction);
        }
        
        addHeaders(headers);

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

    /**
     * Перегрузка метода fromObject(Object source, Map<String, String> headers), если не используются хэдеры
     * @param source объект
     * @return this
     * @throws SOAPException
     * @throws AnnotationException в случае если нет аннотации на объекте
     */
    public RequestConstructor fromObject(Object source) throws SOAPException {
        Class<?> sourceClass = source.getClass();

        if (sourceClass.isAnnotationPresent(EnvelopeProperties.class)) {
            String namespace = sourceClass.getAnnotation(EnvelopeProperties.class).namespace();
            String namespaceURI = sourceClass.getAnnotation(EnvelopeProperties.class).namespaceURI();

            createSoapEnvelope(namespace, namespaceURI);
            createSoapBody();
        } else throw new AnnotationException(sourceClass.getSimpleName() +
                " is not annotated with EnvelopeProperties.class!");

        if (sourceClass.isAnnotationPresent(SoapAction.class)) {
            String soapAction = sourceClass.getAnnotation(SoapAction.class).value();
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

    /**
     * Находит элемент в списке и возвращает его
     * @param elementName наименование элемента
     * @return {@link javax.xml.soap.SOAPElement}
     */
    private SOAPElement find(String elementName) {
        for (SOAPElement el : soapElements) {
            if (el.getLocalName().equals(elementName)) return el;
        }
        throw new NullPointerException(elementName + " not find in this context!");
    }

    /**
     * Печать сообщения в консоль
     * @return this
     * @throws SOAPException
     * @throws IOException
     */
    public RequestConstructor printRequest() throws SOAPException, IOException {
        soapMessage.writeTo(System.out);
        return this;
    }
}
