/*
* Copyright 2022 ALE International
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this 
* software and associated documentation files (the "Software"), to deal in the Software 
* without restriction, including without limitation the rights to use, copy, modify, merge, 
* publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons 
* to whom the Software is furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all copies or 
* substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
* BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.ale.o2g.sample.routing.util;

import java.util.HashMap;

/**
 *
 */
public class ConditionEnumConverter {

	static HashMap<String, String> enum2String = new HashMap<String, String>();
	static HashMap<String, String> string2Enum = new HashMap<String, String>();

	private static void setMapping(String sEnum, String display) {
		enum2String.put(sEnum, display);
		string2Enum.put(display, sEnum);
	}
		
	static {
		setMapping("IMMEDIATE", "Immediate");
		setMapping("BUSY", "On busy");
		setMapping("NO_ANSWER", "On no answer");
		setMapping("BUSY_NO_ANSWER", "On busy or no answer");
	}
	
	public static String getDisplayName(String value) {
		return enum2String.get(value);
	}

	public static String getEnumName(String value) {
		return string2Enum.get(value);
	}
}
