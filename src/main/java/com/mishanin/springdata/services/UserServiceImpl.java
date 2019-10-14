package com.mishanin.springdata.services;

import com.mishanin.springdata.entities.Role;
import com.mishanin.springdata.entities.User;
import com.mishanin.springdata.entities.VerificationToken;
import com.mishanin.springdata.repositories.RoleRepository;
import com.mishanin.springdata.repositories.UserRepository;
import com.mishanin.springdata.repositories.VerificationTokenRepository;
import com.mishanin.springdata.utils.UserDTO;
import com.mishanin.springdata.utils.enums.TypeRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private VerificationTokenRepository tokenRepository;
    private BCryptPasswordEncoder passwordEncoder;

    private static final String ANON = "anon";

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setTokenRepository(VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUserName(String username) {
        return userRepository.findOneByUsername(username);
    }

    @Override
    public User getUser(String verificationToken) {
        User user = tokenRepository.findByToken(verificationToken).getUser();
        return user;
    }

    @Override
    public VerificationToken getVerificationToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        tokenRepository.save(verificationToken);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(@NotNull final String userName) throws UsernameNotFoundException {

        final boolean enabled = true;
        final boolean accountNonExpired = true;
        final boolean credentialsNonExpired = true;
        final boolean accountNonLocked = true;

        User user = userRepository.findOneByUsername(userName);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public User save(@NotNull final UserDTO systemUser) {
        User user = new User();
        if (findByUserName(systemUser.getUserName()) != null) {
            throw new RuntimeException("User with username " + systemUser.getUserName() + " is already exist");
        }
        user.setUsername(systemUser.getUserName());
        user.setPassword(passwordEncoder.encode(systemUser.getPassword()));
        user.setFirstname(systemUser.getFirstName());
        user.setLastname(systemUser.getLastName());
        user.setEmail(systemUser.getEmail());
        user.setPhone(systemUser.getPhone());
        user.setRoles(Arrays.asList(roleRepository.findOneByName("ROLE_CUSTOMER")));
        user.setTypereg(TypeRegistration.FULL.toString());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User saveFull(UserDTO userDTO) {
        User user = userRepository.findByPhone(userDTO.getPhone());
        user.setFirstname(userDTO.getFirstName());
        user.setLastname(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setTypereg(TypeRegistration.FULL.toString());
        user.setUsername(userDTO.getUserName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public User save(@NotNull final User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User saveAnonimus(@NotNull final String phone){
        return userRepository.save(new User(
                ANON,
                ANON,
                ANON,
                ANON,
                ANON,
                phone,
                Arrays.asList(roleRepository.findOneByName("ROLE_CUSTOMER")),
                TypeRegistration.SHORT,
                false
        ));
    }
}
