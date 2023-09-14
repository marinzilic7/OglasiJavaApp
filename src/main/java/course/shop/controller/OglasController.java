package course.shop.controller;
import course.shop.model.*;
import course.shop.repositories.CategoryRepository;
import course.shop.repositories.OglasiRepository;
import course.shop.repositories.RecenzijeRepository;
import course.shop.repositories.UserRepository;
import jakarta.validation.Valid;
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
import course.shop.model.Recenzije;


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
    public String showCourses (Model model,@AuthenticationPrincipal UserDetails userDetails) {
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
        System.out.println("User je" + userr);
        Long userIdd = user.getUserId();
        System.out.println("ID korisnika: " + userIdd);

        return "oglasi";
    }

    @PostMapping("/oglasi/add")
    public String addCourse (@Valid Oglasi oglasi, BindingResult result, Model model, RedirectAttributes redirectAttributes, UserDetails userDetails) {
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
        redirectAttributes.addFlashAttribute("successCourse", "Oglas je uspješno dodan!");
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
    public String editCoruse (@PathVariable("id") Long id, @Valid Oglasi oglasi, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
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
        redirectAttributes.addFlashAttribute("successCourse", "Oglas je uspješno uredjen!");
        return "redirect:/oglasi";
    }


    @GetMapping("/oglasi/delete/{id}")
    public String deleteGame(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {

            Oglasi oglasi = oglasiRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pogrešan ID"));
            oglasiRepository.delete(oglasi);
        redirectAttributes.addFlashAttribute("successCourse", "Oglas je uspješno izbrisan!");


        return "redirect:/oglasi";
    }

}
