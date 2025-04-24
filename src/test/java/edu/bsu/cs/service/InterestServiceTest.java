package edu.bsu.cs.service;

import edu.bsu.cs.dao.InterestDAO;
import edu.bsu.cs.model.Interest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InterestServiceTest {

    private InterestDAO interestDAOMock;
    private InterestService interestService;

    @BeforeEach
    void setUp() {
        interestDAOMock = mock(InterestDAO.class);
        interestService = new InterestService(interestDAOMock);
    }

    @Test
    void testGetAllInterests_ReturnsListOfInterests() {
        Interest interest1 = new Interest();
        Interest interest2 = new Interest();
        when(interestDAOMock.findAll()).thenReturn(List.of(interest1, interest2));

        List<Interest> interests = interestService.getAllInterests();

        assertNotNull(interests);
        assertEquals(2, interests.size());
        verify(interestDAOMock).findAll();
    }

    @Test
    void testFindByNameIgnoreCase_WhenInterestExists_ReturnsInterest() {
        String interestName = "Music";
        Interest interest = new Interest();
        when(interestDAOMock.findByName(interestName)).thenReturn(Optional.of(interest));

        Optional<Interest> result = interestService.findByNameIgnoreCase(interestName);

        assertTrue(result.isPresent());
        assertEquals(interest, result.get());
        verify(interestDAOMock).findByName(interestName);
    }

    @Test
    void testFindByNameIgnoreCase_WhenInterestDoesNotExist_ReturnsEmpty() {
        String interestName = "Sports";
        when(interestDAOMock.findByName(interestName)).thenReturn(Optional.empty());

        Optional<Interest> result = interestService.findByNameIgnoreCase(interestName);

        assertFalse(result.isPresent());
        verify(interestDAOMock).findByName(interestName);
    }

    @Test
    void testSave_SavesInterest() {
        Interest interest = new Interest();
        when(interestDAOMock.save(interest)).thenReturn(interest);

        Interest savedInterest = interestService.save(interest);

        assertNotNull(savedInterest);
        assertEquals(interest, savedInterest);
        verify(interestDAOMock).save(interest);
    }
}
