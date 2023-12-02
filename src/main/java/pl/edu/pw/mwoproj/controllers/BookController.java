package pl.edu.pw.mwoproj.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.mwoproj.dtos.BookDto;
import pl.edu.pw.mwoproj.mappers.BookMapper;
import pl.edu.pw.mwoproj.services.BookService;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService service;
    private final BookMapper mapper;

    @Autowired
    public BookController(BookService service, BookMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public String get(Model model) {
        var books = service.getAll();
        model.addAttribute("books", books);
        return "books/index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        service.delete(id);

        return "redirect:/books";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("book", new BookDto());

        return "books/create";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute BookDto dto) {
        service.create(mapper.mapToEntity(dto));

        return "redirect:/books";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable int id, Model model) {
        var book = service.getById(id);

        model.addAttribute("updateId", id);
        model.addAttribute("book", book);

        return "books/update";
    }

    @PostMapping("/update/{id}")
    public String saveWithUpdate(@PathVariable int id, @ModelAttribute BookDto dto) {
        service.update(id, mapper.mapToEntity(dto));

        return "redirect:/books";
    }
}
