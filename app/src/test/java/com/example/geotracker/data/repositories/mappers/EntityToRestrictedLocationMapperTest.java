package com.example.geotracker.data.repositories.mappers;

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
public class EntityToRestrictedLocationMapperTest {
    @Mock
    private Location mockLocation;

    private Random random = new Random();
    private EntityToRestrictedLocationMapper mapper;

    @Before
    public void setUp() {
        this.random.setSeed(System.currentTimeMillis());
        this.mapper = new EntityToRestrictedLocationMapper();

        long randomIdentifier = this.random.nextLong();
        double randomLat = this.random.nextDouble();
        double randomLng = this.random.nextDouble();
        long randomTimeStamp = System.currentTimeMillis() - this.random.nextLong();

        Mockito.when(this.mockLocation.getId()).thenReturn(randomIdentifier);
        Mockito.when(this.mockLocation.getLatitude()).thenReturn(randomLat);
        Mockito.when(this.mockLocation.getLongitude()).thenReturn(randomLng);
        Mockito.when(this.mockLocation.getTimestamp()).thenReturn(randomTimeStamp);
    }

    @Test
    public void givenSourceEntity_thenReturnConsistentlyMappedDTO() throws Exception {
        RestrictedLocation restrictedLocation = this.mapper.apply(this.mockLocation);
        Mockito.verify(this.mockLocation).getId();
        Mockito.verify(this.mockLocation).getLatitude();
        Mockito.verify(this.mockLocation).getLongitude();
        Mockito.verify(this.mockLocation).getTimestamp();
        Mockito.verify(this.mockLocation, Mockito.times(0)).getJourneyId();
        Assert.assertEquals(restrictedLocation.getIdentifier(), this.mockLocation.getId());
        Assert.assertEquals(restrictedLocation.getLatitude(), this.mockLocation.getLatitude(), 0);
        Assert.assertEquals(restrictedLocation.getLongitude(), this.mockLocation.getLongitude(), 0);
        Assert.assertEquals(restrictedLocation.getRecordedAtIso(), DateTimeUtils.utcMillisToDateTimeIsoString(this.mockLocation.getTimestamp()));
    }
}
