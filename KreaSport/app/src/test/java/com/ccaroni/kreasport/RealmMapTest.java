package com.ccaroni.kreasport;

import com.ccaroni.kreasport.data.legacy.RealmHelper;
import com.ccaroni.kreasport.data.legacy.dto.Checkpoint;
import com.ccaroni.kreasport.data.legacy.dto.Riddle;
import com.ccaroni.kreasport.data.legacy.realm.RealmCheckpoint;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Master on 20/05/2017.
 */

public class RealmMapTest {

    @Test
    public void testDTOToRealm() {
        String title = "AreaSelectionActivity checkpoint";
        String raceId = "dummyRaceId";

        Checkpoint checkpoint = (Checkpoint) new Checkpoint()
                .setTitle(title);
        assertEquals(title, checkpoint.getTitle());

        RealmCheckpoint realmCheckpoint = checkpoint.toRealmCheckpoint(raceId);
        assertEquals(title, realmCheckpoint.getTitle());

        String title2 = "Second checkpoint";
        List<String> answers = Arrays.asList("First Answer", "Second Answer");

        checkpoint = new Checkpoint()
                .setTitle(title2)
                .setRiddle(new Riddle()
                        .setAnswers(answers));

        realmCheckpoint = checkpoint.toRealmCheckpoint(raceId);
        assertEquals(title2, realmCheckpoint.getTitle());
        assertEquals(answers, RealmHelper.realmListToSimpleList(realmCheckpoint.getPossibleAnswers()));
    }

}
