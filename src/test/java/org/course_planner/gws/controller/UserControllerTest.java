//package org.intercom.gws.controller;
//
//import org.intercom.gws.dto.user.UserDTO;
//import org.intercom.gws.entity.UserEntity;
//import org.intercom.gws.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.concurrent.ConcurrentHashMap;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class UserControllerTest {
//    @Mock(lenient = true)
//    private UserRepository userRepository;
//
//    @Mock(lenient = true)
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    private ConcurrentHashMap<String, UserDTO> data = new ConcurrentHashMap<>();
//
//    private void initializeData() {
//        data.put("newUser", new UserDTO(null, "New User", "newuser@example.com",
//                "newUser", "password", null, null, null));
//        data.put("updateUser", new UserDTO(2L, "Update User", "updateuser@example.com",
//                "updateUser", "password", true, LocalDateTime.now(), LocalDateTime.now()));
//        data.put("deleteUser", new UserDTO(3L, "Delete User", "deleteuser@example.com",
//                "deleteUser", "password", true, LocalDateTime.now(), LocalDateTime.now()));
//    }
//
//    @Test
//    public void createUserTestValid() {
//        initializeData();
//        when(userRepository.save(Mockito.any(UserEntity.class)))
//                .thenReturn(new UserEntity(data.get("newUser")));
//        when(userService.createUser(Mockito.any(UserDTO.class)))
//                .thenReturn(new UserDTO(new UserEntity(data.get("newUser"))));
//        UserDTO response = userController.createUser(data.get("newUser"));
//        assertNotNull(response);
//    }
//}
