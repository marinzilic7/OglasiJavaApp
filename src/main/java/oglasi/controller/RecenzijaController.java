package oglasi.controller;


import oglasi.model.*;
import oglasi.model.Oglasi;
import oglasi.model.User;
import oglasi.repositories.OglasiRepository;
import oglasi.repositories.RecenzijeRepository;
import oglasi.repositories.UserRepository;
import jakarta.validation.Valid;
import oglasi.model.Recenzije;
import oglasi.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RecenzijaController {

    @Autowired
    OglasiRepository oglasiRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RecenzijeRepository recenzijeRepository;


    @GetMapping("/recenzija/{id}")
    public String showRecenzijaForm(@PathVariable("id") Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        Oglasi oglasi = oglasiRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        model.addAttribute("user", user);
        model.addAttribute("oglasi", oglasi);
        model.addAttribute("oglas", oglasiRepository.findAll());
        model.addAttribute("recenzije", new Recenzije());
        return "recenzija";
    }

    @PostMapping("/recenzija/add/{id}")
    public String addRecenzija(
            @PathVariable("id") Long oglasiId,
            @Valid Recenzije recenzije,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            UserDetails userDetails
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();


        if (result.hasErrors()) {
            List<Oglasi> oglasi = oglasiRepository.findAll();
            model.addAttribute("categories", oglasi);
            model.addAttribute("recenzija", recenzije);
            model.addAttribute("recenzije", recenzijeRepository.findAll());
            model.addAttribute("added", true);
            model.addAttribute("activeLink", "Igre");
            return "oglasi";
        }


        Long userIdd = user.getUserId();


        User selectedUser = userRepository.findById(userIdd).orElse(null);
        recenzije.setUser(selectedUser);


        Oglasi selectedOglasi = oglasiRepository.findById(oglasiId).orElse(null);
        recenzije.setOglasi(selectedOglasi);


        recenzijeRepository.save(recenzije);

        redirectAttributes.addFlashAttribute("successCourse", "Recenzija objavljena!");
        return "redirect:/oglasi";
    }

    @GetMapping("/recenzije/delete/{id}")
    public String deleteRecenziju(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {

        Recenzije recenzije = recenzijeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pogrešan ID"));
        recenzijeRepository.delete(recenzije);
        redirectAttributes.addFlashAttribute("successCourse", "Recenzija izbrisana!");


        return "redirect:/oglasi";
    }
}



