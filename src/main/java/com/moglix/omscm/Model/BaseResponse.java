package com.moglix.omscm.Model;

import java.util.ArrayList;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
public class BaseResponse<T> {

	private Integer code;
	private String message;
	private Boolean success;
	private T data;
	
	public BaseResponse() {
		this.code = 400;
		this.message = "Something Went  Wrong";
		this.success = false;
		this.data = null;
	}
	
	public BaseResponse(String message, Boolean success, Integer code) {
		super();
		this.code = code;
		this.message = message;
		this.success = success;
	}
	
	public BaseResponse(T data) {
		this.data = data;
//		this(true,data);
	}
	
}
