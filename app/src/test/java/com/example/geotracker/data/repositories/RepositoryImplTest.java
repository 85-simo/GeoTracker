package com.example.geotracker.data.repositories;

import com.example.geotracker.data.persistence.room.database.JourneyDAO;
import com.example.geotracker.data.persistence.room.database.LocationDAO;
import com.example.geotracker.data.persistence.room.entities.Journey;
import com.example.geotracker.data.repositories.mappers.EntityToRestrictedJourneyMapper;
import com.example.geotracker.data.repositories.mappers.EntityToRestrictedLocationMapper;
import com.example.geotracker.rules.RxJava2Rule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryImplTest {
    @Rule
    public RxJava2Rule rxJava2Rule = new RxJava2Rule();

    private RepositoryImpl repository;

    @Mock
    private JourneyDAO mockJourneyDAO;
    @Mock
    private LocationDAO mockLocationDAO;
    @Mock
    private EntityToRestrictedJourneyMapper entityToRestrictedJourneyMapper;
    @Mock
    private EntityToRestrictedLocationMapper entityToRestrictedLocationMapper;

    @Before
    public void setUp() {
        this.repository = new RepositoryImpl(this.mockJourneyDAO, this.mockLocationDAO, this.entityToRestrictedLocationMapper, this.entityToRestrictedJourneyMapper);


    }

    @Test
    public void test() {

    }

    private List<Journey> generateRandomJourneys() {
        Random random = new Random(System.currentTimeMillis());
        int journeysSize = random.nextInt();
        List<Journey> results = new ArrayList<>(journeysSize);
        for (int i = 0; i < journeysSize; i++) {
            Journey mockJourney = Mockito.mock(Journey.class);
            long randomIdentifier = random.nextLong();
            boolean randomCompleteStatus = random.nextBoolean();
            Mockito.when(mockJourney.getId()).thenReturn(randomIdentifier);
            Mockito.when(mockJourney.isComplete()).thenReturn(randomCompleteStatus);
            results.add(mockJourney);
        }
        return results;
    }
}
