package io.practice.secretprovider.controllers;

import io.practice.secretprovider.model.SecretCreationResponse;
import io.practice.secretprovider.services.SecretService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class SecretContoller {

    @Autowired
    private SecretService secretService;

    @GetMapping("/send-secret")
    public String getSendSecretPage() {
        return "send-secret";
    }

    @PostMapping("/send-secret")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView sendSecret(String input, ModelAndView modelAndView) {
        var se = secretService.createSecret(input);
        SecretCreationResponse scr = new SecretCreationResponse(true,
                "http://localhost:8080/get-secret?id=" + se.getId());
        modelAndView.addObject("serverResponse", scr);
        return modelAndView;
    }

    @GetMapping("/get-secret")
    public ModelAndView getSecret(@PathParam("id") UUID id) {
        var s = secretService.getSecret(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("serverResponse", s);
        mv.setViewName("get-secret");
        return mv;
    }
}
