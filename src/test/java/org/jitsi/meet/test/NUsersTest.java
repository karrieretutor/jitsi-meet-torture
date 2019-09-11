/*
 * Copyright @ 2018 karriere tutor GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jitsi.meet.test;

import org.jitsi.meet.test.base.*;
import org.jitsi.meet.test.tasks.*;
import org.jitsi.meet.test.web.*;

import org.testng.annotations.*;

import java.util.concurrent.*;

/**
 * A test that will connect N users to a conference and will let them "talk" for a while
 * @author Kevin Seidel
 */
public class NUsersTest
    extends WebTestBase
{

    private static String NUSERS_N_OPT = "nusers.n";

    @Override
    public boolean skipTestByDefault()
    {
        return true;
    }

    @Override
    public void setupClass()
    {
        super.setupClass();

        String snUsers = System.getProperty(NUSERS_N_OPT);
        int nusers;
        if (snUsers != null) {
            nusers = Integer.parseInt(snUsers);
        }
        else {
            nusers = 5;
        }

        ensureNParticipants(nusers);
        // ensureTwoParticipants();
        

        // String participantsToCreate = System.getProperty("nusers.participants");
        // int numParticipants;

        // try {
        //     numParticipants = Integer.parseInt(participantsToCreate)
        // } 
        // catch (Exception e) {
        //     System.err.printf("Provided nuseres.participants option %s not a number\nUsing default value (5)", 
        //         participantsToCreate);
        //     numParticipants= 5;
        // }

        // ParticipantOptions opts = ParticipantOptions.
        // for (int i = 0; i < numParticipants; i++) {
        //     participants.createParticipant(configPrefix)
        // }
    }

    /**
     * A Test that joins nusers.participants (3 by default) into a conference, then runs
     * for nusers.duration minutes (5 by default). This Test is based on Damian Minkovs LongLivedTest
     */
    @Test
    public void testNUsersTest()
    {
        String timeToRunInMin = System.getProperty("nusers.duration");

        // default is 5 minutes
        if (timeToRunInMin == null || timeToRunInMin.length() == 0)
        {
            timeToRunInMin = "5";
        }

        final int minutesToRun = Integer.valueOf(timeToRunInMin);

        // execute every 10 secs.
        // sometimes the check is executed once more
        // at the time while we are in a process of disposing
        // the two participants and ~9 secs before finishing successful
        // it fails.
        int millsToRun = (minutesToRun - 1) * 60 * 1000;
        HeartbeatTask heartbeatTask
            = new HeartbeatTask(
                getParticipant1(),
                getParticipant2(),
                millsToRun,
                true);

        heartbeatTask.start(10 * 1000, 10 * 1000);

        heartbeatTask.await(minutesToRun, TimeUnit.MINUTES);
    }
}
