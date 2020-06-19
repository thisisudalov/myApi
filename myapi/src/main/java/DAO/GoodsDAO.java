package DAO;

import Entity.Goods;

import java.util.List;

public interface GoodsDAO {

    Goods findById(int id);

    void save(Goods goods);

    void update(Goods goods);

    void delete(Goods goods);

    List<Goods> findAll();

    Goods findByWarehouseAndCode(String warehouse, long code);

    List<Goods> findByCode(long code);

    List<Goods> findByWarehouse(String warehouse);

    List<Goods> findByName(String name);
}
