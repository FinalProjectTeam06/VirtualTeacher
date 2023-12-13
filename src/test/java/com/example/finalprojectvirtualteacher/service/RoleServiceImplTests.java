package com.example.finalprojectvirtualteacher.service;

import com.example.finalprojectvirtualteacher.Helpers;
import com.example.finalprojectvirtualteacher.models.Role;
import com.example.finalprojectvirtualteacher.repositories.contracts.RoleRepository;
import com.example.finalprojectvirtualteacher.services.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.finalprojectvirtualteacher.Helpers.createMockRole;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTests {

    @Mock
    private RoleRepository roleRepositoryMock;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    public void setUp() {

    }

    @Test
    void getAll_ShouldReturnListOfRoles() {
        // Arrange
        List<Role> mockRoles = Helpers.createMockRoles();
        when(roleRepositoryMock.getAll()).thenReturn(mockRoles);

        // Act
        List<Role> result = roleService.getAll();

        // Assert
        assertEquals(mockRoles, result);
    }

    @Test
    void getById_ShouldReturnRole_WhenIdExists() {
        // Arrange
        int roleId = 1;
        Role mockRole = createMockRole(roleId, "MockRole");
        when(roleRepositoryMock.getById(roleId)).thenReturn(mockRole);

        // Act
        Role result = roleService.getById(roleId);

        // Assert
        assertEquals(mockRole, result);
    }

    @Test
    void getById_ShouldReturnNull_WhenIdDoesNotExist() {
        // Arrange
        int nonExistentRoleId = 99;
        when(roleRepositoryMock.getById(nonExistentRoleId)).thenReturn(null);

        // Act
        Role result = roleService.getById(nonExistentRoleId);

        // Assert
        assertEquals(null, result);
    }
}