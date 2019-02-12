package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.ConfigProperties;
import com.codenjoy.dojo.services.dao.Registration;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ValidatorTest {

    private ConfigProperties properties;
    private Registration registration;
    private Validator validator;

    @Before
    public void setUp() {
        validator = new Validator(){{
            ValidatorTest.this.registration = this.registration = mock(Registration.class);
            ValidatorTest.this.properties = this.properties = mock(ConfigProperties.class);
        }};
    }

    @Test
    public void validatePlayerId() {
        assertValidate("Player id is invalid: 'null'", () -> validator.checkPlayerId(null));
        assertValidate("Player id is invalid: ''", () -> validator.checkPlayerId(""));
        assertValidate("Player id is invalid: 'NuLL'", () -> validator.checkPlayerId("NuLL"));
        assertValidate("Player id is invalid: 'null'", () -> validator.checkPlayerId("null"));
        assertValidate("Player id is invalid: 'NULL'", () -> validator.checkPlayerId("NULL"));
        assertValidate("Player id is invalid: '*F(@DF^@(&@DF(@^'", () -> validator.checkPlayerId("*F(@DF^@(&@DF(@^"));
        assertValidate("Player id is invalid: 'too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'",
                () -> validator.checkPlayerId("too large aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));

        assertValidate("", () -> validator.checkPlayerId("1"));
        assertValidate("", () -> validator.checkPlayerId("someId"));
    }

    private void assertValidate(String expectedException, Runnable toRun) {
        try {
            if (toRun != null) {
                toRun.run();
            }
            if (StringUtils.isNotEmpty(expectedException)) {
                fail();
            }
        } catch (Exception e) {
            assertEquals(expectedException, e.getMessage());
        }
    }

}