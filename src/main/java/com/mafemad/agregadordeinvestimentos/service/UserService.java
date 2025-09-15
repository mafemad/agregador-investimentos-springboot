package com.mafemad.agregadordeinvestimentos.service;

import com.mafemad.agregadordeinvestimentos.controller.dto.AccountResponseDTO;
import com.mafemad.agregadordeinvestimentos.controller.dto.CreateAccountDTO;
import com.mafemad.agregadordeinvestimentos.controller.dto.CreateUserDto;
import com.mafemad.agregadordeinvestimentos.controller.dto.UpdateUserDto;
import com.mafemad.agregadordeinvestimentos.entity.Account;
import com.mafemad.agregadordeinvestimentos.entity.BillingAddress;
import com.mafemad.agregadordeinvestimentos.entity.User;
import com.mafemad.agregadordeinvestimentos.repository.AccountRepository;
import com.mafemad.agregadordeinvestimentos.repository.BillingAddressRepository;
import com.mafemad.agregadordeinvestimentos.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    private AccountRepository accountRepository;

    private BillingAddressRepository billingAddressRepository;

    public UserService(UserRepository userRepository,
                       AccountRepository accountRepository,
                       BillingAddressRepository billingAddressRepository) {

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
    }

    public UUID createUser(CreateUserDto createUserDto) {

        var entity = new User(null,
                createUserDto.userName(),
                createUserDto.email(),
                createUserDto.password(),
                Instant.now(),
                null);
        var userSaved = userRepository.save(entity);

        return userSaved.getUserId();

    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId));
    }

    public void updateUserById(String userId, UpdateUserDto updateUserDto) {
        var id = UUID.fromString(userId);
        var userEntity = userRepository.findById(id);

        if(userEntity.isPresent()) {
            var user = userEntity.get();
            if(updateUserDto.username() != null) {
                user.setUserName(updateUserDto.username());
            }
            if(updateUserDto.password() != null) {
                user.setPassword(updateUserDto.password());
            }

            userRepository.save(user);
        }
    }

    public void deleteById(String userId){
        var id = UUID.fromString(userId);
        var userExist = userRepository.existsById(id);
        if(userExist){
            userRepository.deleteById(id);
        }
    }

    @Transactional
    public void createAccount(String userId, CreateAccountDTO createAccountDTO) {

        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // DTO -> Entity

        var account = new Account(
                null,
                user,
                null,
                createAccountDTO.description(),
                new ArrayList<>()
        );

        var accountCreated = accountRepository.save(account);

        var billingAddress = new BillingAddress(
                null,
                accountCreated,
                createAccountDTO.street(),
                createAccountDTO.number());



        billingAddressRepository.save(billingAddress);
    }

    public List<AccountResponseDTO> listAccounts(String userId) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return user.getAccounts().stream().map(ac -> new AccountResponseDTO
                (ac.getAccountId().toString(), ac.getDescription())).toList();
    }
}
