package multipledatasourcedemo.service;

import multipledatasourcedemo.entity.acc.Person;
import multipledatasourcedemo.repo.acc.PersonRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonRepos personRepos;

    public Person savePerson(Person p){
        return personRepos.save(p);
    }

    public List<Person> getAllPerson(){
        return personRepos.findAll();
    }
}
