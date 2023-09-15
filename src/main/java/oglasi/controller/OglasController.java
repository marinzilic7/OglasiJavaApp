package oglasi.controller;
import oglasi.model.*;
import oglasi.repositories.CategoryRepository;
import oglasi.repositories.OglasiRepository;
import oglasi.repositories.RecenzijeRepository;
import oglasi.repositories.UserRepository;
import jakarta.validation.Valid;
import oglasi.model.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import oglasi.model.Recenzije;


@Controller
public class OglasController {
    @Autowired
    OglasiRepository oglasiRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    RecenzijeRepository recenzijeRepository;






    @GetMapping("/oglasi")
    public String showOglas (Model model,@AuthenticationPrincipal UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        Long userId = userDetails.getUserId(); // ili koristite metodu kojom dobavljate ID korisnika
        List<Category> categories = categoryRepository.findAll();

        System.out.println(categories.size());
        model.addAttribute("oglas", oglasiRepository.findAll());
        model.addAttribute("categories", categories);
        model.addAttribute("userId", userId);
        model.addAttribute("user", user);
        model.addAttribute("oglasi", new Oglasi());
        model.addAttribute("added", false);
        model.addAttribute("activeLink", "Igre");
        User userr = userDetails.getUser();
        Long userIdd = user.getUserId();


        return "oglasi";
    }

    @PostMapping("/oglasi/add")
    public String addOglas (@Valid Oglasi oglasi, BindingResult result, Model model, RedirectAttributes redirectAttributes, UserDetails userDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        if (result.hasErrors()) {
            List<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("oglas", oglasi);
            model.addAttribute("oglasi", oglasiRepository.findAll());
            model.addAttribute("added", true);
            model.addAttribute("activeLink", "Igre");
            return "oglasi";
        }
        Long userIdd = user.getUserId();
        User selectedUser = userRepository.findById(userIdd).orElse(null);
        oglasi.setUser(selectedUser);
        Long categoryId = oglasi.getCategory().getId();
        Category selectedCategory = categoryRepository.findById(categoryId).orElse(null);
        oglasi.setCategory(selectedCategory);

        oglasiRepository.save(oglasi);
        redirectAttributes.addFlashAttribute("successOglas", "Oglas je uspješno dodan!");
        return "redirect:/oglasi";
    }

    @GetMapping("/singleOglas/{id}")
    public String showSingleOglas(@PathVariable("id") Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        model.addAttribute("user", user);
        Oglasi oglasi = oglasiRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        List<Recenzije> recenzije = oglasi.getRecenzija();
        model.addAttribute("oglasi", oglasi);
        model.addAttribute("recenzije", recenzije);
        model.addAttribute("oglas", oglasiRepository.findAll());
        model.addAttribute("recenzija", recenzijeRepository.findAll());
        model.addAttribute("activeLink", "Kategorije");
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "singleOglas";
    }



    @GetMapping("/oglasi/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        model.addAttribute("user", user);
        Oglasi oglasi = oglasiRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        model.addAttribute("oglasi", oglasi);
        model.addAttribute("oglas", oglasiRepository.findAll());
        model.addAttribute("activeLink", "Kategorije");
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "oglasi_edit";
    }

    @PostMapping("oglasi/edit/{id}")
    public String editOglas (@PathVariable("id") Long id, @Valid Oglasi oglasi, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        if (result.hasErrors()) {
            model.addAttribute("oglasi", oglasi);
            model.addAttribute("activeLink", "Igre");
            return "oglasi_edit";
        }
        Long userIdd = user.getUserId();
        User selectedUser = userRepository.findById(userIdd).orElse(null);
        oglasi.setUser(selectedUser);
        oglasiRepository.save(oglasi);
        redirectAttributes.addFlashAttribute("successOglas", "Oglas je uspješno uredjen!");
        return "redirect:/oglasi";
    }


    @GetMapping("/oglasi/delete/{id}")
    public String deleteOglas(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {

        try {
            oglasiRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successOglas", "Oglas je uspješno izbrisan.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Prvo izbrisite sve recenzije koje su vezane za ovaj oglas.");
        }

        return "redirect:/oglasi";
    }

}
