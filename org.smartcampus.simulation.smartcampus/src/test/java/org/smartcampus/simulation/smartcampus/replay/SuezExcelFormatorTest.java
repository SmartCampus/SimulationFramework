package org.smartcampus.simulation.smartcampus.replay;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * Created by foerster
 *
 * on 05/02/14.
 *
 */
public class SuezExcelFormatorTest {

    @Test
    public void testTransform() throws Exception {
        SuezExcelFormator formator = new SuezExcelFormator();
        assertEquals(1391583600000L,formator.transform(new String[] {"02/05/2014", "7"}));
    }
}
