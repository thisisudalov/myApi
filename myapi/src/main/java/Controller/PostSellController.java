package Controller;

import Entity.Document;
import Entity.DocumentEntity;
import Entity.Goods;
import Service.DocumentService;
import Service.GoodsService;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.*;

public class PostSellController {
    static String post(String body, GoodsService goodsService){
        String response = "";
        boolean b = false;
        DocumentService documentService = new DocumentService();

        //Сериализация JSON в объект типа Document
        Document document;
        try {
            document = new Gson().fromJson(body, Document.class);
        }catch (JsonParseException e){return "Ошибка парсинга документа";}
        //Проверка существования склада
        if(!Controller.warehouses.contains(document.getWarehouse())) return "No such warehouse - " + document.getWarehouse();
        //Получение объектов товаров из документа
        List<Goods> goodsFromDoc = document.getGoods();
        //Получение записей из бд по имени пришедшего склада
        List<Long> codes = new ArrayList<>();
        List<Goods> goodsFromWarehouse = goodsService.findByWarehouse(document.getWarehouse());
        for (int i = 0; i < goodsFromWarehouse.size(); i++) {
            codes.add(goodsFromWarehouse.get(i).getCode());
        }

        for (int i = 0; i < goodsFromDoc.size(); i++) {
            Goods tempGood = goodsFromDoc.get(i);
            //Проверка наличия на определенном складе записи с определенным артикулем
            if (!codes.contains(tempGood.getCode())){
                response += "На складе "+document.getWarehouse()+" нет товара "+tempGood.getName()+"\n";
            } else {
                Goods goodFromWarehouse = goodsService.findByWarehouseAndCode(document.getWarehouse(), tempGood.getCode());
                if(tempGood.getAmount()>goodFromWarehouse.getAmount())
                {
                     response+= "На складе " + document.getWarehouse() +
                            " не хватает товара " + tempGood.getName() +
                            " его всего "+ goodFromWarehouse.getAmount()+"\n";
                } else {
                    //Товар найден и в наличии
                    List<Goods> glist = new ArrayList<>(goodsService.findByCode(goodFromWarehouse.getCode()));
                    for (int j = 0; j < glist.size(); j++) {
                        glist.get(j).setLastSellPrice(tempGood.getLastSellPrice());
                        goodsService.updateGoods(glist.get(j));
                    }
                    goodFromWarehouse.setAmount(goodFromWarehouse.getAmount() - tempGood.getAmount());
                    goodFromWarehouse.setLastSellPrice(tempGood.getLastSellPrice());
                    goodsService.updateGoods(goodFromWarehouse);
                    response+="Товар "+tempGood.getName() + " удален со склада в количестве "+ tempGood.getAmount() + "\n";
                }
            b = true;
            }
        }
        if (b){ documentService.saveDocument(new DocumentEntity(document.getWarehouse(), "null", document.getGoods())); }
        return response;
    }
}
