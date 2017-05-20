package com.ccaroni.kreasport;

import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.data.dto.Checkpoint;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Master on 20/05/2017.
 */

public class RealmMapTest {

    @Test
    public void testNormalBaseToRealm() {
        String title = "Dummy checkpoint";

        Checkpoint checkpoint = (Checkpoint) new Checkpoint()
                .setTitle(title);
        assertEquals(title, checkpoint.getTitle());

        RealmCheckpoint realmCheckpoint = checkpoint.toRealmCheckpoint();
        assertEquals(title, realmCheckpoint.getTitle());

        String title2 = "Second checkpoint";
        List<String> answers = Arrays.asList("First Answer", "Second Answer");

        checkpoint = new Checkpoint()
                .setTitle(title2)
                .setPossibleAnswers(answers);

        realmCheckpoint = checkpoint.toRealmCheckpoint();
        assertEquals(title2, realmCheckpoint.getTitle());
        assertEquals(answers, realmCheckpoint.getPossibleAnswersAsStrings());
    }

}
