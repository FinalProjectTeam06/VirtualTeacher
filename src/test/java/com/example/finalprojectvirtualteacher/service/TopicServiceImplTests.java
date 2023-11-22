package com.example.finalprojectvirtualteacher.service;

import com.example.finalprojectvirtualteacher.models.Topic;
import com.example.finalprojectvirtualteacher.repositories.contracts.TopicRepository;
import com.example.finalprojectvirtualteacher.services.TopicServiceImpl;
import com.example.finalprojectvirtualteacher.services.contacts.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.finalprojectvirtualteacher.Helpers.createMockTopic;
import static com.example.finalprojectvirtualteacher.Helpers.createMockTopics;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicServiceImplTests {

    private TopicService topicService;
    private TopicRepository topicRepositoryMock;

    @BeforeEach
    public void setUp() {
        topicRepositoryMock = mock(TopicRepository.class);
        topicService = new TopicServiceImpl(topicRepositoryMock);
    }

    @Test
    void getAll_ShouldReturnListOfTopics() {
        // Arrange
        List<Topic> mockTopics = createMockTopics();
        when(topicRepositoryMock.getAll()).thenReturn(mockTopics);

        // Act
        List<Topic> result = topicService.getAll();

        // Assert
        assertEquals(mockTopics, result);
        verify(topicRepositoryMock, times(1)).getAll();
    }

    @Test
    void getById_ShouldReturnTopic_WhenIdExists() {
        // Arrange
        int topicId = 1;
        Topic mockTopic = createMockTopic(topicId);
        when(topicRepositoryMock.getById(topicId)).thenReturn(mockTopic);

        // Act
        Topic result = topicService.getById(topicId);

        // Assert
        assertEquals(mockTopic, result);
        verify(topicRepositoryMock, times(1)).getById(topicId);
    }

    @Test
    void getById_ShouldReturnNull_WhenIdDoesNotExist() {
        // Arrange
        int nonExistentTopicId = 99;
        when(topicRepositoryMock.getById(nonExistentTopicId)).thenReturn(null);

        // Act
        Topic result = topicService.getById(nonExistentTopicId);

        // Assert
        assertEquals(null, result);
        verify(topicRepositoryMock, times(1)).getById(nonExistentTopicId);
    }


}