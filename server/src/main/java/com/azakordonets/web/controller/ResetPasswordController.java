package com.azakordonets.web.controller;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Andrew Zakordonets
 * Date : 12/15/2015.
 * <p>
 */
public class ResetPasswordController {

    private static final Logger log = LogManager.getLogger(ResetPasswordController.class);

    private static final String BASE_URL = "http://tables.finance.ua/ua/currency/order";
    private static CloseableHttpClient httpclient = HttpClients.createDefault();

}
