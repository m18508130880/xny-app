package bean;

import java.util.HashMap;

public class AppLoginJson {

	private String Url;
	private String UId;
	private String Rst;
	private String Token;
	private HashMap<String , Object> CData = new HashMap<String , Object>();
	
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public String getUId() {
		return UId;
	}
	public void setUId(String uId) {
		UId = uId;
	}
	public String getRst() {
		return Rst;
	}
	public void setRst(String rst) {
		Rst = rst;
	}
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	public HashMap<String, Object> getCData() {
		return CData;
	}
	public void setCData(HashMap<String, Object> cData) {
		CData = cData;
	}
}
