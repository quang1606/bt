package com.example.demo1application.Controller;

import com.example.demo1application.model.Book;
import com.example.demo1application.service.BookService;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo1application.db.BookDB.books;

@RequestMapping("/books") //Tham so chung o duoi g@GetMapping de trong,(co the thay duoc GetMapping nhung dai dong hon)
//@Controller//ap dung cho kieu tra ve(view) complate ->muon in ra du lieu thi thi +@ResponseBody
@RestController //danh dau len class -> xu ly reques client yeu cau tra ve restpon tuoong ung
// ClassResponseEntity<?>: class dai dien cho 1 doi tuong http response , co the chua body, header
@ToString
public class BookController {
        private final BookService bookService;

        public BookController(BookService bookService) {
                this.bookService = bookService;
        }
        //Lay danh sach books
        @GetMapping
        public ResponseEntity<?> getBooks() {
                List<Book> list = bookService.getAllBooks();
                return ResponseEntity.status(HttpStatus.OK).body(books);
        }


        //Viet api tra ve danh sach book theo id
        @GetMapping("/sortByYear/{id}")
        public ResponseEntity<?> getBooksByYear(@PathVariable String id) {
                List<Book> list = bookService.getBooksById(id);
                return ResponseEntity.status(HttpStatus.OK).body(list);
        }

        //SAP XEP THEO YEAR
        @GetMapping("/sortByYear")
        public ResponseEntity<?> getBooksByYear() {
                List<Book> list = bookService.getBooksYear();
                return ResponseEntity.status(HttpStatus.OK).body(list);
        }

        //  //Viet API de tim cuon sach ma trong title co chua key word, khong phan biet hoa thuong

        @GetMapping("/search/{keyword}")
        public ResponseEntity<?> getBooksByKeyword(@PathVariable String keyword) {
                List<Book> list = bookService.getBooksKey(keyword);
                return ResponseEntity.status(HttpStatus.OK).body(list);
        }


}
