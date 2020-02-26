package Entity;

import Service.GoodsService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResponseAllGoods {
    public static String build(){
        String response="";
        GoodsService goodsService = new GoodsService();
        Set<Long> gSet = new HashSet();
        List<Goods> glist = goodsService.findAllGoods();
        for (int i = 0; i < glist.size(); i++) {
            gSet.add(glist.get(i).getCode());
        }
        for (Long code : gSet) {
            response+= Goods.toJson(goodsService.findByCode(code).get(0)) +",";
        }
        StringBuilder sb = new StringBuilder(response);
        sb.setCharAt(sb.length()-1, ' ');
        return "{\"goods\" :["+ sb +"]}";
    }
    public static String build(String name){
        GoodsService goodsService = new GoodsService();
        Set<String> gSet = new HashSet();
        List<Goods> glist = goodsService.findAllGoods();
        for (int i = 0; i < glist.size(); i++) {
            gSet.add(glist.get(i).getName());
        }
        if (!gSet.contains(name)){return "Нет такого товара";}
        else {
            return Goods.toJson(goodsService.findByName(name).get(0));
        }
    }
}
