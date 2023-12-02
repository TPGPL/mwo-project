package pl.edu.pw.mwoproj.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.mwoproj.dtos.PublisherDto;
import pl.edu.pw.mwoproj.mappers.PublisherMapper;
import pl.edu.pw.mwoproj.services.PublisherService;

@Controller
@RequestMapping("/publishers")
public class PublisherController {
    private final PublisherService service;
    private final PublisherMapper mapper;

    @Autowired
    public PublisherController(PublisherService service, PublisherMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public String get(Model model) {
        var publishers = service.getAll();
        model.addAttribute("publishers", publishers);
        return "publishers/index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        service.delete(id);

        return "redirect:/publishers";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("pub", new PublisherDto());

        return "publishers/create";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute PublisherDto dto) {
        service.create(mapper.mapToEntity(dto));

        return "redirect:/publishers";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable int id, Model model) {
        var publisher = service.getById(id);

        model.addAttribute("updateId", id);
        model.addAttribute("pub", publisher);

        return "publishers/update";
    }

    @PostMapping("/update/{id}")
    public String saveWithUpdate(@PathVariable int id, @ModelAttribute PublisherDto dto) {
        service.update(id, mapper.mapToEntity(dto));

        return "redirect:/publishers";
    }
}
