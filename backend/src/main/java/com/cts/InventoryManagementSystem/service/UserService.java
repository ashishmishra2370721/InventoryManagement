package com.cts.InventoryManagementSystem.service;

import com.cts.InventoryManagementSystem.dto.LoginRequest;
import com.cts.InventoryManagementSystem.dto.RegisterRequest;
import com.cts.InventoryManagementSystem.dto.Response;
import com.cts.InventoryManagementSystem.dto.UserDTO;
import com.cts.InventoryManagementSystem.entity.User;

public interface UserService {
    Response registerUser(RegisterRequest registerRequest);
    Response loginUser(LoginRequest loginRequest);
    Response getAllUsers();
    User getCurrentLoggedInUser();
    Response updateUser(Long id, UserDTO userDTO);
    Response deleteUser(Long id);
    Response getUserTransactions(Long id);
}
