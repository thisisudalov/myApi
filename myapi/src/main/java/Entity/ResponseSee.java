package Entity;

import Service.GoodsService;
import java.util.*;

public class ResponseSee {

    static public String build(String warehouse){
        String response = "";
        Map<String, Integer> rgmap = new HashMap<>();
        GoodsService goodsService = new GoodsService();
        List<Goods> goods = goodsService.findByWarehouse(warehouse);
        for (int i = 0; i < goods.size(); i++) {
            if (goods.get(i).getAmount()>0) {
                rgmap.put(goods.get(i).getWarehouse_name(), goods.get(i).getAmount());
                response += new ResponseGood(rgmap, goods.get(0).getName(), goods.get(0).getCode());
            }
        }return response;
    }

    static public String build(){
        GoodsService goodsService = new GoodsService();
        List<Goods> gList = goodsService.findAllGoods();
        Set<Long> gSet = new HashSet<>();
        List<Goods> goodsTemp = new ArrayList<>();
        Map<String, Integer> rgmap = new HashMap<>();
        String resp = "";
        //Сет кодов имеющихся товаров на всех складах
        for (int i = 0; i < gList.size(); i++) {
            gSet.add(gList.get(i).getCode());
        }
        ArrayList<Long> set = new ArrayList<>();
        set.addAll(gSet);
        for (int i = 0; i<set.size(); i++) {
            goodsTemp.addAll(goodsService.findByCode(set.get(i)));
            for (int j = 0; j < goodsTemp.size(); j++) {
                if (goodsTemp.get(j).getAmount()>0) rgmap.put(goodsTemp.get(j).getWarehouse_name(), goodsTemp.get(j).getAmount());
            }
            if (rgmap.size()>0) resp+=new ResponseGood(rgmap, goodsTemp.get(0).getName(), goodsTemp.get(0).getCode());
            rgmap.clear();
            goodsTemp.clear();
        }
        //Создание списка товаров с именами, кодами и структурой Map<String, Integer>
        StringBuilder sb = new StringBuilder(resp);
        sb.setCharAt(sb.length()-1, ' ');
        System.out.println(sb);
        resp = "{\n" + "\"goods\" : \n[" + sb + "\n]\n}";
        return resp;
    }
}
