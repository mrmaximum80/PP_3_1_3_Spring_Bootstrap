package springboot.app.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import springboot.app.model.Role;
import springboot.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.app.dao.RoleDAO;
import springboot.app.dao.UserDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public List<User> getAllUsers() {
        List users = userDAO.getAllUsers();
        return users;
    }

    @Override
    public User getUser(long id) {
        return userDAO.getUser(id);
    }

    @Override
    public void saveUser(User user) {
        userDAO.saveUser(user);
    }

    @Override
    public void deleteUser(long id) {
        userDAO.deleteUser(id);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDAO.getAllRoles();
    }

    @Override
    public Role getRole(long id) {
        return roleDAO.getRole(id);
    }

    @Override
    public void saveRole(Role role) {
        roleDAO.saveRole(role);
    }

    @Override
    public void deleteRole(long id) {
        roleDAO.deleteRole(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    @Override
    public User createUserPassword(User user) {
        Long id = userDAO.getAllUsers().stream().map(x -> x.getId()).max(Long::compare).orElse(null);
        user.setPassword("user" + (id + 1));
        return user;
    }

    @Override
    public User addUserRoles(User user) {
        List<String> roles = Arrays.asList(user.getRole().split(","));
        List<Role> roleList = roleDAO.getAllRoles();
        List<Role> userRoles = new ArrayList<>();
        for (Role roleFromList : roleList) {
            for (String role : roles)
                if (roleFromList.getName().equals(role)) {
                    userRoles.add(roleFromList);
                }
        }
        user.setRoles(userRoles);
        return user;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.getUserByUsername(username);
        user.getRoles().get(0);
        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        return user;
    }
}

