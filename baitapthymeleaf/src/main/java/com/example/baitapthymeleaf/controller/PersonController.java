package com.example.baitapthymeleaf.controller;

import com.example.baitapthymeleaf.db.InitDB;
import com.example.baitapthymeleaf.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PersonController {
    private final InitDB initDB; // Inject InitDB

    public PersonController(InitDB initDB) {
        this.initDB = initDB;
    }
        @GetMapping("/")
        public String home() {
            return "index"; // Trả về trang index.html
        }

        @GetMapping("/getAll")
        public String getAllPeople(Model model) {

            List<Person> people = initDB.getPeople(); // Lấy danh sách từ InitDB
            model.addAttribute("people", people);
            return "people"; // Trả về trang liệt kê tất cả người
        }

        @GetMapping("/sortPeopleByFullName")
        public String sortByFullName(Model model) {
            List<Person> people = initDB.getPeople();
            List<Person> sortedPeople = people.stream()
                    .sorted((o1, o2) -> o1.getFullName().compareTo(o2.getFullName()))
                    .toList();
            model.addAttribute("sortedPeople", sortedPeople);
            return "sorted-name"; // Trả về trang sắp xếp theo full name
        }

        @GetMapping("/getSortedJobs")
        public String getSortedJobs(Model model) {
            List<Person> people = initDB.getPeople();
            List<Person> sortedPeople = people.stream()
                    .sorted((o1, o2) -> o1.getJob().compareTo(o2.getJob()))
                    .toList();
            model.addAttribute("sortedPeople", sortedPeople);
            return "sorted-jobs"; // Trả về trang sắp xếp theo nghề nghiệp
        }

        @GetMapping("/getSortedCities")
        public String getSortedCities(Model model) {
            List<Person> people = initDB.getPeople();
            List<Person> sortedPeople = people.stream()
                    .sorted((o1, o2) -> o1.getCity().compareTo(o2.getCity()))
                    .toList();
            model.addAttribute("sortedPeople", sortedPeople);
            return "sorted-cities.html"; // Trả về trang sắp xếp theo thành phố
        }
        @GetMapping("/{id}")
        public String getPersonById(Model model, @PathVariable String id) {
            List<Person> people = initDB.getPeople();

            // Lấy gender của person có id trùng với id
            String gender = people.stream()
                    .filter(person -> person.getId().equals(id))
                    .findFirst()
                    .map(Person::getGender)
                    .orElse("DefaultGender"); // Giới tính mặc định nếu không tìm thấy person

            List<Person> personSort = people.stream()
                    .filter(p -> !p.getId().equals(id) && p.getGender().equals(gender))  // Lọc person có gender giống, id khác
                    .sorted((o1, o2) -> o2.getId().compareTo(o1.getId()))  // Sắp xếp giảm dần theo id
                    .limit(4)  // Giới hạn lấy 4 person
                    .toList();  // Thu thập kết quả thành List

            model.addAttribute("personSort", personSort);
            return "person-sort.html";
        }

}


