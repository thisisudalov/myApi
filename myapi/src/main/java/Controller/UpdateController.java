package Controller;

import Entity.DocumentEntity;
import Entity.DocumentMove;
import Entity.Goods;
import Service.DocumentService;
import Service.GoodsService;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.List;

public class UpdateController {
    static String update(String body, GoodsService goodsService){
        String response = "";
        boolean b = false;
        DocumentService documentService = new DocumentService();

        //Сериализация JSON в объект типа Document
        DocumentMove document;
        try {
            document = new Gson().fromJson(body, DocumentMove.class);
        }catch (JsonParseException e){return "Ошибка парсинга документа";}
        //Проверка существования склада
        if(!Controller.warehouses.contains(document.getWarehouse_to())) return "No such warehouse - " + document.getWarehouse_to();
        if(!Controller.warehouses.contains(document.getWarehouse())) return "No such warehouse - " + document.getWarehouse();
        //Получение объектов товаров из документа
        List<Goods> goodsFromDoc = document.getGoods();
        //Получение записей из бд по имени пришедшего склада
        List<Long> codes = new ArrayList<>();
        List<Goods> goodsFromWarehouse = goodsService.findByWarehouse(document.getWarehouse());
        List<Goods> goodsFromWarehouse_to = goodsService.findByWarehouse(document.getWarehouse_to());
        for (int i = 0; i < goodsFromWarehouse.size(); i++) {
            codes.add(goodsFromWarehouse.get(i).getCode());
        }

        for (int i = 0; i < goodsFromDoc.size(); i++) {
            Goods tempGood = goodsFromDoc.get(i);
            //Проверка наличия на определенном складе записи с определенным артикулем
            if (!codes.contains(tempGood.getCode())){

                response += "На складе "+document.getWarehouse()+" нет товара "+tempGood.getName()+"\n";
                continue;
            } else {
                Goods goodFromWarehouse = goodsService.findByWarehouseAndCode(document.getWarehouse(), tempGood.getCode());
                if(tempGood.getAmount()>goodFromWarehouse.getAmount())
                {
                    response+= "На складе " + document.getWarehouse() +
                            " не хватает товара " + tempGood.getName() +
                            " его всего "+ goodFromWarehouse.getAmount()+"\n";
                    continue;
                } else {
                    goodFromWarehouse.setAmount(goodFromWarehouse.getAmount() - tempGood.getAmount());
                    goodsService.updateGoods(goodFromWarehouse);

                    tempGood.setWarehouse_name(document.getWarehouse_to());
                    tempGood.setLastSellPrice(goodFromWarehouse.getLastSellPrice());
                    tempGood.setLastBuyPrice(goodFromWarehouse.getLastBuyPrice());


                    List<Long> codes_to = new ArrayList<>();

                    for (int j = 0; j < goodsFromWarehouse_to.size(); j++) {
                        codes_to.add(goodsFromWarehouse_to.get(i).getCode());
                    }
                    if(!codes_to.contains(tempGood.getCode())){
                        goodsService.saveGoods(tempGood);
                    } else {
                        Goods goodFromWarehouse_to = goodsService.findByWarehouseAndCode(tempGood.getWarehouse_name(), tempGood.getCode());
                        goodFromWarehouse_to.setAmount(goodFromWarehouse_to.getAmount()+tempGood.getAmount());
                        goodsService.updateGoods(goodFromWarehouse_to);
                    }
                    b = true;
                }
            }
        }
        if (b){ documentService.saveDocument(new DocumentEntity(document.getWarehouse(), document.getWarehouse_to(), document.getGoods())); }
        return response;
    }
}
