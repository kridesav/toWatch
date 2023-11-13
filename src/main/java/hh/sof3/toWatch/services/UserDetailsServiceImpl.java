package hh.sof3.toWatch.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hh.sof3.toWatch.models.User;
import hh.sof3.toWatch.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository uRepo;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.uRepo = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            
        User curruser = uRepo.findByUsername(username);
        if (curruser == null) {
            // If the user is not found, create a guest user
            curruser = new User();
            curruser.setUsername("guest");
            curruser.setRole("GUEST");
            curruser.setPasswordHash("$2a$12$ZDuXu.5xjKbHePSzCsEQC.5qCDi263AyGb8h6MxP/CqDe7RIq.Oj6"); // password: guest
        }
        UserDetails user = new org.springframework.security.core.userdetails.User(username, curruser.getPasswordHash(),
            AuthorityUtils.createAuthorityList(curruser.getRole()));

        return user;
    }
}