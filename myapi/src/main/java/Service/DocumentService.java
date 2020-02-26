package Service;

import DAO.DocumentDAOImpl;
import Entity.DocumentEntity;

import java.util.List;

public class DocumentService {

    private DocumentDAOImpl documentDAO = new DocumentDAOImpl();

    public DocumentService() {
    }

    public DocumentEntity findDocument(int id) {
        return documentDAO.findById(id);
    }

    public void saveDocument(DocumentEntity document) {
        documentDAO.save(document);
    }

    public void deleteDocument(DocumentEntity document) {
        documentDAO.delete(document);
    }

    public void updateDocument(DocumentEntity document) {
        documentDAO.update(document);
    }

    public List<DocumentEntity> findAllDocuments() {
        return documentDAO.findAll();
    }

}