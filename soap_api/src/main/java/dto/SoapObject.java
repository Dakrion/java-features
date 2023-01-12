package dto;

import annotations.*;

@EnvelopeProperties(namespace = "spell", namespaceURI = "http://speller.yandex.net/services/spellservice")
public class SoapObject {

    @SoapAction
    private String soapAction;

    @SoapElement(namespace = "http://speller.yandex.net/services/spellservice")
    @Attribute(name = "lang", value = "ru")
    @Attribute(name = "options", value = "0")
    @Attribute(name = "format", value = "")
    private static class CheckTextRequest {

        @ChildElement(parent = "CheckTextRequest")
        private String text;
    }
}
