package com.ensaj.examsEnsaj.examsEnsaj.controllers;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Module;
import com.ensaj.examsEnsaj.examsEnsaj.entites.Option;
import com.ensaj.examsEnsaj.examsEnsaj.respository.OptionRepository;
import com.ensaj.examsEnsaj.examsEnsaj.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private OptionRepository optionRepository;

    // Afficher la page de gestion des modules
    @GetMapping("{idp}/modules")
    public String showModulesPage(@PathVariable int idp ,Model model) {
        List<Module> modules = moduleService.getAllModules();
        Option option = optionRepository.findById(idp).get();
        model.addAttribute("option", option);
        model.addAttribute("modules", modules);

        return "modules";
    }

    // Ajouter un module
    @PostMapping("{idp}/modules/add")
    public String addModule(@ModelAttribute Module module, @PathVariable int idp) {
        Option option = optionRepository.findById(idp).orElse(null);

        if (option != null) {
            module.setOption(option);
            moduleService.addModule(module);
        }

        return "redirect:/" + idp + "/modules";
    }

    // Supprimer un module
    @GetMapping("{idp}/modules/delete/{id}")
    public String deleteModule(@PathVariable int id, @PathVariable int idp) {
        moduleService.deleteModule(id);
        return "redirect:/" + idp + "/modules";
    }

    // Modifier un module (pré-remplir le formulaire)
    @GetMapping("{idp}/modules/edit/{id}")
    public String editModuleForm(@PathVariable int id, @PathVariable int idp, Model model) {
        Module module = moduleService.getModuleById(id);
        Option option = optionRepository.findById(idp).orElse(null);
        model.addAttribute("module", module);
        model.addAttribute("option", option);
        return "module-edit"; // Vue Thymeleaf pour modifier le module
    }

    // Enregistrer les modifications du module
    @PostMapping("{idp}/modules/update")
    public String updateModule(@ModelAttribute Module module, @PathVariable int idp) {
        moduleService.updateModule(module.getId_module(), module);
        return "redirect:/" + idp + "/modules";
    }
    @PostMapping("{idp}/modules/import")
    public String importModules(@PathVariable int idp,
                                @RequestParam("file") MultipartFile file,
                                RedirectAttributes redirectAttributes) {
        try {
            // Vérifiez si le fichier est vide
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Veuillez choisir un fichier avant de soumettre.");
                return "redirect:/" + idp + "/modules";
            }

            // Appeler le service pour traiter le fichier
            moduleService.importModules(file, idp);

            redirectAttributes.addFlashAttribute("message", "Les modules ont été importés avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'importation : " + e.getMessage());
        }

        return "redirect:/" + idp + "/modules";
    }
}
