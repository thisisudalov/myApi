package Entity;

import java.util.List;
//Класс для сериализации приходящих документов на продажу и покупку - одинаковый конструктор
public class Document {
    String warehouse;
    List<Goods> goods;

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    public Document(String warehouse, List<Goods> goods) {
        this.warehouse = warehouse;
        this.goods = goods;
    }

    public Document(){}

    @Override
    public String toString() {
        return "Document{" +
                "warehouse='" + warehouse + '\'' +
                ", list=" + goods +
                '}';
    }
}
