﻿package com.zym.mingqq.qqclient.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import com.zym.mingqq.Utils;
import com.zym.mingqq.qqclient.QQHttpClient;
import com.zym.mingqq.qqclient.protocol.protocoldata.*;

public class QQProtocol {
	
	public final static String WEBQQ_APP_ID = "1003903";
	public final static String WEBQQ_CLIENT_ID = "97518388";
	
	public static boolean checkVerifyCode(QQHttpClient httpClient, 
			String strQQNum, String strAppId, VerifyCodeInfo result) {
		
		try {
			String url = "https://ssl.ptlogin2.qq.com/check?";
			url += "uin=" + strQQNum + "&";
			url += "appid=" + strAppId + "&";
			url += "js_ver=10038&js_type=0&login_sig=P9011ArgB3ImUQV*BfKuftIu9o3lGeuIoBVBi*WjElOnOl2OzVIzg3gHjE0KJ9e3&u1=http%3A%2F%2Fweb2.qq.com%2Floginproxy.html&r=0.1790402590984438";
			
			boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_GET);
			if (!bRet) {
				return false;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "https://ui.ptlogin2.qq.com/cgi-bin/login?daid=164&target=self&style=5&mibao_css=m_webqq&appid=1003903&enable_qlogin=0&no_verifyimg=1&s_url=http%3A%2F%2Fweb2.qq.com%2Floginproxy.html&f_url=loginerroralert&strong_login=1&login_state=10&t=20130723001");  
			httpClient.addHeader("Accept-Language","zh-cn");  
	        
			httpClient.sendRequest();
			
			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
				
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return result.parse(bytRespData);
			
//			CookieStore httpCookies = (CookieStore)m_httpContext.getAttribute(ClientContext.COOKIE_STORE);
//			List<Cookie> respCookieList = httpCookies.getCookies();
//			for(Cookie ck : respCookieList)
//			{
//				System.out.println(ck);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		    
        
        return true;
	}
	
	// 获取验证码图片
	public static byte[] getVerifyCodePic(QQHttpClient httpClient,
			String strAppId, String strQQNum, String strVCType) {
		try {
			String url = "https://ssl.captcha.qq.com/getimage?";
			url += "aid=" + strAppId + "&";
			url += "r=0.43951176664325314" + "&";
			url += "uin=" + strQQNum;
//			url += "r=" + Math.random();

			boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_GET);
			if (!bRet) {
				return null;
			}

			httpClient.addHeader("Accept", "*/*");
			httpClient.addHeader(
					"Referer",
					"https://ui.ptlogin2.qq.com/cgi-bin/login?daid=164&target=self&style=5&mibao_css=m_webqq&appid=1003903&enable_qlogin=0&no_verifyimg=1&s_url=http%3A%2F%2Fweb2.qq.com%2Floginproxy.html&f_url=loginerroralert&strong_login=1&login_state=10&t=20130723001");
			httpClient.addHeader("Accept-Language", "zh-cn");

			httpClient.sendRequest();
			
			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return null;
			}

			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();

			return bytRespData;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static boolean login1(QQHttpClient httpClient, 
			String strQQNum, String strQQPwd, String strVerifyCode, 
			byte[] bytPtUin, String strAppId, LoginResult_1 result) {
		try {
			String strPwdHash = CalcPwdHash(strQQPwd, strVerifyCode, bytPtUin);
			
			String url = "https://ssl.ptlogin2.qq.com/login?";
			url += "u=" + strQQNum + "&";
			url += "p=" + strPwdHash + "&";
			url += "verifycode=" + strVerifyCode + "&";
			url += "aid=" + strAppId + "&";
 			url += "webqq_type=10&remember_uin=1&login2qq=1&" +
					"u1=http%3A%2F%2Fweb2.qq.com%2Floginproxy.html%3Flogin2qq%3D1%26webqq_type%3D10" +
					"&h=1&ptredirect=0&ptlang=2052&daid=164&from_ui=1&pttype=1" +
					"&dumy=&fp=loginerroralert&action=3-13-11750&mibao_css=m_webqq&" +
					"t=1&g=1&js_type=0&js_ver=10038&" +
					"login_sig=P9011ArgB3ImUQV*BfKuftIu9o3lGeuIoBVBi*WjElOnOl2OzVIzg3gHjE0KJ9e3";
			
 			System.out.println(url);
 			
 			boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_GET);
			if (!bRet) {
				return false;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "https://ui.ptlogin2.qq.com/cgi-bin/login?daid=164&target=self&style=5&mibao_css=m_webqq&appid=1003903&enable_qlogin=0&no_verifyimg=1&s_url=http%3A%2F%2Fweb2.qq.com%2Floginproxy.html&f_url=loginerroralert&strong_login=1&login_state=10&t=20130723001");  
			httpClient.addHeader("Accept-Language","zh-cn");  
	        
			httpClient.sendRequest();
			
			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
			
			List<Cookie> cookies = httpClient.getCookies();
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			if (!result.parse(bytRespData, cookies))
				return false;
			
			if (result.m_nRetCode == 0) {
				bRet = httpClient.openRequest(result.m_strCheckSigUrl, QQHttpClient.REQ_METHOD_GET);
				if (!bRet)
					return false;
				
				httpClient.addHeader("Accept", "*/*");  
				httpClient.addHeader("Referer", "https://ui.ptlogin2.qq.com/cgi-bin/login?daid=164&target=self&style=5&mibao_css=m_webqq&appid=1003903&enable_qlogin=0&no_verifyimg=1&s_url=http%3A%2F%2Fweb2.qq.com%2Floginproxy.html&f_url=loginerroralert&strong_login=1&login_state=10&t=20130723001");  
				httpClient.addHeader("Accept-Language","zh-cn");
				
				httpClient.sendRequest();
				
				nRespCode = httpClient.getRespCode();
				httpClient.closeRequest();
				if (nRespCode != 200)
					return false;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return false;
	}
	
	public static boolean login2(QQHttpClient httpClient, int nQQStatus, 
			String strPtWebQq, String strClientId, LoginResult_2 result) {
		try {
	        String url = "http://d.web2.qq.com/channel/login2";
	        
	        boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_POST);
			if (!bRet) {
				return false;
			}
		
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://d.web2.qq.com/proxy.html?v=20110331002&callback=2&id=3");  
			httpClient.addHeader("Accept-Language","zh-cn");  
			httpClient.addHeader("Content-Type","application/x-www-form-urlencoded");
      
			JSONObject json = new JSONObject();
			json.put("status", QQStatus.convertToQQStatusStr(nQQStatus));
			json.put("ptwebqq", strPtWebQq);  
			json.put("passwd_sig", "");
			json.put("clientid", strClientId);
			json.put("psessionid", JSONObject.NULL);
           
			NameValuePair pair1 = new BasicNameValuePair("r", json.toString());
			NameValuePair pair2 = new BasicNameValuePair("clientid", strClientId);
			NameValuePair pair3 = new BasicNameValuePair("psessionid", "null");
						
			List<NameValuePair> list = new ArrayList<NameValuePair>();  
			list.add(pair1);
			list.add(pair2);
			list.add(pair3);
	        
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			httpClient.setEntity(entity);
		
			httpClient.sendRequest();

			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
			
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			if (!result.parse(bytRespData))
				return false;
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
       return false;
	}

	// 注销
	public static boolean logout(QQHttpClient httpClient, 
			String strClientId, String strPSessionId, LogoutResult result) {
		try {
			long t = System.currentTimeMillis() / 1000;
			
			String url = "http://d.web2.qq.com/channel/logout2?";
			url += "ids=12916&";
			url += "clientid=" + strClientId + "&";
			url += "psessionid=" + strPSessionId + "&";
			url += "t=" + String.valueOf(t);

 			boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_GET);
			if (!bRet) {
				return false;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://d.web2.qq.com/proxy.html?v=20110331002&callback=2");  
			httpClient.addHeader("Accept-Language","zh-cn");  
	        	        
			httpClient.sendRequest();
			
			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
			
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return result.parse(bytRespData);						
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 获取好友列表
	public static boolean getBuddyList(QQHttpClient httpClient, 
			int nQQUin, String strPtWebQq, String strVfWebQq, BuddyListResult result) {
		
		try {
	        String url = "http://s.web2.qq.com/api/get_user_friends2";
	        
	        String strHash = calcBuddyListHash(nQQUin, strPtWebQq);
	        
	        boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_POST);
			if (!bRet) {
				return false;
			}
		
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://s.web2.qq.com/proxy.html?v=20110412001&callback=1&id=3");  
			httpClient.addHeader("Accept-Language","zh-cn");  
			httpClient.addHeader("Content-Type","application/x-www-form-urlencoded");
			
			JSONObject json = new JSONObject();
			json.put("h", "hello");
			json.put("hash", strHash);  
			json.put("vfwebqq", strVfWebQq);
           
			NameValuePair pair1 = new BasicNameValuePair("r", json.toString());
						
			List<NameValuePair> list = new ArrayList<NameValuePair>();  
			list.add(pair1);
	        
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			httpClient.setEntity(entity);
		
			httpClient.sendRequest();

			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
			
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return result.parse(bytRespData);
		} catch (Exception e) {
			e.printStackTrace();
		}
       return false;
	}
	
	// 获取在线好友列表
	public static boolean getOnlineBuddyList(QQHttpClient httpClient, 
			String strClientId, String strPSessionId, OnlineBuddyListResult result) {
		try {
			long t = System.currentTimeMillis() / 1000;

			String url = "http://d.web2.qq.com/channel/get_online_buddies2?";
			url += "clientid=" + strClientId + "&";
			url += "psessionid=" + strPSessionId + "&";
			url += "t=" + String.valueOf(t);

			boolean bRet = httpClient.openRequest(url,
					QQHttpClient.REQ_METHOD_GET);
			if (!bRet) {
				return false;
			}

			httpClient.addHeader("Accept", "*/*");
			httpClient.addHeader("Referer",
					"http://d.web2.qq.com/proxy.html?v=20110331002&callback=2");
			httpClient.addHeader("Accept-Language", "zh-cn");

			httpClient.sendRequest();

			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}

			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();

			return result.parse(bytRespData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
		// 获取群列表
	public static boolean getGroupList(QQHttpClient httpClient, 
			String strVfWebQq, GroupListResult result) {
		try {
	        String url = "http://s.web2.qq.com/api/get_group_name_list_mask2";
	        
	        boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_POST);
			if (!bRet) {
				return false;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://s.web2.qq.com/proxy.html?v=20110412001&callback=1&id=2");  
			httpClient.addHeader("Accept-Language","zh-cn");  
			httpClient.addHeader("Content-Type","application/x-www-form-urlencoded");
			
			JSONObject json = new JSONObject();
			json.put("vfwebqq", strVfWebQq);
           
			NameValuePair pair1 = new BasicNameValuePair("r", json.toString());
						
			List<NameValuePair> list = new ArrayList<NameValuePair>();  
			list.add(pair1);
	        
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			httpClient.setEntity(entity);
		
			httpClient.sendRequest();

			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
			
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return result.parse(bytRespData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 获取最近联系人列表
	public static boolean getRecentList(QQHttpClient httpClient, 
			String strVfWebQq, String strClientId, 
			String strPSessionId, RecentListResult result) {
		try {
	        String url = "http://d.web2.qq.com/channel/get_recent_list2";
	        
	        boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_POST);
			if (!bRet) {
				return false;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://d.web2.qq.com/proxy.html?v=20110331002&callback=2");  
			httpClient.addHeader("Accept-Language","zh-cn");  
			httpClient.addHeader("Content-Type","application/x-www-form-urlencoded");
			
			JSONObject json = new JSONObject();
			json.put("vfwebqq", strVfWebQq);
			json.put("clientid", strClientId);
			json.put("psessionid", strPSessionId);
           
			NameValuePair pair1 = new BasicNameValuePair("r", json.toString());
			NameValuePair pair2 = new BasicNameValuePair("clientid", strClientId);
			NameValuePair pair3 = new BasicNameValuePair("psessionid", strPSessionId);
						
			List<NameValuePair> list = new ArrayList<NameValuePair>();  
			list.add(pair1);
			list.add(pair2);
			list.add(pair3);
	        
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			httpClient.setEntity(entity);
		
			httpClient.sendRequest();

			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
			
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return result.parse(bytRespData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 轮询消息
	public static byte[] poll(QQHttpClient httpClient, 
			String strClientId,	String strPSessionId) {
		try {
	        String url = "http://d.web2.qq.com/channel/poll2";
	        
	        boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_POST);
			if (!bRet) {
				return null;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://d.web2.qq.com/proxy.html?v=20110331002&callback=2");  
			httpClient.addHeader("Accept-Language","zh-cn");  
			httpClient.addHeader("Content-Type","application/x-www-form-urlencoded");
			
			JSONObject json = new JSONObject();
			JSONArray json2 = new JSONArray();
			json.put("clientid", strClientId);
			json.put("psessionid", strPSessionId);
			json.put("key", 0);
			json.put("ids", json2);
           
			NameValuePair pair1 = new BasicNameValuePair("r", json.toString());
			NameValuePair pair2 = new BasicNameValuePair("clientid", strClientId);
			NameValuePair pair3 = new BasicNameValuePair("psessionid", strPSessionId);
						
			List<NameValuePair> list = new ArrayList<NameValuePair>();  
			list.add(pair1);
			list.add(pair2);
			list.add(pair3);
	        
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			httpClient.setEntity(entity);
		
			httpClient.sendRequest();

			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return null;
			}
			
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return bytRespData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 获取好友信息
	public static boolean getBuddyInfo(QQHttpClient httpClient, 
			int nQQUin, String strVfWebQq, BuddyInfoResult result) {
		try {
			long t = System.currentTimeMillis() / 1000;
			
			String url = "http://s.web2.qq.com/api/get_friend_info2?";
			url += "tuin=" + Utils.getUInt(nQQUin) + "&";
			url += "verifysession=&code=&";
			url += "vfwebqq=" + strVfWebQq + "&";
			url += "t=" + t;
			
			
			boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_GET);
			if (!bRet) {
				return false;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://s.web2.qq.com/proxy.html?v=20110412001&callback=1&id=2");  
			httpClient.addHeader("Accept-Language","zh-cn");  
	        
			httpClient.sendRequest();
			
			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
				
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return result.parse(bytRespData);			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return true;
	}
	
	// 获取陌生人信息
	public static boolean getStrangerInfo(QQHttpClient httpClient, 
			int nQQUin, String strVfWebQq, BuddyInfoResult result) {
		try {
			long t = System.currentTimeMillis() / 1000;
			
			String url = "http://s.web2.qq.com/api/get_stranger_info2?";
			url += "tuin=" + Utils.getUInt(nQQUin) + "&";
			url += "verifysession=&gid=0&code=&";
			url += "vfwebqq=" + strVfWebQq + "&";
			url += "t=" + t;
			
			
			boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_GET);
			if (!bRet) {
				return false;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://s.web2.qq.com/proxy.html?v=20110412001&callback=1&id=2");  
			httpClient.addHeader("Accept-Language","zh-cn");  
	        
			httpClient.sendRequest();
			
			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
				
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return result.parse(bytRespData);			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return true;
	}

	// 获取群信息
	public static boolean getGroupInfo(QQHttpClient httpClient, 
			int nGroupCode, String strVfWebQq, GroupInfoResult result) {
		try {
			long t = System.currentTimeMillis() / 1000;
			
			String url = "http://s.web2.qq.com/api/get_group_info_ext2?";
			url += "gcode=" + Utils.getUInt(nGroupCode) + "&";
			url += "vfwebqq=" + strVfWebQq + "&";
			url += "t=" + t;
			
			boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_GET);
			if (!bRet) {
				return false;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://s.web2.qq.com/proxy.html?v=20110412001&callback=1&id=2");  
			httpClient.addHeader("Accept-Language","zh-cn");  
	        
			httpClient.sendRequest();
			
			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
				
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return result.parse(bytRespData);			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return true;
	}

	// 获取好友、群成员或群号码
	public static boolean getQQNum(QQHttpClient httpClient, boolean bIsBuddy, 
			int nQQUin, String strVfWebQq, GetQQNumResult result) {
		try {
			int nType = bIsBuddy ? 1 : 4;
			long t = System.currentTimeMillis() / 1000;
			
			String url = "http://s.web2.qq.com/api/get_friend_uin2?";
			url += "tuin=" + Utils.getUInt(nQQUin) + "&";
			url += "verifysession=&";
			url += "type=" + nType + "&";
			url += "code=&";
			url += "vfwebqq=" + strVfWebQq + "&";
			url += "t=" + t;
			
			boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_GET);
			if (!bRet) {
				return false;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://s.web2.qq.com/proxy.html?v=20110412001&callback=1&id=2");  
			httpClient.addHeader("Accept-Language","zh-cn");  
	        
			httpClient.sendRequest();
			
			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
				
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return result.parse(bytRespData);			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return true;
	}
	
	// 获取QQ个性签名
	public static boolean getQQSign(QQHttpClient httpClient, 
			int nQQUin, String strVfWebQq, GetSignResult result) {
		try {
			long t = System.currentTimeMillis() / 1000;
			
			String url = "http://s.web2.qq.com/api/get_single_long_nick2?";
			url += "tuin=" + Utils.getUInt(nQQUin) + "&";
			url += "vfwebqq=" + strVfWebQq + "&";
			url += "t=" + t;
			
			boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_GET);
			if (!bRet) {
				return false;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://s.web2.qq.com/proxy.html?v=20110412001&callback=1&id=2");  
			httpClient.addHeader("Accept-Language","zh-cn");  
	        
			httpClient.sendRequest();
			
			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
				
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return result.parse(bytRespData);			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return true;
	}

	// 设置QQ个性签名
	public static boolean setQQSign(QQHttpClient httpClient, 
			String strSign, String strVfWebQq, SetSignResult result) {
		try {
	        String url = "http://s.web2.qq.com/api/set_long_nick2";
	        
	        boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_POST);
			if (!bRet) {
				return false;
			}
		
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://s.web2.qq.com/proxy.html?v=20110412001&callback=1&id=2");  
			httpClient.addHeader("Accept-Language","zh-cn");  
			httpClient.addHeader("Content-Type","application/x-www-form-urlencoded");
      
			JSONObject json = new JSONObject();
			json.put("nlk", Utils.unicodeToHexStr(strSign, false));
			json.put("vfwebqq", strVfWebQq); 
           
			NameValuePair pair1 = new BasicNameValuePair("r", json.toString());
					
			List<NameValuePair> list = new ArrayList<NameValuePair>();  
			list.add(pair1);
	        
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			httpClient.setEntity(entity);
		
			httpClient.sendRequest();

			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
			
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			if (!result.parse(bytRespData))
				return false;
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
       return false;
	}

	// 发送好友消息
	public static boolean sendBuddyMsg(QQHttpClient httpClient, 
			BuddyMessage buddyMsg,	String strClientId, 
			String strPSessionId, SendBuddyMsgResult result) {
		try {
		 	if (null == httpClient || null == buddyMsg || null == result 
		 			|| Utils.isEmptyStr(strClientId) || Utils.isEmptyStr(strPSessionId))
		 		return false;
		 
			String strContent = "[";
			
			for (int i = 0; i < buddyMsg.m_arrContent.size(); i++)
			{
				Content content = buddyMsg.m_arrContent.get(i);
				if (null == content)
					continue;
				
				if (content.m_nType == ContentType.CONTENT_TYPE_TEXT)
				{
					strContent += "\"";
					strContent += Utils.unicodeToHexStr(content.m_strText, false);
					strContent += "\",";
				}
				else if (content.m_nType == ContentType.CONTENT_TYPE_FONT_INFO)
				{
					String strFontName = Utils.unicodeToHexStr(content.m_FontInfo.m_strName, true);
					String strColor = Utils.RGBToHexStr(content.m_FontInfo.m_clrText);
					
					strContent += "[\"font\",{\"name\":\"";
					strContent += strFontName;
					strContent += "\",\"size\":\"";
					strContent += content.m_FontInfo.m_nSize;
					strContent += "\",\"style\":[";
					strContent += (content.m_FontInfo.m_bBold ? 1 : 0);
					strContent += ",";
					strContent += (content.m_FontInfo.m_bItalic ? 1 : 0);
					strContent += ",";
					strContent += (content.m_FontInfo.m_bUnderLine ? 1 : 0);
					strContent += "],\"color\":\"";
					strContent += strColor;
					strContent += "\"}],";
				}
				else if (content.m_nType == ContentType.CONTENT_TYPE_FACE)
				{
					strContent += "[\"face\",";
					strContent += content.m_nFaceId;
					strContent += "],";
				}
				else if (content.m_nType == ContentType.CONTENT_TYPE_CUSTOM_FACE)
				{
					strContent += "[\"cface\",\"";
					strContent += content.m_CFaceInfo.m_strRemoteFileName;
					strContent += "\"],";
				}
			}
			
			char c = strContent.charAt(strContent.length()-1);
			if (c == ',') {
				strContent = strContent.substring(0, strContent.length()-1);
			}
			
			strContent +=  "]";
			
	        String url = "http://d.web2.qq.com/channel/send_buddy_msg2";
	        
	        boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_POST);
			if (!bRet) {
				return false;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://d.web2.qq.com/proxy.html?v=20110331002&callback=2");  
			httpClient.addHeader("Accept-Language","zh-cn");  
			httpClient.addHeader("Content-Type","application/x-www-form-urlencoded");
			
			JSONObject json = new JSONObject();
			json.put("to", Utils.getUInt(buddyMsg.m_nToUin));
			json.put("face", 0);
			json.put("content", strContent);
			json.put("msg_id", Utils.getUInt(buddyMsg.m_nMsgId));
			json.put("clientid", strClientId);
			json.put("psessionid", strPSessionId);

			NameValuePair pair1 = new BasicNameValuePair("r", json.toString());
			NameValuePair pair2 = new BasicNameValuePair("clientid", strClientId);
			NameValuePair pair3 = new BasicNameValuePair("psessionid", strPSessionId);
						
			List<NameValuePair> list = new ArrayList<NameValuePair>();  
			list.add(pair1);
			list.add(pair2);
			list.add(pair3);
	        
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			httpClient.setEntity(entity);
		
			httpClient.sendRequest();

			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
			
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return result.parse(bytRespData);
		} catch (Exception e) {
			e.printStackTrace();
		}
       return false;
	}
	
	// 发送群消息
	public static boolean sendGroupMsg(QQHttpClient httpClient, 
			GroupMessage groupMsg,	String strClientId, String strPSessionId, 
			String strGFaceKey, String strGFaceSig, SendGroupMsgResult result) {
		try {
		 	if (null == httpClient || null == groupMsg || null == result 
		 			|| Utils.isEmptyStr(strClientId) || Utils.isEmptyStr(strPSessionId))
		 		return false;
		 
		 	boolean bHasCustomFace = false;
			String strContent = "[";
			
			for (int i = 0; i < groupMsg.m_arrContent.size(); i++)
			{
				Content content = groupMsg.m_arrContent.get(i);
				if (null == content)
					continue;
				
				if (content.m_nType == ContentType.CONTENT_TYPE_TEXT)
				{
					strContent += "\"";
					strContent += Utils.unicodeToHexStr(content.m_strText, false);
					strContent += "\",";
				}
				else if (content.m_nType == ContentType.CONTENT_TYPE_FONT_INFO)
				{
					String strFontName = Utils.unicodeToHexStr(content.m_FontInfo.m_strName, true);
					String strColor = Utils.RGBToHexStr(content.m_FontInfo.m_clrText);
					
					strContent += "[\"font\",{\"name\":\"";
					strContent += strFontName;
					strContent += "\",\"size\":\"";
					strContent += content.m_FontInfo.m_nSize;
					strContent += "\",\"style\":[";
					strContent += (content.m_FontInfo.m_bBold ? 1 : 0);
					strContent += ",";
					strContent += (content.m_FontInfo.m_bItalic ? 1 : 0);
					strContent += ",";
					strContent += (content.m_FontInfo.m_bUnderLine ? 1 : 0);
					strContent += "],\"color\":\"";
					strContent += strColor;
					strContent += "\"}],";
				}
				else if (content.m_nType == ContentType.CONTENT_TYPE_FACE)
				{
					strContent += "[\"face\",";
					strContent += content.m_nFaceId;
					strContent += "],";
				}
				else if (content.m_nType == ContentType.CONTENT_TYPE_CUSTOM_FACE)
				{
					bHasCustomFace = true;
					strContent += "[\"cface\",\"group\",\"";
					strContent += content.m_CFaceInfo.m_strRemoteFileName;
					strContent += "\"],";
				}
			}
			
			char c = strContent.charAt(strContent.length()-1);
			if (c == ',') {
				strContent = strContent.substring(0, strContent.length()-1);
			}
					
			strContent +=  "]";
			
	        String url = "http://d.web2.qq.com/channel/send_qun_msg2";
	        
	        boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_POST);
			if (!bRet) {
				return false;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://d.web2.qq.com/proxy.html?v=20110331002&callback=2");  
			httpClient.addHeader("Accept-Language","zh-cn");  
			httpClient.addHeader("Content-Type","application/x-www-form-urlencoded");
			
			JSONObject json = new JSONObject();
			json.put("group_uin", Utils.getUInt(groupMsg.m_nToUin));
			
			if (bHasCustomFace) {
				json.put("group_code", Utils.getUInt(groupMsg.m_nGroupCode));
				json.put("key", strGFaceKey);
				json.put("sig", strGFaceSig);
			}
			
			json.put("content", strContent);
			json.put("msg_id", Utils.getUInt(groupMsg.m_nMsgId));
			json.put("clientid", strClientId);
			json.put("psessionid", strPSessionId);
			
			NameValuePair pair1 = new BasicNameValuePair("r", json.toString());
			NameValuePair pair2 = new BasicNameValuePair("clientid", strClientId);
			NameValuePair pair3 = new BasicNameValuePair("psessionid", strPSessionId);
						
			List<NameValuePair> list = new ArrayList<NameValuePair>();  
			list.add(pair1);
			list.add(pair2);
			list.add(pair3);
	        
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			httpClient.setEntity(entity);
		
			httpClient.sendRequest();

			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
			
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return result.parse(bytRespData);
		} catch (Exception e) {
			e.printStackTrace();
		}
       return false;
	}

	// 发送临时会话消息
	public static boolean sendSessMsg(QQHttpClient httpClient, 
			SessMessage sessMsg, String strGroupSig, String strClientId, 
			String strPSessionId, SendSessMsgResult result) {
		try {
		 	if (null == httpClient || null == sessMsg || null == result 
		 			|| Utils.isEmptyStr(strClientId) || Utils.isEmptyStr(strPSessionId))
		 		return false;
		 
			String strContent = "[";
			
			for (int i = 0; i < sessMsg.m_arrContent.size(); i++)
			{
				Content content = sessMsg.m_arrContent.get(i);
				if (null == content)
					continue;
				
				if (content.m_nType == ContentType.CONTENT_TYPE_TEXT)
				{
					strContent += "\"";
					strContent += Utils.unicodeToHexStr(content.m_strText, false);
					strContent += "\",";
				}
				else if (content.m_nType == ContentType.CONTENT_TYPE_FONT_INFO)
				{
					String strFontName = Utils.unicodeToHexStr(content.m_FontInfo.m_strName, true);
					String strColor = Utils.RGBToHexStr(content.m_FontInfo.m_clrText);
					
					strContent += "[\"font\",{\"name\":\"";
					strContent += strFontName;
					strContent += "\",\"size\":\"";
					strContent += content.m_FontInfo.m_nSize;
					strContent += "\",\"style\":[";
					strContent += (content.m_FontInfo.m_bBold ? 1 : 0);
					strContent += ",";
					strContent += (content.m_FontInfo.m_bItalic ? 1 : 0);
					strContent += ",";
					strContent += (content.m_FontInfo.m_bUnderLine ? 1 : 0);
					strContent += "],\"color\":\"";
					strContent += strColor;
					strContent += "\"}],";
				}
				else if (content.m_nType == ContentType.CONTENT_TYPE_FACE)
				{
					strContent += "[\"face\",";
					strContent += content.m_nFaceId;
					strContent += "],";
				}
			}
			
			char c = strContent.charAt(strContent.length()-1);
			if (c == ',') {
				strContent = strContent.substring(0, strContent.length()-1);
			}
			
			strContent +=  "]";
			
	        String url = "http://d.web2.qq.com/channel/send_sess_msg2";
	        
	        boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_POST);
			if (!bRet) {
				return false;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://d.web2.qq.com/proxy.html?v=20110331002&callback=2");  
			httpClient.addHeader("Accept-Language","zh-cn");  
			httpClient.addHeader("Content-Type","application/x-www-form-urlencoded");
			
			JSONObject json = new JSONObject();
			json.put("to", Utils.getUInt(sessMsg.m_nToUin));
			json.put("group_sig", strGroupSig);
			json.put("face", 0);
			json.put("content", strContent);
			json.put("msg_id", Utils.getUInt(sessMsg.m_nMsgId));
			json.put("service_type", 0);
			json.put("clientid", strClientId);
			json.put("psessionid", strPSessionId);
			
			NameValuePair pair1 = new BasicNameValuePair("r", json.toString());
			NameValuePair pair2 = new BasicNameValuePair("clientid", strClientId);
			NameValuePair pair3 = new BasicNameValuePair("psessionid", strPSessionId);
						
			List<NameValuePair> list = new ArrayList<NameValuePair>();  
			list.add(pair1);
			list.add(pair2);
			list.add(pair3);
	        
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
			httpClient.setEntity(entity);
		
			httpClient.sendRequest();

			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
			
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return result.parse(bytRespData);
		} catch (Exception e) {
			e.printStackTrace();
		}
       return false;
	}
	
	// 获取头像图片
	public static byte[] getHeadPic(QQHttpClient httpClient, 
			boolean bIsBuddy, int nQQUin, String strVfWebQq) {
		try {
			java.util.Random random = new java.util.Random();
			int r = random.nextInt(10);
			
			int nType = bIsBuddy ? 11 : 14;
			
			long t = System.currentTimeMillis() / 1000;
			
			String url = "http://face" + r + ".web.qq.com/cgi/svr/face/getface?";
			url += "cache=0&";
			url += "type=" + nType + "&";
			url += "fid=0&";
			url += "uin=" + Utils.getUInt(nQQUin) + "&";
			url += "vfwebqq=" + strVfWebQq + "&";
			url += "t=" + t;
			
			boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_GET);
			if (!bRet) {
				return null;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://web2.qq.com/webqq.html");  
			httpClient.addHeader("Accept-Language","zh-cn");  
	        
			httpClient.sendRequest();
			
			int nRespCode = httpClient.getRespCode();
			if (!(nRespCode >= 200 && nRespCode < 300)) {
				httpClient.closeRequest();
				return null;
			}
				
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return bytRespData;			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return null;
	}

	// 获取好友聊天图片
	public static byte[] getBuddyChatPic(QQHttpClient httpClient, 
			int nMsgId, String strFileName, int nQQUin, 
			String strClientId, String strPSessionId) {
		try {
			String url = "http://d.web2.qq.com/channel/get_cface2?";
			url += "lcid=" + Utils.getUInt(nMsgId) + "&";
			url += "guid=" + strFileName + "&";
			url += "to=" + Utils.getUInt(nQQUin) + "&";
			url += "count=5&time=1&";
			url += "clientid=" + strClientId + "&";
			url += "psessionid=" + strPSessionId;
			
			boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_GET);
			if (!bRet) {
				return null;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://web.qq.com/?ADTAG=DESKTOP");  
			httpClient.addHeader("Accept-Language","zh-cn");  
	        
			httpClient.sendRequest();
			
			int nRespCode = httpClient.getRespCode();
			if (!(nRespCode >= 200 && nRespCode < 300)) {
				httpClient.closeRequest();
				return null;
			}
				
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return bytRespData;
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return null;
	}

	// 获取好友离线聊天图片
	public static byte[] getBuddyOffChatPic(QQHttpClient httpClient, 
			String strFileName, int nQQUin, String strClientId, String strPSessionId) {
		try {
			String url = "http://d.web2.qq.com/channel/get_offpic2?";
			url += "file_path=" + strFileName + "&";
			url += "f_uin=" + Utils.getUInt(nQQUin) + "&";
			url += "clientid=" + strClientId + "&";
			url += "psessionid=" + strPSessionId;
			
			boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_GET);
			if (!bRet) {
				return null;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://web.qq.com/?ADTAG=DESKTOP");  
			httpClient.addHeader("Accept-Language","zh-cn");
			httpClient.addHeader("Accept-Encoding","gzip, deflate");
	        
			httpClient.sendRequest();
			
			int nRespCode = httpClient.getRespCode();
			if (!(nRespCode >= 200 && nRespCode < 300)) {
				httpClient.closeRequest();
				return null;
			}
				
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return bytRespData;
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return null;
	}

	// 获取群聊天图片
	public static byte[] getGroupChatPic(QQHttpClient httpClient, 
			int nGroupId, int nQQUin, String strServer, 
			int nPort, int nFileId,  String strFileName, String strVfWebQq) {
		try {
			long t = System.currentTimeMillis() / 1000;
			
			String url = "http://web.qq.com/cgi-bin/get_group_pic?";
			url += "type=0&";
			url += "gid=" + Utils.getUInt(nGroupId) + "&";
			url += "uin=" + Utils.getUInt(nQQUin) + "&";
			url += "rip=" + strServer + "&";
			url += "rport=" + nPort + "&";
			url += "fid=" + Utils.getUInt(nFileId) + "&";
			url += "pic=" + URLEncoder.encode(strFileName, "UTF-8") + "&";
			url += "vfwebqq=" + strVfWebQq + "&";
			url += "t=" + t;
			
			boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_GET);
			if (!bRet) {
				return null;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://web.qq.com/?ADTAG=DESKTOP");  
			httpClient.addHeader("Accept-Language","zh-cn");
			httpClient.addHeader("Accept-Encoding","gzip, deflate");
	        
			httpClient.sendRequest();
			
			int nRespCode = httpClient.getRespCode();
			if (!(nRespCode >= 200 && nRespCode < 300)) {
				httpClient.closeRequest();
				return null;
			}
				
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return bytRespData;
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return null;		
	}

	// 改变状态
	public static boolean changeStatus(QQHttpClient httpClient, 
			int nStatus, String strClientId, 
			String strPSessionId, ChangeStatusResult result) {
		try {
			long t = System.currentTimeMillis() / 1000;
			
			String url = "http://d.web2.qq.com/channel/change_status2?";
			url += "newstatus=" + QQStatus.convertToQQStatusStr(nStatus) + "&";
			url += "clientid=" + strClientId + "&";
			url += "psessionid=" + strPSessionId + "&";
			url += "t=" + t;
			
			boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_GET);
			if (!bRet) {
				return false;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://d.web2.qq.com/proxy.html?v=20110331002&callback=2");  
			httpClient.addHeader("Accept-Language","zh-cn");  
	        
			httpClient.sendRequest();
			
			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
				
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return result.parse(bytRespData);			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return true;
	}

	// 获取临时会话信令
	public static boolean getC2CMsgSignal(QQHttpClient httpClient, 
			int nGroupId, int nToUin, String strClientId, 
			String strPSessionId, GetC2CMsgSigResult result) {
		try {
			long t = System.currentTimeMillis() / 1000;
			
			String url = "http://d.web2.qq.com/channel/get_c2cmsg_sig2?";
			url += "id=" + Utils.getUInt(nGroupId) + "&";
			url += "to_uin=" + Utils.getUInt(nToUin) + "&";
			url += "service_type=0&";
			url += "clientid=" + strClientId + "&";
			url += "psessionid=" + strPSessionId + "&";
			url += "t=" + t;
			
			boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_GET);
			if (!bRet) {
				return false;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://d.web2.qq.com/proxy.html?v=20110331002&callback=2");  
			httpClient.addHeader("Accept-Language","zh-cn");  
	        
			httpClient.sendRequest();
			
			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
				
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return result.parse(bytRespData);			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return true;
	}

	// 获取群表情信令
	public static boolean getGroupFaceSignal(QQHttpClient httpClient, 
			String strClientId, String strPSessionId, GetGroupFaceSigResult result) {
		try {
			long t = System.currentTimeMillis() / 1000;
			
			String url = "http://d.web2.qq.com/channel/get_gface_sig2?";
			url += "clientid=" + strClientId + "&";
			url += "psessionid=" + strPSessionId + "&";
			url += "t=" + t;
			
			boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_GET);
			if (!bRet) {
				return false;
			}
			
			httpClient.addHeader("Accept", "*/*");  
			httpClient.addHeader("Referer", "http://d.web2.qq.com/proxy.html?v=20110331002&callback=2");  
			httpClient.addHeader("Accept-Language","zh-cn");  
	        
			httpClient.sendRequest();
			
			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
				
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			return result.parse(bytRespData);			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return true;
	}

	// 上传自定义表情
	public static boolean uploadCustomFace(QQHttpClient httpClient, 
			String strFileName, String strVfWebQq, UploadCustomFaceResult result) {
		try {
	        String url = "http://up.web2.qq.com/cgi-bin/cface_upload";
	        
	        boolean bRet = httpClient.openRequest(url, QQHttpClient.REQ_METHOD_POST);
			if (!bRet) {
				return false;
			}
		
			httpClient.addHeader("Accept", "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/x-ms-application, application/x-ms-xbap, application/vnd.ms-xpsdocument, application/xaml+xml, */*");  
			httpClient.addHeader("Referer", "http://web2.qq.com/webqq.html");  
			httpClient.addHeader("Accept-Language","zh-cn");  
			httpClient.addHeader("Accept-Encoding","gzip, deflate");
			httpClient.addHeader("Connection","keep-alive");
			httpClient.addHeader("Cache-Control","no-cache");
			
			//String strMimeType = GetMimeTypeByExtension(strFileName);
			
			File file = new File(strFileName);
			
			MultipartEntity mpEntity = new MultipartEntity();
			
			ContentBody cbFile = new FileBody(file);
			mpEntity.addPart("custom_face", cbFile);
			
			ContentBody cbFile2 = new StringBody("EQQ.View.ChatBox.uploadCustomFaceCallback", Charset.forName(HTTP.UTF_8));
			mpEntity.addPart("f", cbFile2);
			
			ContentBody cbFile3 = new StringBody(strVfWebQq, Charset.forName(HTTP.UTF_8));
			mpEntity.addPart("vfwebqq", cbFile3);
			
			httpClient.setEntity(mpEntity);
			//System.out.println("executing request " + );
		
			httpClient.sendRequest();

			int nRespCode = httpClient.getRespCode();
			if (nRespCode != 200) {
				httpClient.closeRequest();
				return false;
			}
			
			byte[] bytRespData = httpClient.getRespBodyData();
			httpClient.closeRequest();
			
			if (!result.parse(bytRespData))
				return false;
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
       return false;
	}

	// 计算第一次登录的密码hash参数
	private static String CalcPwdHash(String strQQPwd, String strVerifyCode, byte[] bytPtUin)
	{
		// UPPER(HEX(MD5(UPPER(HEX(MD5(MD5(密码) + PtUin))) + UPPER(验证码))))

		String strHex = null;
		
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
	        md5.update(strQQPwd.getBytes("UTF-8"));
	        byte[] bytPwdMd5 = md5.digest();
	        
	        byte[] bytTemp = Utils.byteMerger(bytPwdMd5, bytPtUin);
	        
	        md5.reset();
	        md5.update(bytTemp);
	        bytTemp = md5.digest();
			strHex = toHexStr(bytTemp);
			
			strHex = strHex.toUpperCase();
			strVerifyCode = strVerifyCode.toUpperCase();
			
			bytTemp = strHex.getBytes("UTF-8");
			byte[] bytTemp2 = strVerifyCode.getBytes("UTF-8");
			byte[] bytTemp3 = Utils.byteMerger(bytTemp, bytTemp2);
	        
			md5.reset();
			md5.update(bytTemp3);
			bytTemp3 = md5.digest();

			strHex = toHexStr(bytTemp3);
			strHex = strHex.toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();  
		} 
		
		return strHex;
	}
	
	private static String toHexStr(byte[] bytData) {
		byte[] bytHexTable = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		byte[] bytHex = new byte[bytData.length*2];
		
		int i = 0;
		for (int j = 0; j < bytData.length; j++) {
			byte a = bytData[j];
			bytHex[i++] = bytHexTable[(a & 0xf0) >> 4];
			bytHex[i++] = bytHexTable[(a & 0x0f)];
		}

		try {
			return new String(bytHex, "UTF-8");			
		} catch (Exception e) {
			e.printStackTrace();  
		} 
		
		return null;
	}
	
	// 计算获取好友列表的hash参数
	private static String calcBuddyListHash(int nQQUin, String strPtWebQq)
	{
		try {
			byte[] bytPtWebQq = strPtWebQq.getBytes("UTF-8");
			
			byte[] a = {0, 0, 0, 0};
			for (int s = 0; s < bytPtWebQq.length; s++)
				a[s%4] ^= bytPtWebQq[s];

			byte[] j = {'E', 'C', 'O', 'K'};
			int[] d = {0, 0, 0, 0};
			d[0] = nQQUin >> 24 & 255 ^ j[0];
			d[1] = nQQUin >> 16 & 255 ^ j[1];
			d[2] = nQQUin >> 8 & 255 ^ j[2];
			d[3] = nQQUin & 255 ^ j[3];

			int[] j2 = {0,0,0,0,0,0,0,0};
			for (int s = 0; s < j2.length; s++)
				j2[s] = (s % 2 == 0 ? a[s >> 1] : d[s >> 1]);
			byte[] a2 = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
			byte[] d2 = new byte[j2.length*2];
			for (int s = 0; s < j2.length; s++) {
				d2[s*2] = a2[j2[s]>>4&15];
				d2[s*2+1] = a2[j2[s]&15];
			}
			
			return new String(d2, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return "";
	}

}
