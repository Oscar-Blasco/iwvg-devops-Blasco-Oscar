package es.upm.miw.devops.rest;

import es.upm.miw.devops.dto.UserDto;
import es.upm.miw.devops.service.UserService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable String id) {
        return new UserDto(userService.findById(id));
    }
}
