package multipledatasourcedemo.entity.inv;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "item_master")
@Data
public class ItemMaster {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "item_name")
    private String item_name;

    @Column(name = "item_group_name")
    private String item_group_name;

    @Column(name = "item_unit_id")
    private String item_unit_id;
}
