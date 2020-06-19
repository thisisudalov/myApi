package Entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "documents", schema = "schema")
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String warehouse;
    private String warehouse_to;
    private String goods;

    public DocumentEntity(){}

    public DocumentEntity(String warehouse, String warehouse_to, List<Goods> goods) {
        this.warehouse = warehouse;
        this.warehouse_to = warehouse_to;
        this.goods = goods.toString();
    }
}
