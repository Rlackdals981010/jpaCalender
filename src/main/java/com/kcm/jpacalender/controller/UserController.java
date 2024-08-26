package com.kcm.jpacalender.controller;

import com.kcm.jpacalender.dto.UserRequestDto;
import com.kcm.jpacalender.dto.UserResponseDto;
import com.kcm.jpacalender.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import com.kcm.jpacalender.entity.User;

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

    //8 단계. 필터를 통해서 User를 소유하게 되었음 더 이상 유저ID는 필요없음
    @GetMapping()
    public UserResponseDto printUser(HttpServletRequest req){
        User user = (User) req.getAttribute("user");
        System.out.println(user.getId());
        return new UserResponseDto(userService.printUser(user));
    }

    @GetMapping("/all")
    public List<UserResponseDto> printUsers(){
        return userService.printUsers();
    }

    //8 단계. 필터를 통해서 User를 소유하게 되었음 더 이상 유저ID는 필요없음음
    @PutMapping()
    public UserResponseDto updateUser(@RequestBody UserRequestDto userRequestDto,HttpServletRequest req){
        User user = (User) req.getAttribute("user");
        return userService.updateUser(user, userRequestDto);
    }


    //필터를 통해서 User를 소유하게 되었음 더 이상 유저ID는 필요없음
    @DeleteMapping()
    public void deleteUser(HttpServletRequest req) {
        User user = (User) req.getAttribute("user");
        userService.deleteUser(user);
    }


    @PostMapping("/place")
    public void placeUser(@RequestParam(value = "eventId")Long eventId,
                          @RequestParam(value = "placeUserId") Long placeUserId,
                          HttpServletRequest req) {
        User user = (User) req.getAttribute("user");
        userService.placeUser(user,eventId,placeUserId);
    }

}