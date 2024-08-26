package com.kcm.jpacalender.controller;

import com.kcm.jpacalender.dto.UserRequestDto;
import com.kcm.jpacalender.dto.UserResponseDto;
import com.kcm.jpacalender.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //7 단계. res 추가
    @PostMapping()
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto, HttpServletResponse res){

        return userService.createUser(userRequestDto,res);
    }

    @GetMapping("/{userId}")
    public UserResponseDto printUser(@PathVariable Long userId){
        return new UserResponseDto(userService.printUser(userId));
    }

    @GetMapping()
    public List<UserResponseDto> printUsers(){
        return userService.printUsers();
    }

    @PutMapping("/{userId}")
    public UserResponseDto updateUser(@PathVariable Long userId, @RequestBody UserRequestDto userRequestDto){
        return userService.updateUser(userId, userRequestDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }


    @PostMapping("/{userId}")
    public void placeUser(@PathVariable Long userId,
                          @RequestParam(value = "eventId")Long eventId,
                          @RequestParam(value = "placeUserId") Long placeUserId) {
        userService.placeUser(userId,eventId,placeUserId);
    }





}