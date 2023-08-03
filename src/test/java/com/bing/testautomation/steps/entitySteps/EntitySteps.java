package com.bing.testautomation.steps.entitySteps;

import core.domainentity.BaseStep;
import core.domainentity.entity.Entity;
import core.domainentity.entity.EntityBuilder;
import io.cucumber.java.en.Given;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntitySteps {

    // Set slf4j logger
    private static final Logger LOGGER = LoggerFactory.getLogger(EntitySteps.class);
    // Define BaseStep object
    private BaseStep baseStep = new BaseStep();

    @Given("^an entity$")
    public void an_entity() throws Throwable {
        Entity entity = EntityBuilder.buildEntity();
        this.baseStep.setEntity(entity);
    }

    @Given("^an entity for \"([^\"]*)\"$")
    public void an_entity_for_specific_user(final String type) throws Throwable {
        this.baseStep.setTestValues("previous_entity", EntityBuilder.getTestContext());
        Entity entity = EntityBuilder.buildEntity(type);
        this.baseStep.setEntity(entity);
        LOGGER.info("Building " + type + " entity");
    }
}
