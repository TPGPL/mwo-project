package pl.edu.pw.mwoproj.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.mwoproj.dtos.AuthorDto;
import pl.edu.pw.mwoproj.mappers.AuthorMapper;
import pl.edu.pw.mwoproj.services.AuthorService;

@Controller
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService service;
    private final AuthorMapper mapper;

    @Autowired
    public AuthorController(AuthorService service, AuthorMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public String get(Model model) {
        var authors = service.getAll();
        model.addAttribute("authors", authors);
        return "authors/index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        service.delete(id);

        return "redirect:/authors";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("author", new AuthorDto());

        return "authors/create";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute AuthorDto dto) {
        service.create(mapper.mapToEntity(dto));

        return "redirect:/authors";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable int id, Model model) {
        var author = mapper.mapToDto(service.getById(id));

        model.addAttribute("updateId", id);
        model.addAttribute("author", author);

        return "authors/update";
    }

    @PostMapping("/update/{id}")
    public String saveWithUpdate(@PathVariable int id, @ModelAttribute AuthorDto dto) {
        service.update(id, mapper.mapToEntity(dto));

        return "redirect:/authors";
    }
}
