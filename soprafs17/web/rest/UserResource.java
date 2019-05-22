package ch.uzh.ifi.seal.soprafs17.web.rest;

import ch.uzh.ifi.seal.soprafs17.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import ch.uzh.ifi.seal.soprafs17.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs17.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/*

List<User> listUsers()
User addUser(@RequestBody User user)
User getUser(@PathVariable Long userId)
User login(@PathVariable Long userId)
void logout(@PathVariable Long userId, @RequestParam("token") String userToken)

 */



@RestController
@RequestMapping(UserResource.CONTEXT)
public class UserResource
        extends GenericResource {

    Logger                 logger  = LoggerFactory.getLogger(UserResource.class);

    static final String    CONTEXT = "/user";

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<User> listUsers() {
        logger.debug("listUsers");

        List<User> result = new ArrayList<>();
        userRepo.findAll().forEach(result::add);

        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public User addUser(@RequestBody User user) {
        User loggedIn = userService.login(user.getUsername());

        if(loggedIn == null){
            logger.debug("addUser: " + user);
            loggedIn = userService.createUser(user.getUsername());
            //loggedIn = userService.login(user.getUsername());
        }
        return loggedIn;
    }

/* Not used???
 *  * * *
    @RequestMapping(method = RequestMethod.GET, value = "{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable Long userId) {
        logger.debug("getUser: " + userId);
        return userRepo.findOne(userId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "{userId}/login")
    @ResponseStatus(HttpStatus.OK)
    public User login(@PathVariable Long userId) {
        logger.debug("login: " + userId);

        User user = userRepo.findOne(userId);
        if (user != null) {
            user.setToken(UUID.randomUUID().toString());
            user.setStatus(UserStatus.ONLINE);
            user = userRepo.save(user);

            return user;
        }

        return null;
    }

    @RequestMapping(method = RequestMethod.POST, value = "{userId}/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@PathVariable Long userId, @RequestParam("token") String userToken) {
        userService.logout(userToken,userId);
        logger.debug("getUser: " + userId);
    }

    */
}
