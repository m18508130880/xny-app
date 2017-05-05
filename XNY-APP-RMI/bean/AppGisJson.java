package bean;

import java.util.ArrayList;
import java.util.List;

public class AppGisJson {

	private String Url;
	private String Rst;
	List<Object> CData = new ArrayList<Object>();
	
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public String getRst() {
		return Rst;
	}
	public void setRst(String rst) {
		Rst = rst;
	}
	public List<Object> getCData() {
		return CData;
	}
	public void setCData(List<Object> cData) {
		CData = cData;
	}
}
