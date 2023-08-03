package core.domainentity;

import core.domainentity.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class BaseStep {

    // Set slf4j logger
    private static final Logger SLF4J_LOGGER = LoggerFactory.getLogger(BaseStep.class);
    private static final java.util.logging.Logger JAVA_LOGGER = java.util.logging.Logger.getLogger(BaseStep.class.getName());

    // To cache values required by tests during execution
    private final Map<String, Object> testValues = new HashMap<>();

    private Entity entity;

    /*
     * Initialize BaseStep
     */
    public BaseStep() {
        String msg = "Initializing BaseStep";
        SLF4J_LOGGER.info(msg);
        JAVA_LOGGER.log(Level.INFO, msg);
    }

    /*
     * @param : key - the key to set a value to
     *
     * @param : value - the value for the respective key
     */
    public void setTestValues(final String key, final String value) {
        this.testValues.put(key,value);
    }

    /*
     * @return the testValues
     */
    public Map<String, Object> getTestValues() {
        return this.testValues;
    }

    /*
     * @return - the entity
     */
    public Entity getEntity() {
        return this.entity;
    }

    /*
     * @param : entity - the entity to set
     */
    public void setEntity(final Entity entity) {
        this.entity = entity;
    }
}
