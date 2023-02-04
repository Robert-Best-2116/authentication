package com.robertbest.authentication.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.robertbest.authentication.models.LoginUser;
import com.robertbest.authentication.models.User;
import com.robertbest.authentication.services.UserService;

@Controller
public class MainController {

    @Autowired
    private UserService userServ;
    
    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("newUser", new User());
        model.addAttribute("newLogin", new LoginUser());
        return "index.jsp";
    }
    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("newUser") User newUser, 
            BindingResult result, Model model, HttpSession session) {

    	userServ.register(newUser, result);
        
        if(result.hasErrors()) {
            model.addAttribute("newLogin", new LoginUser());
            return "index.jsp";
        }

        session.setAttribute("userId", newUser.getId());
    
        return "redirect:/dashboard";
    }
    
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin, 
    		BindingResult result, Model model, HttpSession session) {
        
    	User user = userServ.login(newLogin, result);
    	
        if(result.hasErrors()) {
        	model.addAttribute("newUser", new User());
            return "index.jsp";
        }
    
        session.setAttribute("userId", user.getId());
    
        return "redirect:/dashboard";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
    	Long userId = (Long) session.getAttribute("userId");
    	if(userId==null) {
    		return "redirect:/";
    	}
    	User user = userServ.findId(userId);
    	model.addAttribute("user", user);
    	return "dashboard.jsp";
    }
    
    @RequestMapping("/clear")
    public String clear(HttpSession session) {
    	session.invalidate();
    	return "redirect:/";
    	
    }
}
