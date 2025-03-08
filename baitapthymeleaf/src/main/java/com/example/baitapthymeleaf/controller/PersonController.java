package com.example.baitapthymeleaf.controller;

import com.example.baitapthymeleaf.db.InitDB;
import com.example.baitapthymeleaf.model.Person;
import com.example.baitapthymeleaf.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PersonController {
    private final InitDB initDB; // Inject InitDB
    private final PersonService personService;
    public PersonController(InitDB initDB, PersonService personService) {
        this.initDB = initDB;
        this.personService = personService;
    }
        @GetMapping("/")
        public String home() {
            return "index"; // Trả về trang index.html
        }

        @GetMapping("/getAll")
        public String getAllPeople(Model model) {

            List<Person> people = personService.getPeople(); // Lấy danh sách từ InitDB
            model.addAttribute("people", people);
            return "people"; // Trả về trang liệt kê tất cả người
        }

        @GetMapping("/sortPeopleByFullName")
        public String sortByFullName(Model model) {
            List<Person> sortedPeople = personService.sortPeople();

            model.addAttribute("sortedPeople", sortedPeople);
            return "sorted-name"; // Trả về trang sắp xếp theo full name
        }

        @GetMapping("/getSortedJobs")
        public String getSortedJobs(Model model) {
            List<Person> sortedPeople = personService.sortJob();

            model.addAttribute("sortedPeople", sortedPeople);
            return "sorted-jobs"; // Trả về trang sắp xếp theo nghề nghiệp
        }

        @GetMapping("/getSortedCities")
        public String getSortedCities(Model model) {
            List<Person> sortedPeople = personService.sortCity();

            model.addAttribute("sortedPeople", sortedPeople);
            return "sorted-cities.html"; // Trả về trang sắp xếp theo thành phố
        }
//    Liệt kê các thành phố và danh sách người đang sống ở thành phố đó -> http://localhost:8080/groupPeopleByCity
    @GetMapping("/groupPeopleByCity")
public String getPeopleByCity(Model model) {
        ResponseEntity<Map<String, Integer>> response = (ResponseEntity<Map<String, Integer>>) personService.getPeopleByCity();
        Map<String, Integer> map = response.getBody();
        model.addAttribute("people", map);
        return "people-by-city";
    }
//    Liệt kê danh sách nghề nghiệp và số người làm nghề đó -> http://localhost:8080/groupJobByCount
    @GetMapping("/groupJobByCount")
        public String getJobCount(Model model) {
        ResponseEntity<Map<String, Integer>> response = (ResponseEntity<Map<String, Integer>>) personService.getJobCount();
        Map<String, Integer> map = response.getBody();
        model.addAttribute("people", map);
        return "job-count";
    }
//    Liệt kê danh sách người có mức lương lớn hơn mức lương trung bình -> http://localhost:8080/aboveAverageSalary
    @GetMapping("/aboveAverageSalary")
    public String getAverageSalary(Model model) {
        List<Person> people = personService.getAverageSalary();
        model.addAttribute("people", people);
        return "average-salary";
    }
//    Hiển thị người có độ dài tên dài nhất -> http://localhost:8080/longestName
    @GetMapping("longestName")
    public String getLongestName(Model model) {
        List<Person> people = personService.getLongsName();
        model.addAttribute("people", people);
        return "longest-name";
    }
    // Danh sach nguoi lien quan den person hien tai
        @GetMapping("/{id}")
        public String getPersonById(Model model, @PathVariable String id) {
            List<Person> personSort = personService.getPersonById(id);
            model.addAttribute("personSort", personSort);
            return "person-sort.html";
        }

}


