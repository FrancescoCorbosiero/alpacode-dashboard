package studio.alpacode.app.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.alpacode.app.domain.Role;
import studio.alpacode.app.domain.Status;
import studio.alpacode.app.domain.User;
import studio.alpacode.app.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByInviteToken(String token) {
        return userRepository.findByInviteToken(token);
    }

    public List<User> findAllClients() {
        return userRepository.findAllClients();
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User createAdmin(String email, String password, String name) {
        User admin = new User(email, name, Role.ADMIN, Status.ACTIVE);
        admin.setPassword(passwordEncoder.encode(password));
        return userRepository.save(admin);
    }

    @Transactional
    public User inviteClient(String email, String name, String companyName) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Un utente con questa email esiste già");
        }

        User client = new User(email, name, Role.CLIENT, Status.PENDING);
        client.setCompanyName(companyName);
        client.setInviteToken(UUID.randomUUID().toString());
        client.setInviteTokenExpiry(LocalDateTime.now().plusHours(48));

        User savedClient = userRepository.save(client);

        // Send invite email
        emailService.sendInviteEmail(email, name, savedClient.getInviteToken());

        return savedClient;
    }

    @Transactional
    public boolean activateAccount(String inviteToken, String password) {
        Optional<User> userOpt = userRepository.findByInviteToken(inviteToken);

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();

        if (!user.isInviteTokenValid()) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(Status.ACTIVE);
        user.setInviteToken(null);
        user.setInviteTokenExpiry(null);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
        return true;
    }

    @Transactional
    public void updateUser(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void disableClient(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            if (user.isClient()) {
                user.setStatus(Status.DISABLED);
                user.setUpdatedAt(LocalDateTime.now());
                userRepository.save(user);
            }
        });
    }

    @Transactional
    public void enableClient(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            if (user.isClient() && user.getStatus() == Status.DISABLED) {
                user.setStatus(Status.ACTIVE);
                user.setUpdatedAt(LocalDateTime.now());
                userRepository.save(user);
            }
        });
    }

    @Transactional
    public void resendInvite(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            if (user.isClient() && user.isPending()) {
                user.setInviteToken(UUID.randomUUID().toString());
                user.setInviteTokenExpiry(LocalDateTime.now().plusHours(48));
                user.setUpdatedAt(LocalDateTime.now());
                userRepository.save(user);
                emailService.sendInviteEmail(user.getEmail(), user.getName(), user.getInviteToken());
            }
        });
    }
}
