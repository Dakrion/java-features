package dto;

import annotations.*;

@SoapAction("http://speller.yandex.net/services/spellservice/checkText")
@EnvelopeProperties(namespace = "spell", namespaceURI = "http://speller.yandex.net/services/spellservice")
public class SoapObject {

    @SoapElement(namespace = "spell", name = "CheckTextRequest")
    @Attribute(name = "lang", value = "ru")
    @Attribute(name = "options", value = "0")
    @Attribute(name = "format", value = "")
    private Object checkTextRequest;

    @ChildElement(parent = "CheckTextRequest")
    private String text;

    public void setText(String text) {
        this.text = text;
    }
}
