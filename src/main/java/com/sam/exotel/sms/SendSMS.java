package com.sam.exotel.sms;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sam.exotel.ExotelJavaApiTemplate;

/**
 * Class to send an SMS using Exotel based on the Send SMS API 
 * http://support.exotel.in/support/solutions/articles/48282-sending-an-sms
 * @author Saifuddin Merchant
 *
 */
public class SendSMS {

	public static Logger sLogger = LoggerFactory.getLogger(SendSMS.class);
	ExotelJavaApiTemplate exotelJavaApiTemplate = new ExotelJavaApiTemplate();

	public static void main(String[] args) {
		SendSMS sendSMS = new SendSMS();
		sendSMS.sendSMS();
	}

	private void sendSMS() {

		String userName = "";
		String token = "";
		String url = "https://{sid}:{token}@twilix.exotel.in/v1/Accounts/{sid}/Sms/send";

		List<NameValuePair> param = new ArrayList<NameValuePair>(1);

		/*
		 * 'From' doesn't matter; For transactional, this will be replaced with your
		 * SenderId; For promotional, this will be ignored by the SMS gateway.
		 * Replace the text "receiver" with the number to which the SMS has to be
		 * sent
		 */
		param.add(new BasicNameValuePair("To", "09029564507"));
		param.add(new BasicNameValuePair("Body", "This is a test message being sent using Exotel with a (SAIFU-4) and (103). If this is being abused, report to 08088919888"));

		try {
			exotelJavaApiTemplate.callAPI(userName, token, url, param);
		} catch (Exception e) {
			//Ignore exception, but log it
			sLogger.error("Exception: {}", e);	//Logs e.toString(0 which is what we want to log
		}

	}
}
