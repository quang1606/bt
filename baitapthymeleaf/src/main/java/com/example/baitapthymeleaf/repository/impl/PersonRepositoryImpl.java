package com.example.baitapthymeleaf.repository.impl;

import ch.qos.logback.core.model.Model;
import com.example.baitapthymeleaf.db.InitDB;
import com.example.baitapthymeleaf.db.PersonDB;
import com.example.baitapthymeleaf.model.Person;
import com.example.baitapthymeleaf.repository.PersonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

    @Override
    public List<Person> getPeople( ) {
        return PersonDB.people;
    }

    @Override
    public List<Person> sortJob() {
        List<Person> sortedPeople = PersonDB.people.stream()
                .sorted((o1, o2) -> o1.getJob().compareTo(o2.getJob()))
                .toList();
        return sortedPeople;
    }

    @Override
    public List<Person> sortPeople() {
        List<Person> sortedPeople = PersonDB.people.stream()
                .sorted((o1, o2) -> o1.getFullName().compareTo(o2.getFullName()))
                .toList();
        return sortedPeople;
    }

    @Override
    public List<Person> sortCity() {
        List<Person> sortedPeople = PersonDB.people.stream()
                .sorted((o1, o2) -> o1.getCity().compareTo(o2.getCity()))
                .toList();
        return sortedPeople;
    }

    @Override
    public ResponseEntity<?> getPeopleByCity() {
        Map<String, Integer> map = new HashMap<>();

        for (Person person : PersonDB.people) {
            // Sử dụng getOrDefault để lấy giá trị hiện tại hoặc 0 nếu chưa có
            map.put(person.getCity(), map.getOrDefault(person.getCity(), 0) + 1);
        }

        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<?> getJobCount() {
        Map<String, Integer> map = new HashMap<>();

        for (Person person : PersonDB.people) {
            map.put(person.getJob(), map.getOrDefault(person.getJob(), 0) + 1);
        }

        return ResponseEntity.ok(map);
    }

    @Override
    public List<Person> getAverageSalary() {
        double sum =0;
        for (Person person : PersonDB.people) {
            sum += person.getSalary();
        }
        double average = sum / PersonDB.people.size();
        List<Person> people = PersonDB.people.stream().filter(person -> person.getSalary()>average).toList();
        return people;
    }

    @Override
    public List<Person> getLongsName() {
        OptionalInt maxLength = PersonDB.people.stream()
                .mapToInt(person -> person.getFullName().length())
                .max();
        if (maxLength.isEmpty()) {
            return Collections.emptyList(); // Trả về danh sách rỗng nếu không có dữ liệu
        }
        return PersonDB.people.stream()
                .filter(person -> person.getFullName().length() == maxLength.getAsInt())
                .toList();
    }

    @Override
    public List<Person> getPersonById(String id) {
        String gender = PersonDB.people.stream()
                .filter(person -> person.getId().equals(id))
                .findFirst()
                .map(Person::getGender)
                .orElse("DefaultGender"); // Giới tính mặc định nếu không tìm thấy person

        List<Person> personSort = PersonDB.people.stream()
                .filter(p -> !p.getId().equals(id) && p.getGender().equals(gender))  // Lọc person có gender giống, id khác
                .sorted((o1, o2) -> o2.getId().compareTo(o1.getId()))  // Sắp xếp giảm dần theo id
                .limit(4)  // Giới hạn lấy 4 person
                .toList();  // Thu thập kết quả thành List
        return personSort;
    }


}
