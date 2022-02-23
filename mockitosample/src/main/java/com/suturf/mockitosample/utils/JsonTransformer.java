package com.suturf.mockitosample.utils;

import com.google.gson.Gson;

import spark.ResponseTransformer;

public class JsonTransformer {
	
	public static T fromJson()

	public static String toJson(Object object) {
		return new Gson().toJson(object);
	}
	
	public static ResponseTransformer jsonTransform() {
		return JsonTransformer::toJson;
	}
}
