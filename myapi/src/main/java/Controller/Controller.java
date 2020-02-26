package Controller;

import Entity.*;
import Service.DocumentService;
import Service.GoodsService;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Controller {

    static Set<String> warehouses = new HashSet<>();
    static {
        warehouses.add("Kolomenskoe");
        warehouses.add("ZIL");
        warehouses.add("Yasenevo");
        warehouses.add("Pobeda");
        warehouses.add("Vorsino");
    }
    public static void serverStart() throws IOException {

        int serverPort = 8001;


        DocumentService documentService = new DocumentService();

        GoodsService goodsService = new GoodsService();

        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        server.createContext("/api/buygoods", (exchange -> {


             if (("POST").equals(exchange.getRequestMethod())) {

                //Чтение body
                InputStream reqText = exchange.getRequestBody();
                InputStreamReader isReader = new InputStreamReader(reqText);
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer sb = new StringBuffer();
                String str;

                while((str = reader.readLine())!= null){
                    sb.append(str);
                }
                String body = sb.toString();

                if (body.length()>0) {
                    String response = PostBuyController.post(body, goodsService);
                    //Response generation
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream output = exchange.getResponseBody();
                    output.write(response.getBytes());
                    output.flush();
                }else{
                    String respText = "Empty request body" + "\n";
                    exchange.sendResponseHeaders(400, respText.getBytes().length);
                    OutputStream output = exchange.getResponseBody();
                    output.write(respText.getBytes());
                    output.flush();
                }
            }else {
                 String respText = "Method not allowed" + "\n";
                 exchange.sendResponseHeaders(405, respText.getBytes().length);
                 OutputStream output = exchange.getResponseBody();
                 output.write(respText.getBytes());
                 output.flush();
             }
        }));

        server.createContext("/api/getdocuments",(exchange -> {


            /////GET/////


            if (("GET").equals(exchange.getRequestMethod())) {

                String response = new Gson().toJson(documentService.findAllDocuments());

                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.setAttribute("Http status : ", 200);
                OutputStream output = exchange.getResponseBody();
                output.write(response.getBytes());
                output.flush();
            }else {
                String respText = "Method not allowed" + "\n";
                exchange.sendResponseHeaders(405, respText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            }
            exchange.close();
        }));

        server.createContext("/api/seegoodslist",(exchange -> {


            /////GET/////


            if (("GET").equals(exchange.getRequestMethod())) {

                String name = "";
                int rCode = 200;
                InputStream reqText = exchange.getRequestBody();
                InputStreamReader isReader = new InputStreamReader(reqText);
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer sb = new StringBuffer();
                String str;

                String response = "";

                while((str = reader.readLine())!= null){
                    sb.append(str);
                }
                String body = sb.toString();
                if (body.length()>0) {
                    try {
                        name = new Gson().fromJson(body, Name.class).getName();
                    }catch (JsonParseException e){response = "Ошибка парсинга документа";}
                    response+=ResponseAllGoods.build(name);
                } else {
                    response+=ResponseAllGoods.build();
                }
                exchange.sendResponseHeaders(rCode, response.getBytes().length);
                exchange.setAttribute("Http status : ", rCode);
                OutputStream output = exchange.getResponseBody();
                output.write(response.getBytes());
                output.flush();
            }else {
                String respText = "Method not allowed" + "\n";
                exchange.sendResponseHeaders(405, respText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            }
            exchange.close();
        }));
        server.createContext("/api/seegoodswhichleft",(exchange -> {


            /////GET/////


            if (("GET").equals(exchange.getRequestMethod())) {

                String warehouse = "";
                int rCode = 200;
                InputStream reqText = exchange.getRequestBody();
                InputStreamReader isReader = new InputStreamReader(reqText);
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer sb = new StringBuffer();
                String str;

                String response = "";

                while((str = reader.readLine())!= null){
                    sb.append(str);
                }
                String body = sb.toString();
                if (body.length()>0) {
                    try {
                        warehouse = new Gson().fromJson(body, Warehouse.class).getName();
                    }catch (JsonParseException e){response = "Ошибка парсинга документа";}
                    //Проверка существования склада
                    if(!Controller.warehouses.contains(warehouse))
                    {
                        response = "No such warehouse - " + warehouse;
                        rCode = 400;
                    }
                    response+=ResponseSee.build(warehouse);
                } else {
                    response+=ResponseSee.build();
                }
                exchange.sendResponseHeaders(rCode, response.getBytes().length);
                exchange.setAttribute("Http status : ", rCode);
                OutputStream output = exchange.getResponseBody();
                output.write(response.getBytes());
                output.flush();
            }else {
                String respText = "Method not allowed" + "\n";
                exchange.sendResponseHeaders(405, respText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            }
            exchange.close();
        }));


        server.createContext("/api/sellgoods",(exchange -> {


            /////POST на продажу/////


            if (("POST").equals(exchange.getRequestMethod())) {
                //Чтение body
                InputStream reqText = exchange.getRequestBody();
                InputStreamReader isReader = new InputStreamReader(reqText);
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer sb = new StringBuffer();
                String str;

                while((str = reader.readLine())!= null){
                    sb.append(str);
                }
                String body = sb.toString();

                if (body.length()>0) {
                    String response = PostSellController.post(body, goodsService);
                    //Response generation
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream output = exchange.getResponseBody();
                    output.write(response.getBytes());
                    output.flush();
                }else{
                    String respText = "Empty request body" + "\n";
                    exchange.sendResponseHeaders(400, respText.getBytes().length);
                    OutputStream output = exchange.getResponseBody();
                    output.write(respText.getBytes());
                    output.flush();
                }
            }else {
                String respText = "Method not allowed" + "\n";
                exchange.sendResponseHeaders(405, respText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            }
            exchange.close();
        }));
        server.createContext("/api/movegoods",(exchange -> {


            /////PUT на перемещение/////


            if (("PUT").equals(exchange.getRequestMethod())) {
                //Чтение body
                InputStream reqText = exchange.getRequestBody();
                InputStreamReader isReader = new InputStreamReader(reqText);
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer sb = new StringBuffer();
                String str;

                while((str = reader.readLine())!= null){
                    sb.append(str);
                }
                String body = sb.toString();

                if (body.length()>0) {
                    String response = UpdateController.update(body, goodsService);
                    //Response generation
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream output = exchange.getResponseBody();
                    output.write(response.getBytes());
                    output.flush();
                }else{
                    String respText = "Empty request body" + "\n";
                    exchange.sendResponseHeaders(400, respText.getBytes().length);
                    OutputStream output = exchange.getResponseBody();
                    output.write(respText.getBytes());
                    output.flush();
                }
            }
            else {
                String respText = "Method not allowed" + "\n";
                exchange.sendResponseHeaders(405, respText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            }
            exchange.close();
        }));
        server.setExecutor(null);
        server.start();
    }
}
