package com.team12.finalproject.controller.front;

import com.team12.finalproject.controller.PostController;
import com.team12.finalproject.controller.UserController;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequiredArgsConstructor
@ApiIgnore
public class UserFrontController {

    private final UserController userController;
    private final PostController postController;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/join")
    public String join() {
        return "userJoin";
    }
}
