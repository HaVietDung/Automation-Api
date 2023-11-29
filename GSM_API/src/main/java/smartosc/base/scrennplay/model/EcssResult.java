package smartosc.base.scrennplay.model;

public class EcssResult {
    private String IFRESULT;
    private String IFFAILMSG;

    public EcssResult(String ifresult, String iffailmsg) {
        IFRESULT = ifresult;
        IFFAILMSG = iffailmsg;
    }

    public String getIFFAILMSG() {
        return IFFAILMSG;
    }

    public void setIFFAILMSG(String IFFAILMSG) {
        this.IFFAILMSG = IFFAILMSG;
    }

    public String getIFRESULT() {
        return IFRESULT;
    }

    public void setIFRESULT(String IFRESULT) {
        this.IFRESULT = IFRESULT;
    }
}
