package com.mafemad.agregadordeinvestimentos.service;

import com.mafemad.agregadordeinvestimentos.controller.CreateUserDto;
import com.mafemad.agregadordeinvestimentos.controller.UpdateUserDto;
import com.mafemad.agregadordeinvestimentos.entity.User;
import com.mafemad.agregadordeinvestimentos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    //AAA aproach
    //ARRANGE
    //ACT
    //ASSERT

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Nested
    class createUser{

        @Test
        @DisplayName("should create a user with success")
        void shouldCreateUser(){
            //arrange
            var user = new User(
                    UUID.randomUUID(),
                    "userName",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null
            );
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
            var input = new CreateUserDto(
                    "userName",
                    "email@email.com",
                    "password");

            //act
            var result = userService.createUser(input);
            //assert
            assertNotNull(result);

            var userCaptured = userArgumentCaptor.getValue();
            assertEquals(input.userName(), userCaptured.getUserName());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());
        }

        @Test
        @DisplayName("should throw exception when error occurs")
        void shouldThrowExceptionWhenErrorOccurs(){
            //arrange
            doReturn(new RuntimeException()).when(userRepository).save(any());
            var input = new CreateUserDto(
                    "userName",
                    "email@email.com",
                    "password");

            //act & assert
            assertThrows(RuntimeException.class, ()->userService.createUser(input));
        }
    }

    @Nested
    class getUseById{

        @Test
        @DisplayName("should get user by id with success when optional is present")
        void shouldGetUserByIdWhenOptionalIsPresent(){
            //Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "userName",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null
            );
            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());

            //Act
            var output = userService.getUserById(user.getUserId().toString());

            //Assert
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("should get user by id with success when optional is empty")
        void shouldGetUserByIdWhenOptionalIsEmpty(){
            //Arrange
            var userId = UUID.randomUUID();
            doReturn(Optional.empty())
                    .when(userRepository).
                    findById(uuidArgumentCaptor.capture());

            //Act
            var output = userService.getUserById(userId.toString());

            //Assert
            assertTrue(output.isEmpty());
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }
    }

    @Nested
    class listUser{

        @Test
        @DisplayName("should list all users with success")
        void shouldListAllUsersWithSuccess(){
            //Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "userName",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null
            );
            var userList = List.of(user);
            doReturn(userList)
                    .when(userRepository)
                    .findAll();
            //Act
            var output = userService.getUsers();
            //Assert
            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }
    }

    @Nested
    class deleteUser{
        @Test
        @DisplayName("should delete user with success when user exists")
        void shouldDeleteUserWithSuccessWhenUserExists(){
            //Arrange

            doReturn(true)
                    .when(userRepository)
                    .existsById(uuidArgumentCaptor.capture());

            doNothing()
                    .when(userRepository)
                    .deleteById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();

            //Act
            userService.deleteById(userId.toString());

            //Assert
            var idList = uuidArgumentCaptor.getAllValues();
            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));

            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));
        }

        @Test
        @DisplayName("should NOT delete user when user Does not exists")
        void shouldNotDeleteUserWhenUserDoesNotExists(){
            //Arrange

            doReturn(false)
                    .when(userRepository)
                    .existsById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();

            //Act
            userService.deleteById(userId.toString());

            //Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1)).existsById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).deleteById(any());
        }


    }

    @Nested
    class updateUserById{

        @Test
        @DisplayName("should update user by id when user exists and username and password are filled")
        void shouldUpdateUserByIdWhenUserExistsAndUsernameAndPasswordAreFilled(){
            //Arrange
            var updateUserDto = new UpdateUserDto("newUsername", "newPassword");

            var user = new User(
                    UUID.randomUUID(),
                    "userName",
                    "email@email.com",
                    "password",
                    Instant.now(),
                    null
            );
            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());
            doReturn(user)
                    .when(userRepository)
                    .save(userArgumentCaptor.capture());

            //Act
            userService.updateUserById(user.getUserId().toString(), updateUserDto);

            //Assert
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(updateUserDto.username(), userCaptured.getUserName());
            assertEquals(updateUserDto.password(), userCaptured.getPassword());

            verify(userRepository, times(1))
                    .findById(uuidArgumentCaptor.capture());
            verify(userRepository, times(1)).save(user);
        }

        @Test
        @DisplayName("should not update user by id when user does not exists")
        void shouldNotUpdateUserByIdWhenUserDoesNotExists(){
            //Arrange
            var updateUserDto = new UpdateUserDto("newUsername", "newPassword");

            var userId = UUID.randomUUID();

            doReturn(Optional.empty())
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());

            //Act
            userService.updateUserById(userId.toString(), updateUserDto);

            //Assert

            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1))
                    .findById(uuidArgumentCaptor.capture());
            verify(userRepository, times(0)).save(any());
        }
    }
}