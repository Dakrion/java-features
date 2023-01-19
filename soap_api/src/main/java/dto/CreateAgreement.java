package dto;

import annotations.ChildElement;
import annotations.EnvelopeProperties;
import annotations.SoapAction;
import annotations.SoapElement;

@SoapAction("http://siebel.com/CustomUI/createAgreement")
@EnvelopeProperties(namespace = "cus", namespaceURI = "http://siebel.com/CustomUI")
public class CreateAgreement {

    @SoapElement(namespace = "cus", name = "CreateAgreementRequest")
    private String createAgreementRequest;

    @ChildElement(parent = "CreateAgreementRequest")
    private String Environment_Code;

    @ChildElement(parent = "CreateAgreementRequest")
    private String TT_Ext_Code;

    @ChildElement(parent = "CreateAgreementRequest")
    private String Agent_Ext_Code;

    @ChildElement(parent = "CreateAgreementRequest")
    private String Chain_code;

    @ChildElement(parent = "CreateAgreementRequest")
    private String Opty_Id;

    @ChildElement(parent = "CreateAgreementRequest")
    private String Paper_Tech;

    @ChildElement(parent = "CreateAgreementRequest")
    private String Delivery_Date;

    public void setCreateAgreementRequest(String createAgreementRequest) {
        this.createAgreementRequest = createAgreementRequest;
    }

    public void setEnvironment_Code(String environment_Code) {
        Environment_Code = environment_Code;
    }

    public void setTT_Ext_Code(String TT_Ext_Code) {
        this.TT_Ext_Code = TT_Ext_Code;
    }

    public void setAgent_Ext_Code(String agent_Ext_Code) {
        Agent_Ext_Code = agent_Ext_Code;
    }

    public void setChain_code(String chain_code) {
        Chain_code = chain_code;
    }

    public void setOpty_Id(String opty_Id) {
        Opty_Id = opty_Id;
    }

    public void setPaper_Tech(String paper_Tech) {
        Paper_Tech = paper_Tech;
    }

    public void setDelivery_Date(String delivery_Date) {
        Delivery_Date = delivery_Date;
    }
}
