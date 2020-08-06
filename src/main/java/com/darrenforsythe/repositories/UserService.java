package com.darrenforsythe.repositories;

public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(String name, String employeeId) {
        var user = new User();
        user.setName(name);
        user.setEmployeeId(employeeId);
        userRepository.save(user);
    }
}
