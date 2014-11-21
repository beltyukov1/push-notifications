package beltyukov.me.pushnotificationsspike;

public class User {

    private String token;
    private Integer oneAccountId;
    private String deviceType;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getOneAccountId() {
        return oneAccountId;
    }

    public void setOneAccountId(Integer oneAccountId) {
        this.oneAccountId = oneAccountId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
