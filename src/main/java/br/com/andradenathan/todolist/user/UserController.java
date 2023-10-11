package br.com.andradenathan.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public void index() {
        System.out.println("teste");
    }

    @PostMapping("/")
    public ResponseEntity create(@RequestBody User userData) {
        var user = this.userRepository.findByUsername(userData.getUsername());
        if(user != null) {
            System.out.println("Usuário já existente");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existente");
        }

        var hashedPassword = BCrypt.withDefaults().hashToString(12, userData.getPassword().toCharArray());
        userData.setPassword(hashedPassword);

        var createdUserData = this.userRepository.save(userData);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserData);
    }
}
