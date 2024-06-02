package multipledatasourcedemo.controller;

import jakarta.persistence.EntityManagerFactory;
import multipledatasourcedemo.entity.acc.Person;
import multipledatasourcedemo.entity.inv.ItemMaster;
import multipledatasourcedemo.repo.inv.ItemRepos;
import multipledatasourcedemo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {


    @Autowired
    private PersonService personService;

    @Autowired
    private ItemRepos itemRepos;

    @Autowired
    @Qualifier("accJdbcTemplate")
    private JdbcTemplate accJdbcTemplate;

    @GetMapping("/say/hi")
    public ResponseEntity<String> getdata(){
        return ResponseEntity.ok().body("hello");
    }

    @PostMapping("/save/single/person")
    public Person savePerson(@RequestHeader("boxId") String boxId, @RequestBody Person person){
        System.out.println("boxId=> "+boxId+" person=> "+ person.getFirst_name());
        return personService.savePerson(person);
    }

    @GetMapping("/find/all/person")
    public List<Person> getAllPerson(@RequestHeader("boxId") String boxId){
        return personService.getAllPerson();
    }

    @PostMapping("/save/single/item")
    public ItemMaster saveItem(@RequestHeader("boxId") String boxId, @RequestBody ItemMaster itemMaster){
        return itemRepos.save(itemMaster);
    }

    @GetMapping("/find/all/items")
    public List getAllItems(){
        return accJdbcTemplate.queryForList("select * from Inventory.item_master");
//        return itemRepos.findAll();
    }
}
