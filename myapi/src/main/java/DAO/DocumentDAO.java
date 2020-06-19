package DAO;

import Entity.DocumentEntity;

import java.util.List;

public interface DocumentDAO {

    DocumentEntity findById(int id);

    void save(DocumentEntity document);

    void update(DocumentEntity document);

    void delete(DocumentEntity document);

    List<DocumentEntity> findAll();
}
