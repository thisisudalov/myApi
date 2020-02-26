package Service;

import DAO.GoodsDAOImpl;
import Entity.Goods;

import java.util.List;

public class GoodsService {

    private GoodsDAOImpl goodsDAO = new GoodsDAOImpl();

    public GoodsService() {
    }

    public Goods findGoods(int id) {
        return goodsDAO.findById(id);
    }

    public void saveGoods(Goods goods) {
        goodsDAO.save(goods);
    }

    public void deleteGoods(Goods goods) {
        goodsDAO.delete(goods);
    }

    public void updateGoods(Goods goods) {
        goodsDAO.update(goods);
    }

    public List<Goods> findAllGoods() {
        return goodsDAO.findAll();
    }

    public List<Goods> findByCode(long code){ return goodsDAO.findByCode(code); }

    public Goods findByWarehouseAndCode(String warehouse, long code) { return goodsDAO.findByWarehouseAndCode(warehouse, code); }

    public List<Goods> findByWarehouse(String warehouse){ return goodsDAO.findByWarehouse(warehouse); }

    public List<Goods> findByName(String name){ return goodsDAO.findByName(name); }

}