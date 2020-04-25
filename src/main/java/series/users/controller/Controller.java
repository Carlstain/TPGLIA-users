package series.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import series.users.errors.*;
import series.users.models.Serie;
import series.users.models.SharedSerie;
import series.users.models.User;
import series.users.services.ISharedSeriesService;
import series.users.services.IUserService;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    private ISharedSeriesService sharedSeriesService;

    @Autowired
    private IUserService userService;

    @LoadBalanced
    private final RestTemplate restTemplate = new RestTemplate();

    private User user;

    @RequestMapping(path = "/share/{id}")
    public List<SharedSerie> getSharedUser(@PathVariable Long id) {
        List<SharedSerie> sharedSeries = sharedSeriesService.sharedWithUser(id);
        if(sharedSeries.isEmpty())
            throw new NoContentException("No series shared with this user");
        return sharedSeries;
    }

    @RequestMapping(path = "/share")
    public List<SharedSerie> getShared() {
        List<SharedSerie> sharedSeries = sharedSeriesService.getAll();
        if(sharedSeries.isEmpty())
            throw new NoContentException();
        return sharedSeries;
    }

    @PostMapping(path = "/share", consumes = "application/json", produces = "application/json")
    public void shareSerie(@RequestBody SharedSerie sharedSerie)    {
        if (this.user == null)
            throw new RequestUnauthorizedException();
        if (this.user.getId() == sharedSerie.getUserId())
            throw new ForbiddenException("You cannot share a serie with youself");
        Serie serie = restTemplate.getForObject("http://localhost:8001/series/"+sharedSerie.getSerieId(), Serie.class);
        if (serie == null || this.user.getId() != serie.getUserid())
            throw new ForbiddenException("You cannot share a serie you do not own");
        if(sharedSerie.getSerieId() == null || sharedSerie.getUserId() == null || sharedSerie.getPermission() == null)
            throw new BadRequestException();
        sharedSeriesService.shareSerie(sharedSerie.getUserId(),sharedSerie.getSerieId(),sharedSerie.getPermission());
    }

    @DeleteMapping(path = "/share")
    public void revokePermissions(@RequestParam(name = "userid", required = true) Long userid,
                                  @RequestParam(name = "serieid", required = true) Long serieid) {
        if (this.user == null)
            throw new RequestUnauthorizedException();
        Serie serie = restTemplate.getForObject("http://localhost:8001/series/"+serieid, Serie.class);
        if (serie == null || this.user.getId() != serie.getUserid())
            throw new ForbiddenException("You cannot revoke permissions on a serie you do not own");
        sharedSeriesService.removeAccess(userid, serieid);
    }

    @DeleteMapping(path = "/share/{serieid}")
    public void privatize(@PathVariable Long serieid) {
        if (this.user == null)
            throw new RequestUnauthorizedException();
        Serie serie = restTemplate.getForObject("http://localhost:8001/series/"+serieid, Serie.class);
        if (serie == null || this.user.getId() != serie.getUserid())
            throw new ForbiddenException("You cannot privatize a serie you do not own");
        sharedSeriesService.privatizeSerie(serieid);
    }

    @RequestMapping(path = "/users")
    public List<User> getAllUsers(@RequestParam(name = "name", required = false) String name) {
        List<User> users = null;
        if(name != null)
            users = userService.findByUserName(name);
        else
            users = userService.getAll();
        if(users.isEmpty())
            throw new NoContentException("No Users found");
        return users;
    }

    @RequestMapping(path = "/users/{id}")
    public List<User> getUserById(@PathVariable Long id) {
        List<User> users = userService.findById(id);
        if(users.isEmpty())
            throw new NoContentException("This user does not exist");
        return userService.findById(id);
    }

    @PostMapping(path = "/users", consumes = "application/json", produces = "application/json")
    public void register(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null || user.getRole()==null)
            throw new BadRequestException();
        userService.createUser(user.getUsername(), user.getPassword(), user.getRole());
        throw new CreatedException();
    }

    @RequestMapping(path = "/users/login", consumes = "application/json")
    public void login(@RequestBody Auth auth) {
        if(auth.username == null || auth.password == null)
            throw new BadRequestException();
        this.user = userService.login(auth.username, auth.password);
    }

    @RequestMapping(path = "/users/logout")
    public void logout() {
        this.user = null;
    }
    
    private static class Auth{
        public String username;
        public String password;
    }

    @RequestMapping(path="/authenticated")
    public User handleUser() {
        return this.user;
    }
}
