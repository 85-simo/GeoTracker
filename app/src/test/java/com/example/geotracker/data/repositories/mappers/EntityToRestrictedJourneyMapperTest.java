package com.example.geotracker.data.repositories.mappers;

import com.example.geotracker.data.dtos.RestrictedJourney;
import com.example.geotracker.data.persistence.room.entities.Journey;
import com.example.geotracker.utils.DateTimeUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Random;

@RunWith(MockitoJUnitRunner.class)
public class EntityToRestrictedJourneyMapperTest {
    @Mock
    private Journey mockJourney;

    private Random random = new Random();
    private EntityToRestrictedJourneyMapper mapper;

    @Before
    public void setUp() {
        this.mapper = new EntityToRestrictedJourneyMapper();
        this.random.setSeed(System.currentTimeMillis());
        long randomIdentifier = this.random.nextLong();
        boolean randomCompleteFlag = this.random.nextBoolean();
        long randomStartedAtTimestamp = System.currentTimeMillis() - this.random.nextLong();
        long randomCompletedAtTimestamp = randomStartedAtTimestamp + random.nextLong();
        Mockito.when(this.mockJourney.getId()).thenReturn(randomIdentifier);
        Mockito.when(this.mockJourney.isComplete()).thenReturn(randomCompleteFlag);
        Mockito.when(this.mockJourney.getStartedAtTimestamp()).thenReturn(randomStartedAtTimestamp);
        Mockito.when(this.mockJourney.getCompletedAtTimestamp()).thenReturn(randomCompletedAtTimestamp);
    }

    @Test
    public void givenSourceEntity_thenReturnConsistentlyMappedDTO() throws Exception {
        RestrictedJourney restrictedJourney = this.mapper.apply(this.mockJourney);
        Mockito.verify(this.mockJourney).getId();
        Mockito.verify(this.mockJourney).isComplete();
        Mockito.verify(this.mockJourney).getStartedAtTimestamp();
        Mockito.verify(this.mockJourney).getCompletedAtTimestamp();
        Assert.assertEquals(restrictedJourney.getIdentifier(), this.mockJourney.getId());
        Assert.assertEquals(restrictedJourney.isComplete(), this.mockJourney.isComplete());
        Assert.assertEquals(restrictedJourney.getStartedAtUTCDateTimeIso(), DateTimeUtils.utcMillisToDateTimeIsoString(this.mockJourney.getStartedAtTimestamp()));
        Assert.assertEquals(restrictedJourney.getCompletedAtUTCDateTimeIso(), DateTimeUtils.utcMillisToDateTimeIsoString(this.mockJourney.getCompletedAtTimestamp()));
    }
}
