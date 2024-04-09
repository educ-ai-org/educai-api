package api.educai.utils.email;

public class EmailPayload {
    private String service_id;
    private String template_id;
    private String user_id;
    private Object template_params;
    private String accessToken;

    public EmailPayload(String template_id, Object template_params) {
        this.service_id = "service_mxhydjk";
        this.template_id = template_id;
        this.user_id = "7PBnkqo-HahTr9LbL";
        this.template_params = template_params;
        this.accessToken = "IEhyrp-hXOwrVq2zZ0RmV";
    }

    public String getService_id() {
        return service_id;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public Object getTemplate_params() {
        return template_params;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
