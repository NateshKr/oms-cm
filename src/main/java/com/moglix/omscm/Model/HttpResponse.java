package com.moglix.omscm.Model;

public class HttpResponse {

	private int responseCode;
	private String content;
	private String responseMesage;

	public int getResponseCode() {
		return this.responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getResponseMesage() {
		return this.responseMesage;
	}

	public void setResponseMesage(String responseMesage) {
		this.responseMesage = responseMesage;
	}

	@Override
	public String toString() {
		return "HTTPResponse [responseCode=" + this.responseCode + ", content=" + this.content + ", responseMesage="
				+ this.responseMesage + "]";
	}
}
