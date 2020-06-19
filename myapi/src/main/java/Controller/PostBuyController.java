package Controller;

import Entity.Document;
import Entity.DocumentEntity;
import Entity.Goods;
import Service.DocumentService;
import Service.GoodsService;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.*;



public class PostBuyController {
    static String post(String body, GoodsService goodsService){
        String response = "";

        DocumentService documentService = new DocumentService();
        boolean b = false;

        System.out.println("BODY: " + body);
        //Сериализация JSON в объект типа Document
        Document document = null;
        try {
            document = new Gson().fromJson(body, Document.class);
        }catch (JsonParseException e){return "Ошибка парсинга документа";}
        //Проверка существования склада
        if(!Controller.warehouses.contains(document.getWarehouse())) return "No such warehouse - " + document.getWarehouse();
        b = true;
        //Получение объектов товаров из документа
        List<Goods> goodsFromDoc = document.getGoods();
        //
        for (int i = 0; i < goodsFromDoc.size(); i++) {


            Goods tempGood = goodsFromDoc.get(i);
            //Установка параметра Warehouse_name всем объектам пришедшего списка товаров
            tempGood.setWarehouse_name(document.getWarehouse());
            //Проверка наличия на всех складах записи с определенным артикулем
            List<Goods> sameGoodsByCode = goodsService.findByCode(tempGood.getCode());
            int size = sameGoodsByCode.size();

            //Если записи нет то создается новая
            if (size<1) {
                tempGood.setLastSellPrice(0);
                goodsService.saveGoods(tempGood);
                System.out.println("Новая запись");
            //Если запись уже есть то нужно решить одинаковый ли склад указан
            }else{
                List<String> tempList = new ArrayList<>();
                for (int j = 0; j < sameGoodsByCode.size(); j++) {
                    tempList.add(sameGoodsByCode.get(j).getWarehouse_name());
                }
                for (int j = 0; j < sameGoodsByCode.size(); j++) {
                    sameGoodsByCode.get(j).setLastBuyPrice(tempGood.getLastBuyPrice());
                    goodsService.updateGoods(sameGoodsByCode.get(j));
                }



                /* //Проверка существования имени и кода
                Set<String> gSet = new HashSet();
                List<Goods> glist = goodsService.findAllGoods();
                for (int j = 0; j < glist.size(); j++) {
                    gSet.add(glist.get(j).getName());
                }

                System.out.println("ТОВАР УЖЕ ЕСТЬ");
                if(!gSet.contains(sameGoodsByCode.get(0).getName()))
                {
                    response += "Товар с таким артикулем существует, нельзя изменить имя" + "\n";
                    break;
                }
                */

                if(!tempList.contains(tempGood.warehouse_name)){
                    tempGood.setLastSellPrice(0);
                    goodsService.saveGoods(tempGood);
                    response+="Запись создана с таким же артикулем\n";
                }else {
                    Goods singleGood = goodsService.findByWarehouseAndCode(tempGood.getWarehouse_name(), tempGood.getCode());
                    singleGood.setLastBuyPrice(tempGood.getLastBuyPrice());
                    System.out.println(tempGood.getAmount()+ "; " +singleGood.getAmount());
                    singleGood.setAmount(tempGood.getAmount()+singleGood.getAmount());
                    goodsService.updateGoods(singleGood);
                    response += "Запись обновлена \n";
                }
            }
        }
        if (b){ documentService.saveDocument(new DocumentEntity(document.getWarehouse(), "null", document.getGoods())); }
        return response;
    }
}
