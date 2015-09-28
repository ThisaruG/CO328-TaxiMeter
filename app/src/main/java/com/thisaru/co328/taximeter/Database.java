package com.thisaru.co328.taximeter;

import io.orchestrate.client.*;

/**
 * Created by Sachin on 9/20/2015.
 */


public class Database {

    private double roadCondtionValue = 1.0;
    public double getRoadCondition (String startTown, String stopTown) {
        Client client = OrchestrateClient.builder("8ef650cc-e964-43b9-b086-94510bc981b1").build();

        client.kv("someCollection", "someKey")
                .get(DomainObject.class)
                .on(new ResponseAdapter<KvObject<DomainObject>>() {
                    @Override
                    public void onFailure(final Throwable error) {
                        // handle errors
                    }

                    @Override
                    public void onSuccess(final KvObject<DomainObject> object) {
                        if (object == null) {
                            // we received a 404, no KV object exists
                        }

                        DomainObject data = object.getValue();
                        roadConditionValue = data;
                    }
                });

        return roadCondtionValue;
    }
}
