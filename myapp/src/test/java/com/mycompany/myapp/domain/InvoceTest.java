package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class InvoceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoce.class);
        Invoce invoce1 = new Invoce();
        invoce1.setId(1L);
        Invoce invoce2 = new Invoce();
        invoce2.setId(invoce1.getId());
        assertThat(invoce1).isEqualTo(invoce2);
        invoce2.setId(2L);
        assertThat(invoce1).isNotEqualTo(invoce2);
        invoce1.setId(null);
        assertThat(invoce1).isNotEqualTo(invoce2);
    }
}
