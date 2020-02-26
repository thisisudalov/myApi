package Entity;

import java.util.List;
//Класс для сериализации запроса на перемещение товаров - конструктор отличается от Document
public class DocumentMove {
    String warehouse;
    String warehouse_to;
    List<Goods> goods;

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getWarehouse_to() {
        return warehouse_to;
    }

    public void setWarehouse_to(String warehouse_to) {
        this.warehouse_to = warehouse_to;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    public DocumentMove(String warehouse, List<Goods> goods) {
        this.warehouse = warehouse;
        this.goods = goods;
    }

    public DocumentMove(){}

    @Override
    public String toString() {
        return "Document{" +
                "warehouse='" + warehouse + '\'' +
                ", list=" + goods +
                "warehouse_to='" + warehouse_to + '\'' +
                '}';
    }
}