package ch.uzh.ifi.seal.soprafs17.service;

import ch.uzh.ifi.seal.soprafs17.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import ch.uzh.ifi.seal.soprafs17.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


/**
 * Service class for managing users.

 UserService(UserRepository userRepository)
 User login(String username)
 User logout(String userToken, Long userId)
 User createUser(String username)
 void deleteUser(Long id)

 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User login(String username){
        User user = userRepository.findByUsername(username);
        if(user != null) {
            user.setStatus(UserStatus.ONLINE);
        }
        return user;
    }

    public User createUser(String username) {

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setStatus(UserStatus.OFFLINE);
        newUser.setToken(UUID.randomUUID().toString());
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);

        return newUser;
    }

}
